package com.stacklab.gxp.web;

import com.stacklab.gxp.domain.Document;
import com.stacklab.gxp.service.DocumentService;
import com.stacklab.gxp.service.NotificationClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin
public class DocumentController {

    private final DocumentService documentService;
    private final NotificationClient notificationClient;

    public DocumentController(DocumentService documentService, NotificationClient notificationClient) {
        this.documentService = documentService;
        this.notificationClient = notificationClient;
    }

    @GetMapping
    public List<Document> all(@RequestParam(required = false) String status) {
        if (status != null && !status.isBlank()) return documentService.byStatus(status);
        return documentService.all();
    }

 /*   @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<Document>> create(@RequestBody Document d) {
        Document saved = documentService.save(d);
        return notificationClient.send("approver@company.com", "New document uploaded: " + saved.getTitle())
                .map(res -> ResponseEntity.ok(saved));
    }*/
 @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
 @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
 public ResponseEntity<Document> uploadDocument(@RequestParam("file") MultipartFile file) {
     Document saved = documentService.save(file); // service ko MultipartFile handle karne layak banana hoga
     return ResponseEntity.ok(saved);
 }


    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<Map<String, String>>> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.getOrDefault("status", "PENDING");
        documentService.updateStatus(id, status);
        return notificationClient.send("uploader@company.com", "Your document status: " + status)
                .map(res -> ResponseEntity.ok(Map.of("status", status)));
    }
}
