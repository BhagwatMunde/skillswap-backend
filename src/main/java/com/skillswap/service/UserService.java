package com.skillswap.service;

import com.skillswap.model.Role;
import com.skillswap.model.User;
import com.skillswap.repository.RoleRepository;
import com.skillswap.repository.UserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillswap.dto.UserDTO;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Finds all users who can teach a specific skill.
     * @param skillId The ID of the skill.
     * @return A list of simplified UserDTO objects.
     */
    public List<UserDTO> findUsersWhoCanTeach(Long skillId) {
        return userRepository.findUsersByOfferingSkill(skillId)
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Finds all users who want to learn a specific skill.
     * @param skillId The ID of the skill.
     * @return A list of simplified UserDTO objects.
     */
    public List<UserDTO> findUsersWhoWantToLearn(Long skillId) {
        return userRepository.findUsersByLearningSkill(skillId)
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }
    

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    // New: Method to get a user by ID, returning an Optional to handle not found cases.
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    // New: Method to save a user to the database. This is used to persist changes
    // after adding or removing a skill.
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
    public User registerNewUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email address already in use.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email);

        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public User updateUserProfile(Long userId, User userDetails) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        existingUser.setName(userDetails.getName());
        existingUser.setBio(userDetails.getBio());
        existingUser.setProfilePictureUrl(userDetails.getProfilePictureUrl());

        return userRepository.save(existingUser);
    }

    public void changePassword(String email, String currentPassword, String newPassword) {
        User user = findByEmail(email);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Incorrect current password.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deleteUser(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Incorrect password for account deletion.");
        }

        userRepository.delete(user);
    }
    
    /**
     * --- NEW (Admin Function) ---
     * Fetches a list of all users in the system.
     * @return A list of all users as DTOs.
     */
    public List<UserDTO> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * --- NEW (Admin Function) ---
     * Toggles the active status of a user's account (ban/unban).
     * @param userId The ID of the user to update.
     * @return The updated User object.
     */
    @Transactional
    public User toggleUserActiveStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        // Invert the current active status
        user.setActive(!user.isActive()); 
        
        return userRepository.save(user);
    }
}
