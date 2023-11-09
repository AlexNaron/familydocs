package com.example.familydocs.repository;

import com.example.familydocs.model.Document;
import com.example.familydocs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByOwner(User owner);

    Optional<Document> findByIdAndOwner(Long id, User owner);

    Optional<Document> findByDocumentName(String DocumentName);
}
