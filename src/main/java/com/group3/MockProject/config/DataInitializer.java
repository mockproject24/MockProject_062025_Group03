package com.group3.MockProject.config;

import com.group3.MockProject.entity.Role;
import com.group3.MockProject.entity.User;
import com.group3.MockProject.repository.RoleRepository;
import com.group3.MockProject.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

/**
 * DataInitializer
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 7/10/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/10/2025      User      Create
 */
@Configuration
public class DataInitializer {
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Bean
    @Transactional
    CommandLineRunner commandLineRunner() {
        return args -> {
            initDefaultRoles();
            log.info("Application Still running at: ");
            log.info("localhost:8081/api");
        };
    }

    private void initDefaultRoles() {
        // Generate dummy data
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("ADMIN", "System Administrator", false, null, null));
            roleRepository.save(new Role("OFFICER", "Investigation Officer", false, null, null));
            roleRepository.save(new Role("USER", "Regular User", false, null, null));
            log.info("Role data initialized!");
        } else {
            log.info("Role data already initialized!");
        }
    }
}
