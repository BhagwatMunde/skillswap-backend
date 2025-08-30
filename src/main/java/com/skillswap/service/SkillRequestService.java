package com.skillswap.service;

import com.skillswap.dto.SkillRequestDTO;
import com.skillswap.model.SkillRequest;
import com.skillswap.repository.SkillRequestRepository;
import com.skillswap.repository.UserRepository;
import com.skillswap.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillRequestService {

    @Autowired
    private SkillRequestRepository skillRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;
    
    @Autowired
    private NotificationService notificationService;

    @Transactional
    public SkillRequestDTO createRequest(Long requesterId, Long receiverId, Long skillId, String message) {
        if (requesterId.equals(receiverId)) {
            throw new RuntimeException("You cannot send a skill request to yourself.");
        }

        skillRequestRepository.findByRequesterIdAndReceiverIdAndSkillId(requesterId, receiverId, skillId)
            .ifPresent(request -> {
                throw new RuntimeException("You have already sent a request for this skill to this user.");
            });

        SkillRequest newRequest = new SkillRequest();
        newRequest.setRequester(userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("Requester not found")));
        newRequest.setReceiver(userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found")));
        newRequest.setSkill(skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found")));
        newRequest.setMessage(message);
        newRequest.setStatus(SkillRequest.RequestStatus.PENDING);

        SkillRequest savedRequest = skillRequestRepository.save(newRequest);
        // --- NEW: Create a notification for the receiver ---
        String notifMessage = savedRequest.getRequester().getName() + " sent you a request to learn " + savedRequest.getSkill().getName() + ".";
        notificationService.createNotification(savedRequest.getReceiver(), notifMessage, "/requests");

        return new SkillRequestDTO(savedRequest);
    }

    @Transactional
    public SkillRequestDTO updateRequestStatus(Long requestId, SkillRequest.RequestStatus newStatus) {
        SkillRequest request = skillRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Skill request not found"));

        request.setStatus(newStatus);
        SkillRequest updatedRequest = skillRequestRepository.save(request);
        
        // --- NEW: Notify the original requester about the status update ---
        String notifMessage = updatedRequest.getReceiver().getName() + " has " + newStatus.name().toLowerCase() + " your request for " + updatedRequest.getSkill().getName() + ".";
        notificationService.createNotification(updatedRequest.getRequester(), notifMessage, "/requests");

        return new SkillRequestDTO(updatedRequest);
    }

    public List<SkillRequestDTO> getSentRequests(Long userId) {
        return skillRequestRepository.findByRequesterId(userId).stream()
                .map(SkillRequestDTO::new)
                .collect(Collectors.toList());
    }

    public List<SkillRequestDTO> getReceivedRequests(Long userId) {
        return skillRequestRepository.findByReceiverId(userId).stream()
                .map(SkillRequestDTO::new)
                .collect(Collectors.toList());
    }
}
