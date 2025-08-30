package com.skillswap.repository;

import com.skillswap.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Find all notifications for a specific user, ordered by most recent
    List<Notification> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);
}
