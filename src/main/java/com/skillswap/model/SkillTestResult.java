package com.skillswap.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "skill_test_results")
@Data
public class SkillTestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // The user who took the test

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_test_id", nullable = false)
    private SkillTest skillTest; // The test that was taken

    @Column(nullable = false)
    private int score; // The user's score

    @Column(nullable = false)
    private boolean passed; // A simple flag to indicate if they passed

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime completedAt;
}
