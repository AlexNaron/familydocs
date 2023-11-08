package com.example.familydocs.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity

public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tagName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "document_tags",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    private Set<Document> taggedDocuments = new HashSet<>();

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
    public String getTagName() {
        return tagName;
    }
    public Set<Document> getTaggedDocuments() {
        return taggedDocuments;
    }

    public Long getId() {
        return id;
    }

    public void setTaggedDocuments(Set<Document> taggedDocuments) {
        this.taggedDocuments = taggedDocuments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id);  // assuming `id` is the unique attribute
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


