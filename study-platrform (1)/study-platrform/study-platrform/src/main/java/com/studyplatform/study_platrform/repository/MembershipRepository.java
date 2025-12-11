package com.studyplatform.study_platrform.repository;

import com.studyplatform.study_platrform.model.Membership;
import com.studyplatform.study_platrform.model.Group;
import com.studyplatform.study_platrform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    // Старый метод (оставить для совместимости если нужно)
    @Query("SELECT m FROM Membership m WHERE m.group.id = :groupId")
    List<Membership> findByGroupId(@Param("groupId") Long groupId);

    // Новые методы для работы с объектами
    @Query("SELECT m FROM Membership m WHERE m.group = :group AND m.user = :user")
    Optional<Membership> findByGroupAndUser(@Param("group") Group group, @Param("user") User user);

    boolean existsByGroupAndUser(Group group, User user);
}