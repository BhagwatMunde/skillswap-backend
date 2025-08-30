package com.skillswap.dto;

import com.skillswap.model.Notification;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private String message;
    private boolean isRead;
    private String link;
    private LocalDateTime createdAt;

    public NotificationDTO(Notification notification) {
        this.id = notification.getId();
        this.message = notification.getMessage();
        this.isRead = notification.isRead();
        this.link = notification.getLink();
        this.createdAt = notification.getCreatedAt();
    }
}
