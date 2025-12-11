package com.studyplatform.study_platrform.dto;

import java.time.LocalDateTime;

public class Notification {
    private String type;
    private String message;
    private Long userId;
    private Long groupId;
    private LocalDateTime timestamp;

    public Notification() {}

    public Notification(String type, String message, Long userId, Long groupId) {
        this.type = type;
        this.message = message;
        this.userId = userId;
        this.groupId = groupId;
        this.timestamp = LocalDateTime.now();
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}