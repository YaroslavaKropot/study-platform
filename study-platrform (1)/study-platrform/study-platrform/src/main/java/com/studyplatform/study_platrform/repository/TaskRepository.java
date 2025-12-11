package com.studyplatform.study_platrform.repository;

import com.studyplatform.study_platrform.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.group.id = :groupId")
    List<Task> findByGroupId(@Param("groupId") Long groupId);

}