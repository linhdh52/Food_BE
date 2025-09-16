package com.foodbe.entity;

import com.foodbe.constants.Constants;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "media",
        indexes = {
                @Index(name = "idx_media_type_ref", columnList = "type, reference_id"),
                @Index(name = "idx_media_primary", columnList = "type, reference_id, is_primary")
        })
public class MediaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private Constants.MediaType type;

    @Column(name = "reference_id", nullable = false)
    private Long referenceId;

    @Column(name = "file_name", nullable = false, length = 512)
    private String fileName;

    @Lob
    @Column(name = "base64_data", nullable = false, columnDefinition = "TEXT")
    private String base64Data;

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Constants.MediaType getType() {
        return type;
    }

    public void setType(Constants.MediaType type) {
        this.type = type;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBase64Data() {
        return base64Data;
    }

    public void setBase64Data(String base64Data) {
        this.base64Data = base64Data;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
