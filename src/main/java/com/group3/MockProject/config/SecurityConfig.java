package com.group3.MockProject.config;

import com.group3.MockProject.security.JwtAuthenticationEntryPoint;
import com.group3.MockProject.security.JwtAuthenticationFilter;
import com.group3.MockProject.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * User details service for authentication
     */
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * JWT authentication entry point for handling unauthorized access
     */
    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    /**
     * JWT authentication filter for token validation
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Creates the authentication manager bean
     * <p>
     * This bean is used for authenticating users in the application.
     * </p>
     *
     * @param authConfig the authentication configuration
     * @return AuthenticationManager the authentication manager instance
     * @throws Exception if configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Creates the password encoder bean
     * <p>
     * Uses BCrypt for password hashing which is a secure one-way hashing algorithm.
     * </p>
     *
     * @return PasswordEncoder the BCrypt password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates the DAO authentication provider
     * <p>
     * Configures the authentication provider with user details service and password encoder.
     * This provider is used to authenticate users against the database.
     * </p>
     *
     * @return DaoAuthenticationProvider the configured authentication provider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configures the security filter chain
     * <p>
     * Sets up security rules including CORS, CSRF, session management,
     * and endpoint authorization. Allows public access to auth endpoints
     * and requires authentication for all other endpoints.
     * </p>
     *
     * @param http the HttpSecurity object to configure
     * @return SecurityFilterChain the configured security filter chain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF as we use JWT tokens
                .csrf(AbstractHttpConfigurer::disable)
                // Configure exception handling
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                // Set session management to stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configure authorization rules
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll() // Allow public access to auth endpoints
                                .requestMatchers("/api/cases/**").permitAll() // Allow public access to cases for now
                                .requestMatchers("/api/evidence/**").permitAll() // Allow public access to evidence for now
                                .requestMatchers("/api/interviews/**").permitAll() // Allow public access to interviews for now
                                .requestMatchers("/api/investigation-plans/**").permitAll() // Allow public access to investigation plans for now
                                .requestMatchers("/**").permitAll()
                                //.anyRequest().authenticated() // Require authentication for other endpoints
                );

        // Set authentication provider
        http.authenticationProvider(authenticationProvider());
        
        // Add JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
} 