package com.skillswap.controller;

import com.skillswap.dto.SkillRequestDTO;
import com.skillswap.model.SkillRequest;
import com.skillswap.model.User;
import com.skillswap.service.SkillRequestService;
import com.skillswap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skill-requests")
public class SkillRequestController {

    @Autowired
    private SkillRequestService skillRequestService;

    @Autowired
    private UserService userService;

    // DTOs for request bodies
    static class CreateRequest {
        public Long receiverId;
        public Long skillId;
        public String message;
    }

    static class UpdateStatusRequest {
        public String status; // e.g., "ACCEPTED" or "REJECTED"
    }

    private Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();
        User user = userService.findByEmail(username);
        return user.getId();
    }

    @PostMapping
    public ResponseEntity<?> createSkillRequest(@RequestBody CreateRequest request) {
        try {
            Long requesterId = getAuthenticatedUserId();
            SkillRequestDTO newRequest = skillRequestService.createRequest(
                requesterId,
                request.receiverId,
                request.skillId,
                request.message
            );
            return new ResponseEntity<>(newRequest, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{requestId}/status")
    public ResponseEntity<?> updateRequestStatus(@PathVariable Long requestId, @RequestBody UpdateStatusRequest request) {
        try {
            SkillRequest.RequestStatus newStatus = SkillRequest.RequestStatus.valueOf(request.status.toUpperCase());
            SkillRequestDTO updatedRequest = skillRequestService.updateRequestStatus(requestId, newStatus);
            return ResponseEntity.ok(updatedRequest);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid status value.", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/sent")
    public ResponseEntity<List<SkillRequestDTO>> getSentRequests() {
        Long userId = getAuthenticatedUserId();
        return ResponseEntity.ok(skillRequestService.getSentRequests(userId));
    }

    @GetMapping("/received")
    public ResponseEntity<List<SkillRequestDTO>> getReceivedRequests() {
        Long userId = getAuthenticatedUserId();
        return ResponseEntity.ok(skillRequestService.getReceivedRequests(userId));
    }
}
