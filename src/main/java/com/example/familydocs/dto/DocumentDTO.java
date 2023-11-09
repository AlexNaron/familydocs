package com.example.familydocs.dto;

import com.example.familydocs.model.Tag;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DocumentDTO {
    private Long id;
    private String documentName;
    private String documentLink;
    private String documentDescription;
    private Set<String> tagNames =  new HashSet<>();
    private String storageUrl; // URL to access the PDF in object storage

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getStorageUrl() {
        return storageUrl;
    }
    public void setStorageUrl(String storageUrl) { this.storageUrl = storageUrl; }

    public String getDocumentDescription() { return documentDescription; }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public Set<String> getTagNames() { return tagNames; }

    public void setTagNames(Set<Tag> tags) {
        this.tagNames = tags.stream()
                .map(Tag::getTagName)
                .collect(Collectors.toSet());
    }
}
