package com.skillswap.controller;

import com.skillswap.dto.UserDTO;
import com.skillswap.model.User;
import com.skillswap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')") // Secures all endpoints in this controller
public class AdminController {

    @Autowired
    private UserService userService;

    /**
     * Endpoint to get a list of all users.
     * @return A list of all users.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Endpoint to toggle a user's active status (ban/unban).
     * @param userId The ID of the user to update.
     * @return The updated user object.
     */
    @PutMapping("/users/{userId}/toggle-status")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Long userId) {
        try {
            User updatedUser = userService.toggleUserActiveStatus(userId);
            return ResponseEntity.ok(new UserDTO(updatedUser)); // Return a clean DTO
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
