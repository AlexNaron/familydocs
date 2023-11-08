package com.example.familydocs.service;

import com.example.familydocs.model.Document;

import java.util.Set;

public interface  TagService {
    Set<Document> getAllDocumentsForCurrentUserByTagName(String tagName);
}
