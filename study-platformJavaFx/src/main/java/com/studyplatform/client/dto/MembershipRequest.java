package com.studyplatform.client.dto;

public class MembershipRequest {
    private Long userId;
    private Long groupId;
    private String role;

    public MembershipRequest() {}

    public MembershipRequest(Long userId, Long groupId, String role) {
        this.userId = userId;
        this.groupId = groupId;
        this.role = role;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "MembershipRequest{userId=" + userId + ", groupId=" + groupId + ", role='" + role + "'}";
    }
}