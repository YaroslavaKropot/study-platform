package com.studyplatform.study_platrform.repository;

import com.studyplatform.study_platrform.model.Resource;
import com.studyplatform.study_platrform.model.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    @Query("SELECT r FROM Resource r WHERE r.group.id = :groupId")
    List<Resource> findByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT r FROM Resource r WHERE r.group.id = :groupId AND r.type = :type")
    List<Resource> findByGroupIdAndType(@Param("groupId") Long groupId, @Param("type") ResourceType type);
}