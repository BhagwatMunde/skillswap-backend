package com.skillswap.controller;

import com.skillswap.dto.UserSkillDTO;
import com.skillswap.model.User;
import com.skillswap.model.UserSkill;
import com.skillswap.service.UserSkillService;
import com.skillswap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-skills")
public class UserSkillController {

    @Autowired
    private UserSkillService userSkillService;
    
    @Autowired
    private UserService userService;

    static class AddUserSkillRequest {
        private Long skillId;
        private boolean offering;

        public Long getSkillId() { return skillId; }
        public void setSkillId(Long skillId) { this.skillId = skillId; }
        public boolean isOffering() { return offering; }
        public void setOffering(boolean offering) { this.offering = offering; }
    }

    private Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user = userService.findByEmail(username);
        return user.getId();
    }

    @PostMapping
    public ResponseEntity<?> addSkillToProfile(@RequestBody AddUserSkillRequest request) {
        Long userId = getAuthenticatedUserId();
        try {
            UserSkill userSkill = userSkillService.addSkill(userId, request.getSkillId(), request.isOffering());
            UserSkillDTO responseDto = new UserSkillDTO(userSkill);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<?> removeSkillFromProfile(@PathVariable Long skillId) {
        Long userId = getAuthenticatedUserId();
        try {
            userSkillService.removeSkill(userId, skillId);
            return ResponseEntity.ok("Skill removed from profile successfully.");
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    // This is the correct endpoint for the profile page to call.
    // It gets skills for the currently logged-in user.
    @GetMapping
    public ResponseEntity<List<UserSkillDTO>> getAuthenticatedUserSkills() {
        Long userId = getAuthenticatedUserId();
        List<UserSkillDTO> skills = userSkillService.getUserSkills(userId);
        return ResponseEntity.ok(skills);
    }
    
    // This endpoint can be used later for viewing other users' public profiles.
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserSkillDTO>> getUserSkillsByUserId(@PathVariable Long userId) {
        List<UserSkillDTO> skills = userSkillService.getUserSkills(userId);
        return ResponseEntity.ok(skills);
    }
}
