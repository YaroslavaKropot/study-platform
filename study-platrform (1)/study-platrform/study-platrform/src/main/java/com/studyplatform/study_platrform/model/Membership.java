package com.studyplatform.study_platrform.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "memberships")
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    public Membership() {
        this.joinedAt = LocalDateTime.now();
        this.role = MemberRole.MEMBER;
    }

    public Membership(Group group, User user, MemberRole role) {
        this.group = group;
        this.user = user;
        this.role = role != null ? role : MemberRole.MEMBER;
        this.joinedAt = LocalDateTime.now();
    }


    public Long getId() { return id; }
    public Group getGroup() { return group; }
    public User getUser() { return user; }
    public MemberRole getRole() { return role; }
    public LocalDateTime getJoinedAt() { return joinedAt; }


    public void setId(Long id) { this.id = id; }
    public void setGroup(Group group) { this.group = group; }
    public void setUser(User user) { this.user = user; }
    public void setRole(MemberRole role) { this.role = role; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
}