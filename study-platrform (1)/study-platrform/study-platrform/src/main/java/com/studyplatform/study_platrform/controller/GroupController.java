package com.studyplatform.study_platrform.controller;

import com.studyplatform.study_platrform.model.Group;
import com.studyplatform.study_platrform.model.User;
import com.studyplatform.study_platrform.repository.GroupRepository;
import com.studyplatform.study_platrform.repository.UserRepository;
import com.studyplatform.study_platrform.service.ActivityService;
import com.studyplatform.study_platrform.dto.GroupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityService activityService;

    @GetMapping
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createGroup(
            @Valid @RequestBody GroupRequest groupRequest) {

        User creator = userRepository.findById(groupRequest.getCreatedBy()).orElse(null);
        if (creator == null) {
            return ResponseEntity.status(404).body("User with ID " + groupRequest.getCreatedBy() + " not found");
        }

        Group group = new Group();
        group.setName(groupRequest.getName());
        group.setDescription(groupRequest.getDescription());
        group.setCreatedBy(creator);
        group.setCreatedAt(LocalDateTime.now());

        Group savedGroup = groupRepository.save(group);

        activityService.logGroupCreated(creator.getId(), savedGroup.getName());

        return ResponseEntity.ok(savedGroup);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        return groupRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupById(@PathVariable Long id) {
        if (!groupRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        groupRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}