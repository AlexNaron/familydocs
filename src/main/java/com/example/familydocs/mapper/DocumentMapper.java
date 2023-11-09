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
    private final TagRepository tagRepository;

    @Autowired
    public DocumentMapper(DocumentRepository documentRepository, TagRepository tagRepository) {
        this.documentRepository = documentRepository;
        this.tagRepository = tagRepository;
    }

    public DocumentDTO toDTO(Document document) {
        if (document == null) {
            return null;
        }
        DocumentDTO dto = new DocumentDTO();
        dto.setId(document.getId());
        dto.setDocumentName(document.getDocumentName());
        dto.setStorageUrl(document.getStorageUrl());
        dto.setDocumentDescription(document.getDocumentDescription());
        dto.setTagNames(document.getTags());

        return dto;
    }

    public Document toEntity(DocumentDTO dto) {
        if (dto == null) {
            return null;
        }
        Document existingDocument =  documentRepository.findByDocumentId(dto.getId())
                .orElse(null);
        if (existingDocument != null) {
            return existingDocument;
        } else {
            // No existing document found. The question here is, do I want to create the doc or do I throw an error?
            throw new DocumentNotFoundByIdException(dto.getId());
        }
/*        return documentRepository.findByDocumentName(dto.getDocumentName())
                .orElseGet(() -> {
                    Document document = new Document();
                    document.setDocumentName(dto.getDocumentName());
                    document.setDocumentLink(dto.getDocumentLink());
                    document.setDocumentDescription(dto.getDocumentDescription());

                    Set<Tag> tags = new HashSet<>();
                    for (String tagName : dto.getTagNames()) {
                        Tag tag = tagRepository.findByTagName(tagName)
                                .orElseGet(() -> {
                                    Tag newTag = new Tag();
                                    newTag.setTagName(tagName);
                                    return tagRepository.save(newTag);
                                });
                        tags.add(tag);
                    }
                    return document;
                });*/
    }
}
