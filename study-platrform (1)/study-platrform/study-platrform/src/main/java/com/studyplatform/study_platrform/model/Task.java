package com.studyplatform.study_platrform.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tasks")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.NEW;

    private LocalDateTime deadline;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Task() {
        this.createdAt = LocalDateTime.now();
    }

    public Task(Group group, User createdBy, String title, String description, TaskStatus status, LocalDateTime deadline) {
        this.group = group;
        this.createdBy = createdBy;
        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Group getGroup() { return group; }
    public User getCreatedBy() { return createdBy; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }
    public LocalDateTime getDeadline() { return deadline; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setGroup(Group group) { this.group = group; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}