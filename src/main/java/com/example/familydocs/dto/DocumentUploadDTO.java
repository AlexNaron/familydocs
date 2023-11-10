package com.example.familydocs.dto;

import com.example.familydocs.model.Document;
import org.springframework.web.multipart.MultipartFile;

public class DocumentUploadDTO {
    private String documentDescription;
    private String documentName;
    private String documentStorageName;
    private MultipartFile file;

    public MultipartFile getFile() { return file; }

    public void setDocumentStorageName(String documentStorageName) { this.documentStorageName = documentStorageName; }

    public Document toDocument(){

        Document document = new Document();
        document.setDocumentName(documentName);
        document.setDocumentDescription(documentDescription);
        document.setDocumentStorageName(documentStorageName);

        return document;
    }

    @SuppressWarnings("unused")
    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    @SuppressWarnings("unused")
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    @SuppressWarnings("unused")
    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
