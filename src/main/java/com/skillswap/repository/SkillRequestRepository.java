package com.skillswap.repository;

import com.skillswap.model.SkillRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRequestRepository extends JpaRepository<SkillRequest, Long> {

    // Find all requests sent by a specific user
    List<SkillRequest> findByRequesterId(Long requesterId);

    // Find all requests received by a specific user
    List<SkillRequest> findByReceiverId(Long receiverId);

    // Find a specific request to prevent duplicates
    Optional<SkillRequest> findByRequesterIdAndReceiverIdAndSkillId(Long requesterId, Long receiverId, Long skillId);
}
