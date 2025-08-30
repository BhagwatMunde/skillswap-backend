package com.skillswap.controller;

import com.fasterxml.jackson.annotation.JsonFormat; // Import the annotation
import com.skillswap.dto.SessionDTO;
import com.skillswap.model.Session;
import com.skillswap.model.User;
import com.skillswap.service.SessionService;
import com.skillswap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    // DTOs for request bodies
    static class ScheduleRequest {
        public Long skillRequestId;
        
        // --- FIX: This annotation tells the server how to read the date format ---
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
        public LocalDateTime scheduledTime;
    }

    static class UpdateStatusRequest {
        public String status; // e.g., "COMPLETED" or "CANCELLED"
    }

    private Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();
        User user = userService.findByEmail(username);
        return user.getId();
    }

    @PostMapping
    public ResponseEntity<?> scheduleSession(@RequestBody ScheduleRequest request) {
        try {
            SessionDTO newSession = sessionService.scheduleSession(request.skillRequestId, request.scheduledTime);
            return new ResponseEntity<>(newSession, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{sessionId}/status")
    public ResponseEntity<?> updateSessionStatus(@PathVariable Long sessionId, @RequestBody UpdateStatusRequest request) {
        try {
            Session.SessionStatus newStatus = Session.SessionStatus.valueOf(request.status.toUpperCase());
            SessionDTO updatedSession = sessionService.updateSessionStatus(sessionId, newStatus);
            return ResponseEntity.ok(updatedSession);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid status value.", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/my-sessions")
    public ResponseEntity<List<SessionDTO>> getUserSessions() {
        Long userId = getAuthenticatedUserId();
        List<SessionDTO> sessions = sessionService.getUserSessions(userId);
        return ResponseEntity.ok(sessions);
    }
}
