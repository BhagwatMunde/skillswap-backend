package com.skillswap.dto;

import com.skillswap.model.Skill;
import com.skillswap.model.UserSkill;

import lombok.Data;

@Data
public class UserSkillDTO {
	 private Long skillId;
	    private String skillName;
	    private String difficultyLevel; // Stored as String
	    private boolean offering;
	    
	    public UserSkillDTO(UserSkill userSkill) {
	        this.skillId = userSkill.getSkill().getId();
	        this.skillName = userSkill.getSkill().getName();
	        this.difficultyLevel = userSkill.getSkill().getDifficultyLevel().name();
	        this.offering = userSkill.isOffering();
	    }
	    
	    // Optionally add a method to get the enum version
	    public Skill.DifficultyLevel getDifficultyLevelEnum() {
	        return Skill.DifficultyLevel.valueOf(difficultyLevel);
	    }
}
