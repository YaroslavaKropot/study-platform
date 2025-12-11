package com.studyplatform.client.dto;

public class GroupRequest {
    private String name;
    private String description;
    private Long createdBy;

    public GroupRequest() {}

    public GroupRequest(String name, String description, Long createdBy) {
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
    }

    //SessionManager
    public GroupRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    @Override
    public String toString() {
        return "GroupRequest{name='" + name + "', description='" + description + "', createdBy=" + createdBy + "}";
    }
}