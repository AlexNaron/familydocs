package com.example.familydocs.controller;

import com.example.familydocs.dto.DocumentDTO;
import com.example.familydocs.dto.DocumentUploadDTO;
import com.example.familydocs.dto.TagDTO;
import com.example.familydocs.logging.LoggableParameter;
import com.example.familydocs.model.Document;
import com.example.familydocs.model.Tag;
import com.example.familydocs.service.DocumentService;
import com.example.familydocs.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Set;

@RestController
@RequestMapping("/document")
public class DocumentController {

    private final DocumentService documentService;

    private final FileStorageService fileStorageService;

    @Autowired
    public DocumentController(DocumentService documentService, FileStorageService fileStorageService) {
        this.documentService = documentService;
        this.fileStorageService = fileStorageService;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping
    public ResponseEntity<Set<DocumentDTO>> getAllDocumentsForUser(@LoggableParameter @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(documentService.getAllDocumentsForUser(userDetails.getUsername()));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<DocumentDTO> uploadDocument(@LoggableParameter @AuthenticationPrincipal UserDetails userDetails,
                                                      @ModelAttribute DocumentUploadDTO uploadDTO) {

        MultipartFile file = uploadDTO.getFile();

        String objectName = fileStorageService.uploadFile(file, userDetails.getUsername());
        uploadDTO.setDocumentStorageName(objectName);

        Document document = uploadDTO.toDocument();

        return ResponseEntity.ok(documentService.addDocumentForUser(userDetails.getUsername(), document));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("/{documentId}")
    public ResponseEntity<DocumentDTO> deleteDocument(@LoggableParameter @AuthenticationPrincipal UserDetails userDetails,
                                                      @LoggableParameter @PathVariable Long documentId) {

        DocumentDTO documentDTO = documentService.getDocumentDTOForUserById(userDetails.getUsername(), documentId);
        String objectName = documentDTO.getDocumentStorageName();

        fileStorageService.deleteFile(objectName);

        documentDTO = documentService.deleteDocumentByIdForUser(userDetails, documentId);

        return ResponseEntity.ok(documentDTO);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{documentId}")
    public ResponseEntity<DocumentDTO> getDocumentById(@LoggableParameter @AuthenticationPrincipal UserDetails userDetails,
                                                       @LoggableParameter @PathVariable Long documentId) {

        return ResponseEntity.ok(documentService.getDocumentDTOForUserById(userDetails.getUsername(), documentId));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/{documentId}/tag")
    public ResponseEntity<DocumentDTO> addTagsForDocument(@LoggableParameter @AuthenticationPrincipal UserDetails userDetails,
                                                          @LoggableParameter @PathVariable Long documentId,
                                                          @LoggableParameter @RequestBody Tag tag) {

        String tagName = tag.getTagName();
        DocumentDTO documentDTO = documentService
                .addTagsForDocumentForUser(userDetails.getUsername(), documentId, tagName);

        return ResponseEntity.ok(documentDTO);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{documentId}/tag")
    public ResponseEntity<Set<TagDTO>> getTagsByDocumentId(@LoggableParameter @AuthenticationPrincipal UserDetails userDetails,
                                                           @LoggableParameter @PathVariable Long documentId) {

        return ResponseEntity.ok(documentService.getAllTagsDTOForDocumentForUserById(userDetails.getUsername(), documentId));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("/{documentId}/tag/{tagId}")
    public ResponseEntity<String> deleteTagByIdForDocument(@LoggableParameter @AuthenticationPrincipal UserDetails userDetails,
                                                           @LoggableParameter @PathVariable Long documentId,
                                                           @LoggableParameter @PathVariable Long tagId) {

        TagDTO tagDTO = documentService.deleteTagForDocumentForUser(userDetails.getUsername(), documentId, tagId);

        return ResponseEntity.ok(String.format("Tag with id %d and name %s has been removed from the document %d",
                tagDTO.getId(), tagDTO.getTagName(), documentId));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{documentId}/pdf")
    public ResponseEntity<InputStreamResource> downloadOdfFile(@LoggableParameter @AuthenticationPrincipal UserDetails userDetails,
                                                               @LoggableParameter @PathVariable Long documentId) {

        DocumentDTO documentDTO = documentService.getDocumentDTOForUserById(userDetails.getUsername(), documentId);
        try {
            InputStream fileStream = fileStorageService.downloadFile(documentDTO.getDocumentStorageName());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + documentDTO.getDocumentStorageName() + "\"")
                    .body(new InputStreamResource(fileStream));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
