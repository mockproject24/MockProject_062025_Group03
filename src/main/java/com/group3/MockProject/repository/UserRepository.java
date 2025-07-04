package com.group3.MockProject.repository;

import com.group3.MockProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    
    Optional<User> findByUsernameAndIsDeletedFalse(String username);
    
    Boolean existsByUsername(String username);
    
    Boolean existsByEmail(String email);
} 