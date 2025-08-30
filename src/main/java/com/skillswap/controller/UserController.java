package com.skillswap.controller;

import com.skillswap.model.User;
import com.skillswap.service.UserService;
import com.skillswap.dto.UserDTO;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.skillswap.dto.RegisterRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // DTO for password change request
    static class ChangePasswordRequest {
        private String currentPassword;
        private String newPassword;

        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

    // DTO for user deletion confirmation
    static class DeleteUserRequest {
        private String password;

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String userEmail;
        if (principal instanceof UserDetails) {
            userEmail = ((UserDetails) principal).getUsername();
        } else {
            userEmail = (String) principal;
        }

        User user = userService.findByEmail(userEmail);
        user.setPassword(null); // Clear password before sending to the client
        return ResponseEntity.ok(user);
    }

    // New: Endpoint to get a specific user's public profile
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        // We now call the new getUserById method from the service, which returns an Optional.
        Optional<User> userOptional = userService.getUserById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Remove sensitive information before sending the response
            user.setPassword(null);
            user.setEmail(null);
            user.setRoles(new HashSet<>());
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // New: Endpoint to update user profile
    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestBody User userDetails) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String userEmail;
        if (principal instanceof UserDetails) {
            userEmail = ((UserDetails) principal).getUsername();
        } else {
            userEmail = (String) principal;
        }

        User authenticatedUser = userService.findByEmail(userEmail);
        Long userId = authenticatedUser.getId();

        User updatedUser = userService.updateUserProfile(userId, userDetails);
        updatedUser.setPassword(null);
        return ResponseEntity.ok(updatedUser);
    }

    // New: Endpoint to change user's password
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String userEmail;
        if (principal instanceof UserDetails) {
            userEmail = ((UserDetails) principal).getUsername();
        } else {
            userEmail = (String) principal;
        }
        
        try {
            userService.changePassword(userEmail, request.getCurrentPassword(), request.getNewPassword());
            return ResponseEntity.ok("Password changed successfully.");
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // New: Endpoint to delete user account
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, @RequestBody DeleteUserRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail;
        if (principal instanceof UserDetails) {
            userEmail = ((UserDetails) principal).getUsername();
        } else {
            userEmail = (String) principal;
        }

        User authenticatedUser = userService.findByEmail(userEmail);

        if (!authenticatedUser.getId().equals(userId)) {
            return new ResponseEntity<>("You can only delete your own account.", HttpStatus.FORBIDDEN);
        }

        try {
            userService.deleteUser(authenticatedUser.getId(), request.getPassword());
            return ResponseEntity.ok("User account deleted successfully.");
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    
    /**
     * Endpoint to find users who can teach a specific skill.
     * @param skillId The ID of the skill.
     * @return A list of users.
     */
    @GetMapping("/teaches/{skillId}")
    public ResponseEntity<List<UserDTO>> findUsersBySkill(@PathVariable Long skillId) {
        List<UserDTO> users = userService.findUsersWhoCanTeach(skillId);
        return ResponseEntity.ok(users);
    }

    /**
     * Endpoint to find users who want to learn a specific skill.
     * @param skillId The ID of the skill.
     * @return A list of users.
     */
    @GetMapping("/learns/{skillId}")
    public ResponseEntity<List<UserDTO>> findLearnersBySkill(@PathVariable Long skillId) {
        List<UserDTO> users = userService.findUsersWhoWantToLearn(skillId);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Handles user registration.
     * This method is mapped to handle HTTP POST requests to "/api/users/register".
     * It consumes a JSON body, registers the new user, and returns a success response.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        // We create a new User entity from the RegisterRequest DTO.
        User newUser = new User();
        
        // FIX: The User class you provided has a `name` field, not `username`.
        // We must use the `setName` method to set the user's name from the DTO.
        newUser.setName(request.getUsername());

        // The rest of the DTO fields are mapped as before.
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword()); 
        
        // Now, we can pass the correctly populated User object to the service.
        userService.registerNewUser(newUser);

        return ResponseEntity.ok("User registered successfully!");
    }
}
