package com.skillswap.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSkill {

    @EmbeddedId
    private UserSkillId id;

//    @ManyToOne
//    @MapsId("userId")
//    @JoinColumn(name = "user_id")
//    @JsonIgnoreProperties("userSkills") // Add this
//    private User user;
//
//    @ManyToOne
//    @MapsId("skillId")
//    @JoinColumn(name = "skill_id")
//    @JsonIgnoreProperties("userSkills") // Add this
//    private Skill skill;
//
//    @Column(nullable = false)
//    private boolean offering; // True if the user is offering the skill, false if wanting to learn
    
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("userSkills") // Breaks circular reference
    private User user;

    @ManyToOne
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    @JsonIgnoreProperties("userSkills") // Breaks circular reference
    private Skill skill;

    @Column(nullable = false)
    private boolean offering;
}
