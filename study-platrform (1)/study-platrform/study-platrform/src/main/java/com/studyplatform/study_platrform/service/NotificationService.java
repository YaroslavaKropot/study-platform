package com.studyplatform.study_platrform.service;

import com.studyplatform.study_platrform.dto.Notification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotificationToAll(Notification notification) {
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }

    public void sendNotificationToGroup(Long groupId, Notification notification) {
        messagingTemplate.convertAndSend("/topic/group." + groupId, notification);
    }

    public void notifyNewTask(Long groupId, String taskTitle, Long createdBy) {
        Notification notification = new Notification(
                "NEW_TASK",
                "New task created: " + taskTitle,
                createdBy,
                groupId
        );
        sendNotificationToGroup(groupId, notification);
    }

    public void notifyNewResource(Long groupId, String resourceTitle, Long uploadedBy) {
        Notification notification = new Notification(
                "NEW_RESOURCE",
                "New resource uploaded: " + resourceTitle,
                uploadedBy,
                groupId
        );
        sendNotificationToGroup(groupId, notification);
    }
}