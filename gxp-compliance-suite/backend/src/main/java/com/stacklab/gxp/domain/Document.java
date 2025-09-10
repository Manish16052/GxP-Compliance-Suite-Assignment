package com.stacklab.gxp.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Document {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;
    private String title;
    private String type;
    private Instant createdAt = Instant.now();
    private String status = "PENDING";
    private String uploadedBy;
    @Lob
    private byte[] data;

    public Long getDocumentId() { return documentId; }
    public void setDocumentId(Long id)
    {
        this.documentId = id; }
    public String getTitle() { return title; }
    public void setTitle(String t) { this.title = t; }
    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant c) { this.createdAt = c; }
    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }
    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String u) { this.uploadedBy = u; }
}
