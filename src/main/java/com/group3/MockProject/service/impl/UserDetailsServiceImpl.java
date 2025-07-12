package com.group3.MockProject.service.impl;

import com.group3.MockProject.entity.User;
import com.group3.MockProject.repository.UserRepository;
import com.group3.MockProject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserDetailsServiceImpl
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 04/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 04/07/2025   Hải Đăng      Create
 * 7/12/2025      Ngoc Nghia      update
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * User repository for database operations
     */
    private final UserRepository userRepository;

    /**
     * Loads user details by username for authentication
     * <p>
     * This method is called by Spring Security during authentication to load
     * user details from the database. It finds the user by username and
     * converts it to UserDetailsImpl for security framework usage.
     * </p>
     *
     * @param username the username to search for
     * @return UserDetails the user details for authentication
     * @throws UsernameNotFoundException if user is not found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return UserDetailsImpl.build(user);
    }
} 