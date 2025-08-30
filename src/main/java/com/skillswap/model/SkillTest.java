package com.skillswap.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "skill_tests")
@Data
public class SkillTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false, unique = true)
    private Skill skill; // Each test is for one specific skill

    @Column(nullable = false)
    private String title;

    // Storing questions and answers as a JSON string for simplicity.
    // Example: [{"question": "What is a variable?", "options": ["A", "B", "C"], "answer": "A"}, ...]
    @Lob
    @Column(name = "questions_json", columnDefinition = "TEXT")
    private String questionsJson;
}
