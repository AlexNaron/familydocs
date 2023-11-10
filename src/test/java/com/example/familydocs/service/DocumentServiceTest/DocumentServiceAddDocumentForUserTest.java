package com.example.familydocs.service.DocumentServiceTest;

import com.example.familydocs.dto.DocumentDTO;
import com.example.familydocs.model.Document;
import com.example.familydocs.service.DocumentServiceBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Test suite for the function getAllDocumentsForUser from DocumentServiceImpl class.
 */
public class DocumentServiceAddDocumentForUserTest extends DocumentServiceBaseTest {

    @BeforeEach
    public void setUp() {
        super.baseSetUp(); // This calls the setup from the base class
    }

    /**
     * Test adding a document for a user when both user and document are valid.
     */
    @Test
    public void whenAddDocumentForUser_thenDocumentShouldBeAdded() {
        // Arrange
        when(userRepository.findByUsername(any(String.class))).thenReturn(mockUser);
        when(documentRepository.save(any(Document.class))).thenReturn(mockDocument);
        when(documentMapper.toDTO(any(Document.class))).thenReturn(mockDocumentDTO);

        // Act
        DocumentDTO resultDTO = documentService.addDocumentForUser(mockUser.getUsername(), mockDocument);

        // Assert
        assertEquals(mockDocumentDTO, resultDTO, "The returned DocumentDTO should match the expected one");
    }

    /**
     * Test adding a document for a non-existing user should throw UsernameNotFoundException.
     */
    @Test
    public void whenAddDocumentForNonExistingUser_thenThrowException() {
        // Arrange
        when(userRepository.findByUsername(any(String.class))).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> documentService.addDocumentForUser("nonExistingUser", mockDocument),
                "Adding a document for a non-existing user should throw UsernameNotFoundException");
    }
}
