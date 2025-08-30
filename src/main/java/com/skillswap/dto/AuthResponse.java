package com.skillswap.dto;

import java.util.Set;

// Updated to handle a set of roles, a user ID, and a JWT token
public class AuthResponse {
    private String jwt;
    private Long userId;
    private Set<String> roles;

    public AuthResponse(String jwt, Long userId, Set<String> roles) {
        this.jwt = jwt;
        this.userId = userId;
        this.roles = roles;
    }

    public String getJwt() { return jwt; }
    public Long getUserId() { return userId; }
    public Set<String> getRoles() { return roles; }
}
