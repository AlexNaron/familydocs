package com.example.familydocs.service;

import com.example.familydocs.dto.DocumentDTO;
import com.example.familydocs.dto.TagDTO;
import com.example.familydocs.model.Document;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

public interface DocumentService {

    Set<DocumentDTO> getAllDocumentsForUser(String userName);

    DocumentDTO addDocumentForUser(String userName, Document document);

    DocumentDTO getDocumentDTOForUserById(String userName, Long documentId);

    Set<TagDTO> getAllTagsDTOForDocumentForUserById(String userName, Long documentId);

    DocumentDTO addTagsForDocumentForUser(String userName, Long documentId, String tagName);

    TagDTO deleteTagForDocumentForUser(String userName, Long documentId, Long tagId);

    DocumentDTO deleteDocumentByIdForUser(UserDetails userDetails, Long documentId);
}
