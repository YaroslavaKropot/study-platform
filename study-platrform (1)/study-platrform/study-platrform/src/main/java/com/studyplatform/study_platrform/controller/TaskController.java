package com.studyplatform.study_platrform.controller;

import com.studyplatform.study_platrform.model.*;
import com.studyplatform.study_platrform.repository.*;
import com.studyplatform.study_platrform.dto.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/groups/{groupId}/tasks")
    public ResponseEntity<?> createTask(
            @PathVariable Long groupId,
            @Valid @RequestBody TaskRequest taskRequest) {

        Group group = groupRepository.findById(groupId).orElse(null);
        if (group == null) {
            return ResponseEntity.status(404).body("Group with ID " + groupId + " not found");
        }

        User creator = userRepository.findById(taskRequest.getCreatedBy()).orElse(null);
        if (creator == null) {
            return ResponseEntity.status(404).body("User with ID " + taskRequest.getCreatedBy() + " not found");
        }

        Task task = new Task();
        task.setGroup(group);
        task.setCreatedBy(creator);
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());

        TaskStatus status;
        if (taskRequest.getStatus() != null && !taskRequest.getStatus().trim().isEmpty()) {
            try {
                status = TaskStatus.valueOf(taskRequest.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                status = TaskStatus.NEW;
            }
        } else {
            status = TaskStatus.NEW;
        }
        task.setStatus(status);

        if (taskRequest.getDeadline() != null) {
            task.setDeadline(taskRequest.getDeadline());
        }

        task.setCreatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);

        return ResponseEntity.ok(savedTask);
    }

    @GetMapping("/groups/{groupId}/tasks")
    public ResponseEntity<?> getGroupTasks(@PathVariable Long groupId) {
        if (!groupRepository.existsById(groupId)) {
            return ResponseEntity.status(404).body("Group not found");
        }

        return ResponseEntity.ok(taskRepository.findByGroupId(groupId));
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<?> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskRequest taskRequest) {

        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            return ResponseEntity.status(404).body("Task with ID " + taskId + " not found");
        }

        if (taskRequest.getTitle() != null) {
            task.setTitle(taskRequest.getTitle());
        }
        if (taskRequest.getDescription() != null) {
            task.setDescription(taskRequest.getDescription());
        }
        if (taskRequest.getStatus() != null) {
            try {
                task.setStatus(TaskStatus.valueOf(taskRequest.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
            }
        }
        if (taskRequest.getDeadline() != null) {
            task.setDeadline(taskRequest.getDeadline());
        }

        Task updatedTask = taskRepository.save(task);
        return ResponseEntity.ok(updatedTask);
    }
    @DeleteMapping("/groups/{groupId}/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(
            @PathVariable Long groupId,
            @PathVariable Long taskId) {

        System.out.println("[DEBUG] DELETE /groups/" + groupId + "/tasks/" + taskId);
        Group group = groupRepository.findById(groupId).orElse(null);
        if (group == null) {
            return ResponseEntity.status(404).body("Group with ID " + groupId + " not found");
        }
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            return ResponseEntity.status(404).body("Task with ID " + taskId + " not found");
        }
        if (!task.getGroup().getId().equals(groupId)) {
            return ResponseEntity.status(400).body("Task does not belong to this group");
        }

        try {
            taskRepository.delete(task);
            System.out.println("[DEBUG] Task deleted successfully: " + taskId);
            return ResponseEntity.ok("Task deleted successfully");
        } catch (Exception e) {
            System.out.println("[DEBUG] Error deleting task: " + e.getMessage());
            return ResponseEntity.status(500).body("Error deleting task: " + e.getMessage());
        }
    }

}
