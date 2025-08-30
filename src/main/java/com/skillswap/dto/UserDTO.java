package com.skillswap.dto;

import com.skillswap.model.User;
import lombok.Data;

// A simplified DTO for user information
@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String bio;
    private String profilePictureUrl;
    private boolean isActive; // --- NEW FIELD ---

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.bio = user.getBio();
        this.profilePictureUrl = user.getProfilePictureUrl();
        this.isActive = user.isActive(); // --- NEW MAPPING ---
    }
}
