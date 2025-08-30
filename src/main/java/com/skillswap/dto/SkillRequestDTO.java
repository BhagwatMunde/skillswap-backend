package com.skillswap.dto;

import com.skillswap.model.SkillRequest;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SkillRequestDTO {

    private Long id;
    private UserDTO requester;
    private UserDTO receiver;
    private SkillDTO skill;
    private String status;
    private String message;
    private LocalDateTime createdAt;
    private Long sessionId; // --- NEW FIELD ---
    private String sessionStatus; // --- NEW FIELD ---

    public SkillRequestDTO(SkillRequest skillRequest) {
        this.id = skillRequest.getId();
        this.requester = new UserDTO(skillRequest.getRequester());
        this.receiver = new UserDTO(skillRequest.getReceiver());
        this.skill = new SkillDTO(skillRequest.getSkill());
        this.status = skillRequest.getStatus().name();
        this.message = skillRequest.getMessage();
        this.createdAt = skillRequest.getCreatedAt();
        
        // --- NEW: Set session info if it exists ---
        if (skillRequest.getSession() != null) {
            this.sessionId = skillRequest.getSession().getId();
            this.sessionStatus = skillRequest.getSession().getStatus().name();
        } else {
            this.sessionId = null;
            this.sessionStatus = null;
        }
    }
}
