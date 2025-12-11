package com.studyplatform.study_platrform.controller;

import com.studyplatform.study_platrform.dto.MembershipRequest;
import com.studyplatform.study_platrform.model.*;
import com.studyplatform.study_platrform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/groups/{groupId}/memberships")
public class MembershipController {

    @Autowired private MembershipRepository membershipRepository;
    @Autowired private GroupRepository groupRepository;
    @Autowired private UserRepository userRepository;

    @GetMapping
    public List<Membership> getGroupMemberships(@PathVariable Long groupId) {
        return membershipRepository.findByGroupId(groupId);
    }

    @PostMapping
    public ResponseEntity<?> addMember(
            @PathVariable Long groupId,
            @RequestBody MembershipRequest req) {

        Group group = groupRepository.findById(groupId).orElse(null);
        if (group == null) {
            return ResponseEntity.status(404).body("Group not found");
        }
        User user = userRepository.findById(req.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        if (membershipRepository.existsByGroupAndUser(group, user)) {
            return ResponseEntity.badRequest().build();
        }

        Membership m = new Membership(group, user, req.getRole() != null ? req.getRole() : MemberRole.MEMBER);
        Membership saved = membershipRepository.save(m);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateRole(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            @RequestBody MembershipRequest req) {

        Group group = groupRepository.findById(groupId).orElse(null);
        if (group == null) {
            return ResponseEntity.status(404).body("Group not found");
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        Membership membership = membershipRepository.findByGroupAndUser(group, user)
                .orElse(null);
        if (membership == null) {
            return ResponseEntity.status(404).body("Membership not found");
        }

        membership.setRole(req.getRole() != null ? req.getRole() : MemberRole.MEMBER);
        return ResponseEntity.ok(membershipRepository.save(membership));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> removeMember(@PathVariable Long groupId, @PathVariable Long userId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        if (group == null) {
            return ResponseEntity.status(404).body("Group not found");
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        Membership membership = membershipRepository.findByGroupAndUser(group, user)
                .orElse(null);
        if (membership == null) {
            return ResponseEntity.status(404).body("Membership not found");
        }

        membershipRepository.delete(membership);
        return ResponseEntity.noContent().build();
    }
}

