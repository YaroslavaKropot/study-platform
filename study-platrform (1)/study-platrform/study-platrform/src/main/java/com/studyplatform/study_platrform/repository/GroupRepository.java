package com.studyplatform.study_platrform.repository;

import com.studyplatform.study_platrform.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {}