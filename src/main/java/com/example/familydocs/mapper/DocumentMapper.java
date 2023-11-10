package com.example.familydocs.mapper;

import com.example.familydocs.dto.DocumentDTO;
import com.example.familydocs.exception.DocumentNotFoundByIdException;
import com.example.familydocs.model.Document;
import com.example.familydocs.repository.DocumentRepository;
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
        dto.setDocumentStorageName(document.getDocumentStorageName());
        dto.setDocumentDescription(document.getDocumentDescription());
        dto.setTagNames(document.getTags());

        return dto;
    }

    public Document toEntity(DocumentDTO dto) {

        if (dto == null) {
            return null;
        }
        Document existingDocument =  documentRepository.findById(dto.getId())
                .orElse(null);
        if (existingDocument != null) {

            return existingDocument;
        } else {
            throw new DocumentNotFoundByIdException(dto.getId());
        }
    }
}
