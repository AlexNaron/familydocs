package com.example.familydocs.service;

import io.minio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.Normalizer;
import java.util.UUID;

@Service
public class FileStorageService {

    private final MinioClient minioClient;

    private final String bucketName = "pdfs";

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Autowired
    public FileStorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error initializing storage", e);
        }
    }

    private String getUniqueFileName(String originalFilename, String userName) {

        String sanitizedOriginalFilename = Normalizer.normalize(originalFilename, Normalizer.Form.NFD);
        sanitizedOriginalFilename = sanitizedOriginalFilename.replaceAll("[^\\p{ASCII}]", "");

        String uuidToken = UUID.randomUUID().toString();
        return bucketName + "/" + userName + "/" + uuidToken + "-" + sanitizedOriginalFilename;
    }

    public String uploadFile(MultipartFile file, String userName) {

        try {
            String objectName = getUniqueFileName(file.getOriginalFilename(), userName);
            InputStream is = file.getInputStream();
            long fileSize = file.getSize();
            String contentType = file.getContentType();

            minioClient.putObject(
                    PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(is, fileSize, -1)
                    .contentType(contentType)
                    .build()
            );

            logger.info("Minio service: PDF with name {} for user {} has been uploaded", objectName, userName);

            return objectName;

        } catch (Exception e) {
            throw new RuntimeException("Error storing file", e);
        }
    }

    public InputStream downloadFile(String fileName) {

        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Error downloading file", e);
        }
    }

    public void deleteFile(String fileName) {

        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
            logger.info("Minio service: PDF with name {} has been deleted", fileName);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file", e);
        }
    }
}