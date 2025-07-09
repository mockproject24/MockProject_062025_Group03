package com.group3.MockProject.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JwtAuthenticationEntryPoint - JWT authentication entry point
 * <p>
 * Handles authentication failures and unauthorized access attempts.
 * This class is invoked when a user tries to access a secured REST resource
 * without providing valid credentials or with an invalid/expired JWT token.
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
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    /**
     * Handles authentication failures
     * <p>
     * This method is triggered whenever an unauthenticated user requests a secured HTTP resource
     * and an AuthenticationException is thrown. It returns an HTTP 401 Unauthorized error
     * with an appropriate error message.
     * </p>
     *
     * @param request the HTTP request that caused the authentication failure
     * @param response the HTTP response to modify
     * @param authException the authentication exception that was thrown
     * @throws IOException if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        
        logger.error("Unauthorized error: {}", authException.getMessage());
        
        // Set response content type to JSON
        response.setContentType("application/json");
        
        // Set HTTP status to 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        // Create JSON error response
        String jsonResponse = String.format(
            "{\"code\": 401, \"message\": \"Unauthorized: %s\", \"result\": null}",
            authException.getMessage()
        );
        
        // Write the response
        response.getWriter().write(jsonResponse);
    }
} 