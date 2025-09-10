package com.stacklab.gxp.repo;

import com.stacklab.gxp.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByStatus(String status);
}
