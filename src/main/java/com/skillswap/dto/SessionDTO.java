package com.skillswap.dto;

import com.skillswap.model.Session;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SessionDTO {

    private Long id;
    private SkillRequestDTO skillRequest;
    private LocalDateTime scheduledTime;
    private String status;
    private LocalDateTime createdAt;

    public SessionDTO(Session session) {
        this.id = session.getId();
        this.skillRequest = new SkillRequestDTO(session.getSkillRequest());
        this.scheduledTime = session.getScheduledTime();
        this.status = session.getStatus().name();
        this.createdAt = session.getCreatedAt();
    }
}
