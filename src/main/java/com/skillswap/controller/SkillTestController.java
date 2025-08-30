package com.skillswap.controller;

import com.skillswap.dto.SkillTestResultDTO;
import com.skillswap.model.SkillTest;
import com.skillswap.model.User;
import com.skillswap.service.SkillTestService;
import com.skillswap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/skill-tests")
public class SkillTestController {

    @Autowired
    private SkillTestService skillTestService;

    @Autowired
    private UserService userService;

    // DTOs for request bodies
    static class SkillTestRequest {
        public Long skillId;
        public String title;
        public String questionsJson;
    }

    static class TestResultRequest {
        public Long testId;
        public int score;
        public boolean passed;
    }
    
    private Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();
        User user = userService.findByEmail(username);
        return user.getId();
    }

    /**
     * Admin-only endpoint to create a new skill test.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<SkillTest> createSkillTest(@RequestBody SkillTestRequest request) {
        SkillTest newTest = skillTestService.createSkillTest(
            request.skillId,
            request.title,
            request.questionsJson
        );
        return new ResponseEntity<>(newTest, HttpStatus.CREATED);
    }

    /**
     * Endpoint for a user to submit their test results.
     */
    @PostMapping("/results")
    public ResponseEntity<SkillTestResultDTO> submitTestResult(@RequestBody TestResultRequest request) {
        Long userId = getAuthenticatedUserId();
        SkillTestResultDTO resultDto = skillTestService.submitTestResult(
            userId,
            request.testId,
            request.score,
            request.passed
        );
        return new ResponseEntity<>(resultDto, HttpStatus.CREATED);
    }

    /**
     * Endpoint to get all test results for a specific user.
     */
    @GetMapping("/results/user/{userId}")
    public ResponseEntity<List<SkillTestResultDTO>> getResultsForUser(@PathVariable Long userId) {
        List<SkillTestResultDTO> results = skillTestService.getResultsForUser(userId);
        return ResponseEntity.ok(results);
    }
}
