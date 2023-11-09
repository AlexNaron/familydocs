package com.example.familydocs.service;

import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FileStorageService {

    private final MinioClient minioClient;
    private final String bucketName = "pdfs";

    @Autowired
    public FileStorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
        try {
            // Check if the bucket exists, and if not, create it
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error initializing storage", e);
        }
    }

    private String getUniqueFileName(String originalFilename, String userName) {
        String uuidToken = UUID.randomUUID().toString();
        return bucketName + userName + "/" + uuidToken + "-" + originalFilename;
    }

    public String uploadFile(MultipartFile file, String userName) {
        try {
            String fileName = getUniqueFileName(file.getOriginalFilename(), userName);
            InputStream is = file.getInputStream();
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                    .bucket(bucketName)
                    .filename(fileName)
                    .build()
            );
            return fileName;
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
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file", e);
        }
    }
}