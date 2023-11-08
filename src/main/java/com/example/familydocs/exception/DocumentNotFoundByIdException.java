package com.example.familydocs.exception;

public class DocumentNotFoundByIdException extends RuntimeException {
    public DocumentNotFoundByIdException(Long documentId) {
        super("Document with ID " + documentId + " not found.");
    }
}
