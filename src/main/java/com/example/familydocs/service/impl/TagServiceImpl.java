package com.example.familydocs.service.impl;

import com.example.familydocs.exception.TagNotFoundByNameException;
import com.example.familydocs.model.Document;
import com.example.familydocs.model.Tag;
import com.example.familydocs.model.User;
import com.example.familydocs.repository.DocumentRepository;
import com.example.familydocs.repository.TagRepository;
import com.example.familydocs.repository.UserRepository;
import com.example.familydocs.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(DocumentRepository documentRepository, UserRepository userRepository,
                          TagRepository tagRepository){
        this.documentRepository = documentRepository;
        this.userRepository=userRepository;
        this.tagRepository = tagRepository;
    }
    protected String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    protected User getCurrentUser(){
        String currentUsername = getCurrentUsername();
        User currentUser = userRepository.findByUsername(currentUsername);

        if (currentUser == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        return currentUser;
    }
    public Set<Document> getAllDocumentsForCurrentUserByTagName(String tagName){
        Optional<Tag> optionalTag = tagRepository.findByTagName(tagName);
        if (!optionalTag.isPresent()) {
            throw new TagNotFoundByNameException(tagName);
        }
        Tag currentTag = optionalTag.get();
        Set<Document> allDocumentsWithTagName = currentTag.getTaggedDocuments();

        User currentUser = getCurrentUser();

        Set<Document> allDocumentsForCurrentUserWithTagName = allDocumentsWithTagName.stream()
                .filter(d -> d.getOwner().equals(currentUser)).collect(Collectors.toSet());
        return allDocumentsForCurrentUserWithTagName;
    }

}
