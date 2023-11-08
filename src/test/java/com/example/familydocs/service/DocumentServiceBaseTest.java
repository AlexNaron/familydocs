package com.example.familydocs.service;

import com.example.familydocs.dto.DocumentDTO;
import com.example.familydocs.mapper.DocumentMapper;
import com.example.familydocs.mapper.TagMapper;
import com.example.familydocs.model.Document;
import com.example.familydocs.model.User;
import com.example.familydocs.repository.DocumentRepository;
import com.example.familydocs.repository.TagRepository;
import com.example.familydocs.repository.UserRepository;
import com.example.familydocs.service.impl.DocumentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test suite for DocumentServiceImpl class.
 */
public class DocumentServiceBaseTest {

    protected DocumentServiceImpl documentService;

    @Mock
    protected DocumentRepository documentRepository;
    @Mock
    protected UserRepository userRepository;
    @Mock
    protected TagRepository tagRepository;
    @Mock
    protected DocumentMapper documentMapper;
    @Mock
    protected TagMapper tagMapper;


    protected User mockUser;
    protected Document mockDocument;
    protected DocumentDTO mockDocumentDTO;
    protected String mockUserName;
    @BeforeEach
    public void baseSetUp() {
        MockitoAnnotations.openMocks(this);
        documentService = new DocumentServiceImpl(documentRepository, userRepository, tagRepository, documentMapper, tagMapper);

        mockUser = new User();
        mockUserName = "testUser";
        mockUser.setUsername(mockUserName);

        mockDocument = new Document();
        mockDocument.setDocumentName("Test Document");

        mockDocumentDTO = new DocumentDTO();
        mockDocumentDTO.setId(1L);
        mockDocumentDTO.setDocumentName("Test Document DTO");
    }
}

