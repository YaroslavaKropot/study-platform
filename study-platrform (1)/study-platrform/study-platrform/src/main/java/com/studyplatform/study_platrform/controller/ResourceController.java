package com.studyplatform.study_platrform.controller;

import com.studyplatform.study_platrform.model.*;
import com.studyplatform.study_platrform.repository.*;
import com.studyplatform.study_platrform.service.NotificationService;
import com.studyplatform.study_platrform.dto.ResourceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/groups/{groupId}/resources")
public class ResourceController {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private GroupRepository groupRepository; // Добавил

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<Resource> getGroupResources(@PathVariable Long groupId) {
        return resourceRepository.findByGroupId(groupId);
    }

    @GetMapping("/type/{type}")
    public List<Resource> getResourcesByType(@PathVariable Long groupId, @PathVariable ResourceType type) {
        return resourceRepository.findByGroupIdAndType(groupId, type);
    }
    @DeleteMapping("/{resourceId}")
    public ResponseEntity<Void> deleteResource(
            @PathVariable Long groupId,
            @PathVariable Long resourceId) {

        if (!groupRepository.existsById(groupId)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = resourceRepository.findById(resourceId).orElse(null);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        if (!resource.getGroup().getId().equals(groupId)) {
            return ResponseEntity.badRequest().build();
        }

        resourceRepository.delete(resource);

        return ResponseEntity.noContent().build();
    }
    @PostMapping
    public ResponseEntity<Resource> createResource(
            @PathVariable Long groupId,
            @Valid @RequestBody ResourceRequest resourceRequest) {

        Group group = groupRepository.findById(groupId).orElse(null);
        if (group == null) {
            return ResponseEntity.notFound().build();
        }

        User uploadedBy = userRepository.findById(resourceRequest.getUploadedBy()).orElse(null);
        if (uploadedBy == null) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new Resource();
        resource.setGroup(group);
        resource.setUploadedBy(uploadedBy);
        resource.setTitle(resourceRequest.getTitle());
        resource.setType(resourceRequest.getType());
        resource.setPathOrUrl(resourceRequest.getPathOrUrl());
        resource.setUploadedAt(LocalDateTime.now());

        Resource savedResource = resourceRepository.save(resource);

        notificationService.notifyNewResource(
                groupId,
                savedResource.getTitle(),
                uploadedBy.getId()
        );

        return ResponseEntity.ok(savedResource);
    }
}