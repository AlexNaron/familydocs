package com.example.familydocs.service.impl;

import com.example.familydocs.dto.DocumentDTO;
import com.example.familydocs.dto.TagDTO;
import com.example.familydocs.exception.DocumentNotFoundByIdException;
import com.example.familydocs.exception.TagNotFoundByIdException;
import com.example.familydocs.mapper.DocumentMapper;
import com.example.familydocs.mapper.TagMapper;
import com.example.familydocs.model.Document;
import com.example.familydocs.model.Tag;
import com.example.familydocs.model.User;
import com.example.familydocs.repository.DocumentRepository;
import com.example.familydocs.repository.TagRepository;
import com.example.familydocs.repository.UserRepository;
import com.example.familydocs.service.DocumentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private final UserRepository userRepository;

    private final TagRepository tagRepository;

    private final DocumentMapper documentMapper;

    private final TagMapper tagMapper;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, UserRepository userRepository,
                               TagRepository tagRepository, DocumentMapper documentMapper, TagMapper tagMapper){

        this.documentRepository = documentRepository;
        this.userRepository=userRepository;
        this.tagRepository = tagRepository;
        this.documentMapper = documentMapper;
        this.tagMapper = tagMapper;
    }

    protected User getUser(String userName){

        User user = userRepository.findByUsername(userName);

        if (user == null) { throw new UsernameNotFoundException(userName); }

        return user;
    }

    private Document getDocumentForUserById(String userName, Long documentId) {

        User user = getUser(userName);
        Optional<Document> optionalDocument = documentRepository.findByIdAndOwner(documentId, user);
        if (optionalDocument.isEmpty()) { throw new DocumentNotFoundByIdException(documentId); }

        return optionalDocument.get();
    }

    @Override
    public Set<DocumentDTO> getAllDocumentsForUser(String userName) {

        User user = getUser(userName);
        List<Document> documents = documentRepository.findByOwner(user);

        return documents.stream()
                .map(documentMapper::toDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public DocumentDTO addDocumentForUser(String userName, Document document) {

        User user = getUser(userName);
        document.setOwner(user);

        return documentMapper.toDTO(documentRepository.save(document));
    }

    @Override
    public DocumentDTO getDocumentDTOForUserById(String userName, Long documentId) {

        Document document = getDocumentForUserById( userName, documentId);

        return documentMapper.toDTO(document);
    }

    @Override
    public Set<TagDTO> getAllTagsDTOForDocumentForUserById(String userName, Long documentId) {

        Document document = getDocumentForUserById(userName, documentId);
        Set<Tag> tags = document.getTags();

        return tags.stream()
                .map(tagMapper::toDTO)
                .collect(Collectors.toSet());
    }

    public DocumentDTO addTagsForDocumentForUser(String userName,Long documentId, String tagName){

        Document document = getDocumentForUserById(userName, documentId);
        Tag tag = tagRepository.findByTagName(tagName)
                .orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setTagName(tagName);
                    return tagRepository.save(newTag);
                });
        document.getTags().add(tag);
        tag.getTaggedDocuments().add(document);

        tagRepository.save(tag);
        documentRepository.save(document);

        return documentMapper.toDTO(document);
    }

    public TagDTO deleteTagForDocumentForUser(String userName, Long documentId, Long tagId){

        Document document = getDocumentForUserById(userName, documentId);
        Set<Tag> tagsOfDocument = document.getTags();
        Optional<Tag> optionalTagToRemove = tagRepository.findById(tagId);
        if (optionalTagToRemove.isEmpty()) {
            throw new TagNotFoundByIdException(tagId);
        }
        Tag tagToRemove = optionalTagToRemove.get();

        tagsOfDocument.remove(tagToRemove);
        tagToRemove.getTaggedDocuments().remove(document);

        document.setTags(tagsOfDocument);
        tagToRemove.setTaggedDocuments(tagToRemove.getTaggedDocuments());

        documentRepository.save(document);
        tagRepository.save(tagToRemove);

        return tagMapper.toDTO(tagToRemove);
    }

    @Transactional
    public DocumentDTO deleteDocumentByIdForUser(UserDetails userDetails, Long documentId){

        Document document = getDocumentForUserById(userDetails.getUsername(), documentId);
        for (Tag tag : document.getTags()) { tag.getTaggedDocuments().remove(document); }
        document.getTags().clear();
        documentRepository.deleteById(documentId);

        return documentMapper.toDTO(document);
    }
}
