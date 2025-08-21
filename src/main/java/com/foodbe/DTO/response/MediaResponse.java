package com.foodbe.DTO.response;

import com.foodbe.constants.Constants;

import java.time.LocalDateTime;

public class MediaResponse {
    private Long id;
    private String fileName;
    private Constants.MediaType type;
    private Long referenceId;
    private boolean isPrimary;
    private LocalDateTime createdAt;
    private String base64Data;

    public MediaResponse() {
    }

    public MediaResponse(Long id, String fileName, Constants.MediaType type,
                         Long referenceId, boolean isPrimary, LocalDateTime createdAt,
                         String base64Data) {
        this.id = id;
        this.fileName = fileName;
        this.type = type;
        this.referenceId = referenceId;
        this.isPrimary = isPrimary;
        this.createdAt = createdAt;
        this.base64Data = base64Data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String getBase64Data() {
        return base64Data;
    }

    public void setBase64Data(String base64Data) {
        this.base64Data = base64Data;
    }
}
