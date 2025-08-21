package com.foodbe.DTO.request;

import com.foodbe.constants.Constants;

public class MediaUploadRequest {
    private String fileName;
    private String base64Data;
    private Constants.MediaType type;
    private Long referenceId;
    private boolean isPrimary;

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
}
