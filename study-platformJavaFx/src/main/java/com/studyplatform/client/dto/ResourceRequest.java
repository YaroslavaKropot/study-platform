package com.studyplatform.client.dto;

public class ResourceRequest {
    private String title;
    private String type;
    private String pathOrUrl;
    private Long groupId;
    private Long uploadedBy;   //SessionManager

    public ResourceRequest() {}


    public ResourceRequest(String title, String url, Long groupId) {
        this.title = title;
        this.type = "LINK";
        this.pathOrUrl = url;
        this.groupId = groupId;
    }

    public ResourceRequest(String title, String type, String path, Long groupId) {
        this.title = title;
        this.type = type;
        this.pathOrUrl = path;
        this.groupId = groupId;
    }

    public ResourceRequest(String title, String type, String pathOrUrl, Long groupId, Long uploadedBy) {
        this.title = title;
        this.type = type;
        this.pathOrUrl = pathOrUrl;
        this.groupId = groupId;
        this.uploadedBy = uploadedBy;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPathOrUrl() { return pathOrUrl; }
    public void setPathOrUrl(String pathOrUrl) { this.pathOrUrl = pathOrUrl; }

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }

    public Long getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(Long uploadedBy) { this.uploadedBy = uploadedBy; }

    @Override
    public String toString() {
        return "ResourceRequest{title='" + title + "', type='" + type +
                "', pathOrUrl='" + pathOrUrl + "', groupId=" + groupId + "}";
    }
}