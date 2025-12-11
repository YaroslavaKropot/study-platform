package com.studyplatform.study_platrform.service;

import com.studyplatform.study_platrform.model.ActivityLog;
import com.studyplatform.study_platrform.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ActivityService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    public void logUserActivity(Long userId, String action, String details) {
        ActivityLog activityLog = new ActivityLog();
        activityLog.setUserId(userId);
        activityLog.setAction(action);
        activityLog.setTimestamp(LocalDateTime.now());
        activityLog.setDetails(details);
        activityLogRepository.save(activityLog);
    }

    public void logRegistration(Long userId) {
        logUserActivity(userId, "USER_REGISTERED", "User registered");
    }

    public void logLogin(Long userId) {
        logUserActivity(userId, "USER_LOGGED_IN", "User logged in");
    }

    public void logGroupCreated(Long userId, String groupName) {
        logUserActivity(userId, "GROUP_CREATED", "Group created: " + groupName);
    }

    public void logGroupCreated(String createdBy, String groupName) {
        logUserActivity(0L, "GROUP_CREATED", "Group created: " + groupName + " (created by: " + createdBy + ")");
    }

    public void logTaskCreated(Long userId, String taskTitle) {
        logUserActivity(userId, "TASK_CREATED", "Task created: " + taskTitle);
    }

    public void logResourceUploaded(Long userId, String resourceTitle) {
        logUserActivity(userId, "RESOURCE_UPLOADED", "Resource uploaded: " + resourceTitle);
    }
}