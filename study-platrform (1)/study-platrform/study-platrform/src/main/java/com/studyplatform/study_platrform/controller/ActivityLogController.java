package com.studyplatform.study_platrform.controller;

import com.studyplatform.study_platrform.model.ActivityLog;
import com.studyplatform.study_platrform.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/activity")
public class ActivityLogController {
    @Autowired
    private ActivityLogRepository activityLogRepository;

    @GetMapping
    public List<ActivityLog> getAllActivity(){
        return activityLogRepository.findAll();
    }
    @GetMapping("/user/{userId}")
    public List<ActivityLog> getActivityByUserId(@PathVariable Long userId){
        return activityLogRepository.findByUserId(userId);
    }
    @GetMapping("/action/{action}")
    public List<ActivityLog> getActivityByAction(@PathVariable String action){
        return activityLogRepository.findByAction(action);
    }
    @PostMapping
    public ActivityLog createActivityLog(@RequestBody ActivityLog activityLog){
        return activityLogRepository.save(activityLog);
    }
}
