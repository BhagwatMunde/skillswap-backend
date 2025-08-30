package com.skillswap.controller;

import com.skillswap.model.SkillEndorsement;
import com.skillswap.model.User;
import com.skillswap.service.EndorsementService;
import com.skillswap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/endorsements")
public class EndorsementController {

    @Autowired
    private EndorsementService endorsementService;

    @Autowired
    private UserService userService;

    // DTO for the request body
    static class EndorsementRequest {
        public Long endorsedUserId;
        public Long skillId;
    }
    
    private Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();
        User user = userService.findByEmail(username);
        return user.getId();
    }

    @PostMapping
    public ResponseEntity<?> addEndorsement(@RequestBody EndorsementRequest request) {
        Long endorserId = getAuthenticatedUserId();
        try {
            SkillEndorsement newEndorsement = endorsementService.addEndorsement(
                endorserId,
                request.endorsedUserId,
                request.skillId
            );
            return new ResponseEntity<>(newEndorsement, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SkillEndorsement>> getEndorsementsForUser(@PathVariable Long userId) {
        List<SkillEndorsement> endorsements = endorsementService.getEndorsementsForUser(userId);
        return ResponseEntity.ok(endorsements);
    }
}
