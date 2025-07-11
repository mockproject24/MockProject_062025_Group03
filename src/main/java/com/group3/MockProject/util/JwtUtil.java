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

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${app.jwtSecret:mockProjectSecretKey}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs:86400000}")
    private int jwtExpirationMs;

    /**
     * ✅ Tạo SecretKey đảm bảo đủ dài cho HS512 (64 bytes = 512 bits)
     */
    private SecretKey getSigningKey() {
        String paddedSecret = jwtSecret;
        // Đảm bảo key đủ dài cho HS512 (cần ít nhất 64 bytes)
        while (paddedSecret.length() < 64) {
            paddedSecret += "PADDING_FOR_SECURITY";
        }
        return Keys.hmacShaKeyFor(paddedSecret.getBytes());
    }

    /**
     * Generates a JWT token from authentication object
     */
    public String generateJwtToken(Authentication authentication) {
        String username = authentication.getName();

        return Jwts.builder()
                .subject(username) // ✅ Dùng subject() thay vì setSubject()
                .issuedAt(new Date()) // ✅ Dùng issuedAt() thay vì setIssuedAt()
                .expiration(new Date(Instant.now().toEpochMilli() + jwtExpirationMs)) // ✅ Dùng expiration()
                .signWith(getSigningKey()) // ✅ Tự động detect HS512
                .compact();
    }

    /**
     * Generates a JWT token from username
     */
    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(Instant.now().toEpochMilli() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extracts username from JWT token
     */
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser() // ✅ Có thể dùng parser() hoặc parserBuilder()
                .verifyWith(getSigningKey()) // ✅ Dùng verifyWith() thay vì setSigningKey()
                .build()
                .parseSignedClaims(token) // ✅ Dùng parseSignedClaims() thay vì parseClaimsJws()
                .getPayload()
                .getSubject();
    }

    /**
     * Validates a JWT token
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey()) // ✅ Dùng verifyWith()
                    .build()
                    .parseSignedClaims(authToken); // ✅ Dùng parseSignedClaims()

            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (JwtException e) {
            logger.error("JWT validation failed: {}", e.getMessage());
        }

        return false;
    }
}
