package com.skillswap.service;

import com.skillswap.dto.SkillTestResultDTO;
import com.skillswap.model.SkillTest;
import com.skillswap.model.SkillTestResult;
import com.skillswap.repository.SkillRepository;
import com.skillswap.repository.SkillTestRepository;
import com.skillswap.repository.SkillTestResultRepository;
import com.skillswap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillTestService {

    @Autowired
    private SkillTestRepository skillTestRepository;

    @Autowired
    private SkillTestResultRepository resultRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    /**
     * Creates a new skill test (Admin functionality).
     * @param skillId The ID of the skill this test is for.
     * @param title The title of the test.
     * @param questionsJson The JSON string of questions and answers.
     * @return The created SkillTest.
     */
    public SkillTest createSkillTest(Long skillId, String title, String questionsJson) {
        SkillTest test = new SkillTest();
        test.setSkill(skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found")));
        test.setTitle(title);
        test.setQuestionsJson(questionsJson);
        return skillTestRepository.save(test);
    }

    /**
     * Submits a user's result for a skill test and returns a DTO.
     * @param userId The ID of the user taking the test.
     * @param testId The ID of the test being taken.
     * @param score The user's calculated score.
     * @param passed Whether the user passed the test.
     * @return The saved SkillTestResult as a DTO.
     */
    public SkillTestResultDTO submitTestResult(Long userId, Long testId, int score, boolean passed) {
        SkillTestResult result = new SkillTestResult();
        result.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
        result.setSkillTest(skillTestRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Skill test not found")));
        result.setScore(score);
        result.setPassed(passed);
        
        SkillTestResult savedResult = resultRepository.save(result);
        return new SkillTestResultDTO(savedResult);
    }

    /**
     * Gets all test results for a specific user as a list of DTOs.
     * @param userId The user's ID.
     * @return A list of their test results as DTOs.
     */
    public List<SkillTestResultDTO> getResultsForUser(Long userId) {
        return resultRepository.findByUserId(userId)
                .stream()
                .map(SkillTestResultDTO::new)
                .collect(Collectors.toList());
    }
}
