package com.skillswap.service;

import com.skillswap.dto.SessionDTO;
import com.skillswap.model.Session;
import com.skillswap.model.SkillRequest;
import com.skillswap.repository.SessionRepository;
import com.skillswap.repository.SkillRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SkillRequestRepository skillRequestRepository;
    
    @Autowired
    private NotificationService notificationService; // Inject NotificationService


    /**
     * Schedules a new session based on an accepted skill request.
     * @param skillRequestId The ID of the accepted SkillRequest.
     * @param scheduledTime The date and time for the session.
     * @return The newly created Session as a DTO.
     */
    @Transactional
    public SessionDTO scheduleSession(Long skillRequestId, LocalDateTime scheduledTime) {
        sessionRepository.findBySkillRequestId(skillRequestId).ifPresent(s -> {
            throw new RuntimeException("A session for this skill request has already been scheduled.");
        });

        SkillRequest request = skillRequestRepository.findById(skillRequestId)
                .orElseThrow(() -> new RuntimeException("Skill request not found."));

        if (request.getStatus() != SkillRequest.RequestStatus.ACCEPTED) {
            throw new RuntimeException("Cannot schedule a session for a request that is not accepted.");
        }

        Session newSession = new Session();
        newSession.setSkillRequest(request);
        newSession.setScheduledTime(scheduledTime);
        newSession.setStatus(Session.SessionStatus.SCHEDULED);

        Session savedSession = sessionRepository.save(newSession);
        
        // --- NEW: Notify both users that the session has been scheduled ---
        String requesterMessage = "Your session with " + savedSession.getSkillRequest().getReceiver().getName() + " for " + savedSession.getSkillRequest().getSkill().getName() + " is scheduled.";
        String receiverMessage = "Your session with " + savedSession.getSkillRequest().getRequester().getName() + " for " + savedSession.getSkillRequest().getSkill().getName() + " is scheduled.";
        
        notificationService.createNotification(savedSession.getSkillRequest().getRequester(), requesterMessage, "/sessions");
        notificationService.createNotification(savedSession.getSkillRequest().getReceiver(), receiverMessage, "/sessions");

        return new SessionDTO(savedSession);
    }

    /**
     * Updates the status of an existing session.
     * @param sessionId The ID of the session to update.
     * @param newStatus The new status for the session.
     * @return The updated Session as a DTO.
     */
    @Transactional
    public SessionDTO updateSessionStatus(Long sessionId, Session.SessionStatus newStatus) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found."));

        session.setStatus(newStatus);
        Session updatedSession = sessionRepository.save(session);
        return new SessionDTO(updatedSession);
    }

    /**
     * Gets all sessions for a specific user.
     * @param userId The ID of the user.
     * @return A list of the user's sessions as DTOs.
     */
    public List<SessionDTO> getUserSessions(Long userId) {
        return sessionRepository.findBySkillRequest_RequesterIdOrSkillRequest_ReceiverId(userId, userId)
                .stream()
                .map(SessionDTO::new)
                .collect(Collectors.toList());
    }
}
