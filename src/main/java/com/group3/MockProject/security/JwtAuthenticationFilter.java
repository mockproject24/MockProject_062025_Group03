package com.group3.MockProject.security;

import com.group3.MockProject.service.impl.UserDetailsServiceImpl;
import com.group3.MockProject.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter - JWT authentication filter
 * <p>
 * Processes JWT tokens from HTTP requests and sets up Spring Security authentication context.
 * This filter runs once per request to validate JWT tokens and authenticate users.
 * It extracts the JWT from the Authorization header, validates it, and sets the security context.
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
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * JWT utility for token operations
     */
    private final JwtUtil jwtUtil;

    /**
     * User details service for loading user information
     */
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    /**
     * Processes JWT authentication for each request
     * <p>
     * This method is called once per request to:
     * 1. Extract JWT token from Authorization header
     * 2. Validate the token
     * 3. Load user details from database
     * 4. Set authentication context for Spring Security
     * </p>
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain to continue processing
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            
            if (jwt != null && jwtUtil.validateJwtToken(jwt)) {
                String username = jwtUtil.getUsernameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts JWT token from request Authorization header
     * <p>
     * Looks for Bearer token in the Authorization header and extracts
     * the JWT token part by removing the "Bearer " prefix.
     * </p>
     *
     * @param request the HTTP request to extract token from
     * @return String the JWT token or null if not found
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
} 