package com.studyplatform.study_platrform.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private LocalDateTime timestamp;
    private String details;

    public ActivityLog(){}

    public ActivityLog(Long userId, String action, LocalDateTime timestamp, String details){
        this.userId = userId;
        this.action = action;
        this.timestamp = LocalDateTime.now();
        this.details = details;

    }
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getAction() { return action; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDetails() { return details; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setAction(String action) { this.action = action; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setDetails(String details) { this.details = details; }

}
