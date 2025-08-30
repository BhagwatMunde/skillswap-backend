package com.skillswap.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object for user registration requests.
 * This class is used to receive data from the client and apply validation.
 */
@Data // Lombok annotation to automatically generate getters, setters, toString, etc.
public class RegisterRequest {

    // The `@NotBlank` annotation ensures that the username field is not null or empty.
    // The `@Size` annotation ensures the length is within the specified range.
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters long")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}
