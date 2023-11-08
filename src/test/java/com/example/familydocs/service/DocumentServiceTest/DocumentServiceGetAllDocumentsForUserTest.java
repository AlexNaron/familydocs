package com.example.familydocs.service.DocumentServiceTest;

import com.example.familydocs.dto.DocumentDTO;
import com.example.familydocs.model.Document;
import com.example.familydocs.service.DocumentServiceBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Test suite for the function getAllDocumentsForUser from DocumentServiceImpl class.
 */
@SpringBootTest
public class DocumentServiceGetAllDocumentsForUserTest extends DocumentServiceBaseTest {

    @BeforeEach
    public void setUp() {
        super.baseSetUp(); // This calls the setup from the base class
    }

    /**
     * Test to ensure that getting all documents for a user returns the correct set of DocumentDTOs.
     */
    @Test
    public void testGetAllDocumentsForUser() {
        List<Document> documentList = Arrays.asList(mockDocument);
        Set<DocumentDTO> expectedDocumentDTOSet = documentList.stream()
                .map(document -> new DocumentDTO())
                .collect(Collectors.toSet());

        // When the repository methods are called, we return the mock data
        when(userRepository.findByUsername(mockUserName)).thenReturn(mockUser);
        when(documentRepository.findByOwner(mockUser)).thenReturn(documentList);
        when(documentMapper.toDTO(mockDocument)).thenReturn(new DocumentDTO());

        // When getting all documents for the user
        Set<DocumentDTO> actualDocumentDTOSet = documentService.getAllDocumentsForUser(mockUserName);

        // Then the retrieved document set should match the expected size
        assertEquals(expectedDocumentDTOSet.size(), actualDocumentDTOSet.size());
    }

    /**
     * Test to verify that the service can handle multiple documents for a single user.
     */
    @Test
    public void testGetAllDocumentsForUserWithMultipleDocuments() {
        Document mockDocument1 = new Document();
        Document mockDocument2 = new Document();
        List<Document> documentList = Arrays.asList(mockDocument1, mockDocument2);

        when(userRepository.findByUsername(mockUserName)).thenReturn(mockUser);
        when(documentRepository.findByOwner(mockUser)).thenReturn(documentList);
        when(documentMapper.toDTO(any(Document.class))).thenAnswer(i -> new DocumentDTO());

        Set<DocumentDTO> documentDTOSet = documentService.getAllDocumentsForUser(mockUserName);

        // Assert that two documents are returned for the user
        assertEquals(2, documentDTOSet.size()); // The user should have two documents
    }

    /**
     * Test to ensure that an empty set is returned when the user has no documents.
     */
    @Test
    public void testGetAllDocumentsForUserWithNoDocuments() {
        when(userRepository.findByUsername(mockUserName)).thenReturn(mockUser);
        when(documentRepository.findByOwner(mockUser)).thenReturn(Collections.emptyList());

        Set<DocumentDTO> documentDTOSet = documentService.getAllDocumentsForUser(mockUserName);

        // Assert that the returned set is empty
        assertTrue(documentDTOSet.isEmpty());
    }

    /**
     * Test to ensure that the service throws UsernameNotFoundException for a non-existent user.
     */
    @Test
    public void testGetAllDocumentsForNonExistentUser() {
        String userName = "nonExistentUser";
        when(userRepository.findByUsername(userName)).thenReturn(null);

        // Assert that UsernameNotFoundException is thrown
        assertThrows(UsernameNotFoundException.class, () -> {
            documentService.getAllDocumentsForUser(userName);
        });
    }
}
