package com.studyplatform.study_platrform.dto;

import com.studyplatform.study_platrform.model.MemberRole;
import jakarta.validation.constraints.NotNull;

public class MembershipRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    private MemberRole role;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public MemberRole getRole() { return role; }
    public void setRole(MemberRole role) { this.role = role; }
}