package com.skillswap.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
@Data
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A session is created from an accepted skill request
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_request_id", nullable = false, unique = true)
    private SkillRequest skillRequest;

    @Column(nullable = false)
    private LocalDateTime scheduledTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public enum SessionStatus {
        SCHEDULED,
        COMPLETED,
        CANCELLED
    }
}
