package com.skillswap.dto;

import com.skillswap.model.SkillTestResult;
import lombok.Data;
import java.time.LocalDateTime;

// DTO for returning skill test results
@Data
public class SkillTestResultDTO {
    private Long id;
    private int score;
    private boolean passed;
    private LocalDateTime completedAt;
    private String skillName;
    private String testTitle;
    private UserDTO user;

    public SkillTestResultDTO(SkillTestResult result) {
        this.id = result.getId();
        this.score = result.getScore();
        this.passed = result.isPassed();
        this.completedAt = result.getCompletedAt();
        this.skillName = result.getSkillTest().getSkill().getName();
        this.testTitle = result.getSkillTest().getTitle();
        this.user = new UserDTO(result.getUser());
    }
}
