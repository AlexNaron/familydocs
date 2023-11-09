package com.example.familydocs.mapper;

import com.example.familydocs.dto.DocumentDTO;
import com.example.familydocs.exception.DocumentNotFoundByIdException;
import com.example.familydocs.model.Document;
import com.example.familydocs.repository.DocumentRepository;
import com.example.familydocs.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentMapper(DocumentRepository documentRepository) { this.documentRepository = documentRepository; }

    public DocumentDTO toDTO(Document document) {

        if (document == null) {
            return null;
        }
        DocumentDTO dto = new DocumentDTO();
        dto.setId(document.getId());
        dto.setDocumentName(document.getDocumentName());
        dto.setDocumentLink(document.getDocumentLink());
        dto.setDocumentDescription(document.getDocumentDescription());
        dto.setTagNames(document.getTags());

        return dto;
    }

    public Document toEntity(DocumentDTO dto) {

        if (dto == null) {
            return null;
        }

        Document existingDocument = documentRepository
                .findByDocumentName(dto.getDocumentName())
                .orElse(null);
        if (existingDocument != null) {
            return existingDocument;
        } else {
            // No existing document found. The question here is, do I want to create the doc or do I throw an error?
            // Answer ERROR
            throw new DocumentNotFoundByIdException(dto.getId());
        }
    }
}
