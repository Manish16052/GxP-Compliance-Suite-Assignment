package com.stacklab.gxp.service;

import com.stacklab.gxp.domain.Document;
import com.stacklab.gxp.repo.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentService {
    private final DocumentRepository repository;
    public DocumentService(DocumentRepository repository) { this.repository = repository; }

   /* public Document save(Document d) { return repository.save(d); }*/

    public Document save(MultipartFile file) {
        try {
            Document doc = new Document();
            doc.setTitle(file.getOriginalFilename());
            //doc.setContentType(file.getContentType());
            doc.setData(file.getBytes());  // entity me `@Lob byte[] data` hona chahiye
            doc.setStatus("UPLOADED");

            return repository.save(doc);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
    public List<Document> all() { return repository.findAll(); }
    public List<Document> byStatus(String s) { return repository.findByStatus(s); }
    public Document updateStatus(Long id, String status) {
        Document d = repository.findById(id).orElseThrow();
        d.setStatus(status);
        return repository.save(d);
    }
}
