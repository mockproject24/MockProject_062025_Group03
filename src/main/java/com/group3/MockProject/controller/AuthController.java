package com.group3.MockProject.controller;

import com.group3.MockProject.dto.request.LoginRequest;
import com.group3.MockProject.dto.request.RegisterRequest;
import com.group3.MockProject.dto.response.ApiResponse;
import com.group3.MockProject.dto.response.JwtResponse;
import com.group3.MockProject.dto.response.MessageResponse;
import com.group3.MockProject.entity.Role;
import com.group3.MockProject.entity.User;
import com.group3.MockProject.repository.RoleRepository;
import com.group3.MockProject.repository.UserRepository;
import com.group3.MockProject.security.UserDetailsImpl;
import com.group3.MockProject.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * AuthController - handles user authentication and registration
 * <p>
 * Provides REST endpoints for user authentication and registration operations.
 * This controller manages user login and registration without JWT authentication.
 * </p>
 *
 * @version 1.0
 * @since 2025-07-08
 * @author Group3
 * 
 * <p>
 * Copyright (c) 2025 Group3. All rights reserved.
 * </p>
 * 
 * <p>
 * Modification Log:
 * </p>
 * <table border="1">
 * <tr>
 * <th>DATE</th>
 * <th>AUTHOR</th>
 * <th>DESCRIPTION</th>
 * </tr>
 * <tr>
 * <td>08-07-2025</td>
 * <td>Group3</td>
 * <td>Create</td>
 * </tr>
 * </table>
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    /**
     * Authentication manager for user authentication
     */
    private final AuthenticationManager authenticationManager;
    
    /**
     * User repository for database operations
     */
    private final UserRepository userRepository;
    
    /**
     * Role repository for database operations  
     */
    private final RoleRepository roleRepository;
    
    /**
     * Password encoder for hashing passwords
     */
    private final PasswordEncoder encoder;
    
    /**
     * JWT utility for token operations
     */
    private final JwtUtil jwtUtil;

    /**
     * Authenticates a user with username and password
     * <p>
     * This endpoint validates user credentials and returns user information.
     * Note: This is a simplified version without JWT authentication.
     * </p>
     *
     * @param loginRequest the login request containing username and password
     * @return ResponseEntity containing user information or error message
     *
     * @throws Exception if authentication fails
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), 
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("");

            JwtResponse jwtResponse = new JwtResponse(
                    jwt,
                    userDetails.getUsername(),
                    null, // email removed from User entity
                    userDetails.getFullname(),
                    role);

            return ResponseEntity.ok(ApiResponse.success(jwtResponse));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("Authentication failed: " + e.getMessage()));
        }
    }

    /**
     * Registers a new user in the system
     * <p>
     * This endpoint creates a new user account with the provided information.
     * It validates that username and email are unique before creating the user.
     * </p>
     *
     * @param registerRequest the registration request containing user details
     * @return ResponseEntity containing success message or error details
     *
     * @throws Exception if registration fails
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<MessageResponse>> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Check if username already exists
            if (userRepository.existsByUsername(registerRequest.getUsername())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.badRequest("Username is already taken"));
            }

            // Create new user
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setPasswordHash(encoder.encode(registerRequest.getPassword()));
            user.setFullname(registerRequest.getFullname());
            user.setPhoneNumber(registerRequest.getPhoneNumber());
            user.setCreateAt(LocalDateTime.now());
            user.setDeleted(false);

            // Set default role if not provided
            final String finalRoleId = (registerRequest.getRoleId() == null || registerRequest.getRoleId().isEmpty()) 
                    ? "USER" 
                    : registerRequest.getRoleId();

            Role role = roleRepository.findByRoleId(finalRoleId)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + finalRoleId));
            user.setRole(role);

            // Save user
            userRepository.save(user);

            MessageResponse response = new MessageResponse("User registered successfully!");
            return ResponseEntity.ok(ApiResponse.success(response));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalServerError("Registration failed: " + e.getMessage()));
        }
    }
}