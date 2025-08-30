package com.skillswap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillswap.model.User;
import com.skillswap.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    
    // Add a mock bean for the PasswordEncoder
    // This is required because UserController now has a dependency on it
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    // Dummy user details for testing
    private User testUser;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        // Setup a mock authenticated user for each test
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setName("Test User");
        testUser.setBio("A bio about the test user.");
        testUser.setProfilePictureUrl("http://example.com/pic.jpg");

        // Create a mock UserDetails object for the security context with roles
        userDetails = new org.springframework.security.core.userdetails.User(
                testUser.getEmail(), testUser.getPassword(), Collections.emptyList());

        // Set the mock user in the SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        
        // Mock the passwordEncoder's matches method to always return true for testing
        // This is a crucial step to prevent the test from failing when it checks passwords
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
    }

    // Helper DTOs to match the request body in the controller
    static class ChangePasswordRequest {
        public String currentPassword;
        public String newPassword;

        public ChangePasswordRequest(String currentPassword, String newPassword) {
            this.currentPassword = currentPassword;
            this.newPassword = newPassword;
        }
    }

    static class DeleteUserRequest {
        public String password;
        public DeleteUserRequest(String password) {
            this.password = password;
        }
    }

    @Test
    void test_getProfile_shouldReturnAuthenticatedUser() throws Exception {
        // Mock the service layer call
        Mockito.when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);

        // Perform the GET request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/profile"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(testUser.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(testUser.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").doesNotExist());
    }

    @Test
    void test_getUserById_shouldReturnPublicProfile() throws Exception {
        // *** This is the updated mock for the new method signature ***
        Mockito.when(userService.getUserById(testUser.getId())).thenReturn(Optional.of(testUser));

        // Perform the GET request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + testUser.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testUser.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").doesNotExist());
    }

    // *** New test case for the not-found scenario handled by Optional ***
    @Test
    void test_getUserById_shouldReturnNotFound_ifUserDoesNotExist() throws Exception {
        // Mock the service call to return an empty Optional
        Mockito.when(userService.getUserById(999L)).thenReturn(Optional.empty());

        // Perform the GET request for a non-existent user and expect a 404 Not Found
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/999"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void test_updateProfile_shouldUpdateAndReturnUser() throws Exception {
        // Create a new User object with updated details
        User updatedDetails = new User();
        updatedDetails.setName("Updated Name");
        updatedDetails.setBio("New bio content.");
        updatedDetails.setProfilePictureUrl("http://example.com/newpic.jpg");

        // Create an expected User object after the update
        User expectedUpdatedUser = new User();
        expectedUpdatedUser.setId(1L);
        expectedUpdatedUser.setEmail(testUser.getEmail());
        expectedUpdatedUser.setName(updatedDetails.getName());
        expectedUpdatedUser.setBio(updatedDetails.getBio());
        expectedUpdatedUser.setProfilePictureUrl(updatedDetails.getProfilePictureUrl());
        expectedUpdatedUser.setPassword(testUser.getPassword());
        expectedUpdatedUser.setRoles(new HashSet<>());

        // Mock the service layer calls
        Mockito.when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);
        Mockito.when(userService.updateUserProfile(Mockito.any(Long.class), Mockito.any(User.class)))
                .thenReturn(expectedUpdatedUser);

        // Perform the PUT request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedDetails.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bio").value(updatedDetails.getBio()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").doesNotExist());
    }
    
    @Test
    void test_changePassword_shouldReturnOk_onSuccess() throws Exception {
        // Create a DTO with password change request
        ChangePasswordRequest request = new ChangePasswordRequest("oldpassword", "newpassword");
        
        // Mock service call to do nothing when called with the correct parameters
        Mockito.doNothing().when(userService).changePassword(testUser.getEmail(), request.currentPassword, request.newPassword);
        
        // Mock user details
        Mockito.when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);

        // Perform PUT request and assert success
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Password changed successfully."));
    }

    @Test
    void test_changePassword_shouldReturnBadRequest_onFailure() throws Exception {
        // Create a DTO with password change request
        ChangePasswordRequest request = new ChangePasswordRequest("wrongpassword", "newpassword");
        
        // Mock service to throw a RuntimeException for a failed password change
        Mockito.doThrow(new RuntimeException("Invalid password.")).when(userService)
                .changePassword(testUser.getEmail(), request.currentPassword, request.newPassword);
        
        // Mock user details
        Mockito.when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);

        // Perform PUT request and assert failure
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Invalid password."));
    }
    
    @Test
    void test_deleteUser_shouldReturnOk_onSuccess() throws Exception {
        // Create a DTO with deletion request
        DeleteUserRequest request = new DeleteUserRequest("password123");
        
        // Mock service call to find the user
        Mockito.when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);
        
        // Mock service call to delete the user
        Mockito.doNothing().when(userService).deleteUser(testUser.getId(), request.password);

        // Perform DELETE request and assert success
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User account deleted successfully."));
    }

    @Test
    void test_deleteUser_shouldReturnForbidden_ifDeletingOtherUser() throws Exception {
        // Create a DTO with deletion request
        DeleteUserRequest request = new DeleteUserRequest("password123");
        
        // Mock service call to find the authenticated user
        Mockito.when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);

        // Perform DELETE request on a different user's ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string("You can only delete your own account."));
    }
}
