package com.group3.MockProject.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {
    
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    /**
     * JWT secret key from application properties
     */
    @Value("${app.jwtSecret:mockProjectSecretKey}")
    private String jwtSecret;

    /**
     * JWT expiration time in milliseconds from application properties
     */
    @Value("${app.jwtExpirationMs:86400000}")
    private int jwtExpirationMs;

    /**
     * Generates a JWT token from authentication object
     * <p>
     * Creates a JWT token containing the username and expiration time.
     * The token is signed with the application's secret key.
     * </p>
     *
     * @param authentication the authentication object containing user details
     * @return String the generated JWT token
     */
    public String generateJwtToken(Authentication authentication) {
        String username = authentication.getName();
        
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(Instant.now().toEpochMilli() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Generates a JWT token from username
     * <p>
     * Creates a JWT token for a specific username with default expiration time.
     * </p>
     *
     * @param username the username to create token for
     * @return String the generated JWT token
     */
    public String generateTokenFromUsername(String username) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(Instant.now().toEpochMilli() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extracts username from JWT token
     * <p>
     * Parses the JWT token and extracts the username from the subject claim.
     * </p>
     *
     * @param token the JWT token to parse
     * @return String the username from the token
     */
    public String getUsernameFromJwtToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates a JWT token
     * <p>
     * Checks if the token is valid by verifying its signature and expiration.
     * Logs any validation errors for debugging purposes.
     * </p>
     *
     * @param authToken the JWT token to validate
     * @return boolean true if token is valid, false otherwise
     */
    public boolean validateJwtToken(String authToken) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            
            Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(authToken);
            
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
} 