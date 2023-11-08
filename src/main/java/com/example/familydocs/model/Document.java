package com.example.familydocs.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String documentName;
    private String documentLink;
    private String documentDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;
    @ManyToMany(mappedBy = "taggedDocuments")
    private Set<Tag> tags = new HashSet<>();
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public void setDocumentLink(String documentLink) {
        this.documentLink = documentLink;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public String getDocumentName() {
        return documentName;
    }

    public String getDocumentLink() {
        return documentLink;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getOwner() { return owner; }

    public Long getId() {
        return id;
    }
}
