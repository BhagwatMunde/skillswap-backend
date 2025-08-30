package com.skillswap.dto;

import com.skillswap.model.Skill;
import lombok.Data;

// A simplified DTO for skill information
@Data
public class SkillDTO {
    private Long id;
    private String name;
    private String category;
    private Skill.DifficultyLevel difficultyLevel;

    public SkillDTO(Skill skill) {
        this.id = skill.getId();
        this.name = skill.getName();
        this.category = skill.getCategory();
        this.difficultyLevel = skill.getDifficultyLevel();
    }
}
