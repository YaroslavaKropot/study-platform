package com.studyplatform.study_platrform.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resources")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private ResourceType type;

    @Column(name = "path_or_url")
    private String pathOrUrl;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    public Resource() {
        this.uploadedAt = LocalDateTime.now();
    }

    public Resource(Group group, User uploadedBy, String title, ResourceType type, String pathOrUrl) {
        this.group = group;
        this.uploadedBy = uploadedBy;
        this.title = title;
        this.type = type;
        this.pathOrUrl = pathOrUrl;
        this.uploadedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Group getGroup() { return group; }
    public User getUploadedBy() { return uploadedBy; }
    public String getTitle() { return title; }
    public ResourceType getType() { return type; }
    public String getPathOrUrl() { return pathOrUrl; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }

    public void setId(Long id) { this.id = id; }
    public void setGroup(Group group) { this.group = group; }
    public void setUploadedBy(User uploadedBy) { this.uploadedBy = uploadedBy; }
    public void setTitle(String title) { this.title = title; }
    public void setType(ResourceType type) { this.type = type; }
    public void setPathOrUrl(String pathOrUrl) { this.pathOrUrl = pathOrUrl; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}