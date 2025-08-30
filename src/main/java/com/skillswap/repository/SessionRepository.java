package com.skillswap.repository;

import com.skillswap.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    /**
     * Finds a session by the ID of the skill request that created it.
     * This is useful for checking if a session has already been scheduled for a request.
     * @param skillRequestId The ID of the SkillRequest.
     * @return An Optional containing the Session if found.
     */
    Optional<Session> findBySkillRequestId(Long skillRequestId);

    /**
     * Finds all sessions where the given user is either the requester or the receiver.
     * This allows us to fetch a user's complete session history.
     * @param requesterId The user's ID.
     * @param receiverId The user's ID (should be the same as requesterId).
     * @return A list of all sessions the user is a part of.
     */
    List<Session> findBySkillRequest_RequesterIdOrSkillRequest_ReceiverId(Long requesterId, Long receiverId);
    
    // --- NEW: Methods to count completed sessions for a user's stats ---
    long countBySkillRequest_ReceiverIdAndStatus(Long receiverId, Session.SessionStatus status);
    long countBySkillRequest_RequesterIdAndStatus(Long requesterId, Session.SessionStatus status);
}
