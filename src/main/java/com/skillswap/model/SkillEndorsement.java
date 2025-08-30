package com.skillswap.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "skill_endorsements")
@Data
public class SkillEndorsement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endorser_id", nullable = false)
    private User endorser; // The user giving the endorsement

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endorsed_user_id", nullable = false)
    private User endorsedUser; // The user whose skill is being endorsed

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill; // The specific skill being endorsed

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
