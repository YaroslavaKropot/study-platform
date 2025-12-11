package com.studyplatform.study_platrform.dto;

import com.studyplatform.study_platrform.model.ResourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ResourceRequest {

    @NotBlank(message = "Resource title is required")
    private String title;

    @NotNull(message = "Resource type is required")
    private ResourceType type;

    private String pathOrUrl;

    @NotNull(message = "Uploader user ID is required")
    private Long uploadedBy;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public ResourceType getType() { return type; }
    public void setType(ResourceType type) { this.type = type; }

    public String getPathOrUrl() { return pathOrUrl; }
    public void setPathOrUrl(String pathOrUrl) { this.pathOrUrl = pathOrUrl; }

    public Long getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(Long uploadedBy) { this.uploadedBy = uploadedBy; }
}