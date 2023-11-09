package com.example.familydocs.controller;

import com.example.familydocs.dto.DocumentDTO;
import com.example.familydocs.dto.TagDTO;
import com.example.familydocs.model.Document;
import com.example.familydocs.model.Tag;
import com.example.familydocs.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping
    public Set<DocumentDTO> getAllDocumentsForUser(@AuthenticationPrincipal UserDetails userDetails) {

        return documentService.getAllDocumentsForUser(userDetails.getUsername());
    }

    @PostMapping
    public DocumentDTO addDocument(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Document document) {

        return documentService.addDocumentForUser(userDetails.getUsername(), document);
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<DocumentDTO> deleteDocument(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long documentId) {

        DocumentDTO documentDTO = documentService.deleteDocumentByIdForUser(userDetails, documentId);

        return ResponseEntity.ok(documentDTO);
    }

    @GetMapping("/{documentId}")
    public DocumentDTO getDocumentById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long documentId) {

        return documentService.getDocumentDTOForUserById(userDetails.getUsername(), documentId);
    }

    @PostMapping("/{documentId}/tag")
    public ResponseEntity<DocumentDTO> addTagsForDocument(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long documentId, @RequestBody Tag tag) {

        String tagName = tag.getTagName();
        DocumentDTO documentDTO = documentService.addTagsForDocumentForUser(userDetails.getUsername(), documentId, tagName);

        return ResponseEntity.ok(documentDTO);
    }

    @GetMapping("/{documentId}/tag")
    public Set<TagDTO> getTagsByDocumentId(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long documentId) {

        return documentService.getAllTagsDTOForDocumentForUserById(userDetails.getUsername(), documentId);
    }

    @DeleteMapping("/{documentId}/tag/{tagId}")
    public ResponseEntity<String> deleteTagByIdForDocument(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long documentId, @PathVariable Long tagId) {

        TagDTO tagDTO = documentService.deleteTagForDocumentForUser(userDetails.getUsername(), documentId, tagId);

        return ResponseEntity.ok(String.format("Tag with id %d and name %s has been removed from the document %d", tagDTO.getId(), tagDTO.getTagName(), documentId));
    }
}
