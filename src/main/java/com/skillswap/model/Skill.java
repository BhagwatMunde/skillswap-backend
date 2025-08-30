package com.skillswap.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
    
    private String category;
    
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private Boolean isActive = true;
    
    private String imageUrl;

//    @ManyToMany(mappedBy = "skills", fetch = FetchType.LAZY)
//    private Set<User> users = new HashSet<>();
    
    
    //add after in deepseek
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("skill")
    private Set<UserSkill> userSkills = new HashSet<>();

    public enum DifficultyLevel {
        BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    }

    // Constructors
    public Skill() {
    }

    public Skill(String name, String description, String category, 
                DifficultyLevel difficultyLevel, String imageUrl) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.difficultyLevel = difficultyLevel;
        this.imageUrl = imageUrl;
    }

    // Getters and setters for all fields
    // ... (include all the new fields) ...
    
    // equals() and hashCode() should be updated if you include new fields in identity
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return Objects.equals(name, skill.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
