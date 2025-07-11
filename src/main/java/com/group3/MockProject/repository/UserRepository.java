package com.group3.MockProject.repository;

import com.group3.MockProject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * UserRepository
 *
 * Provides business logic for managing employment details.
 *
 * Version 1.0
 * Date: 7/4/2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/4/2025      NGUYEN NGOC SY      Create
 */

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);


    Optional<User> findByFullName(String fullName);
    
    Optional<User> findByUsernameAndIsDeletedFalse(String username);
    
    Boolean existsByUsername(String username);

    @Query("SELECT u FROM User u JOIN u.role r JOIN u.usersCases uc JOIN uc.caseEntity c WHERE r.roleId = 'OFFICER' AND c.caseId = :caseId AND uc.isDeleted = false")
    Page<User> findOfficersByCaseId(@Param("caseId") String caseId, Pageable pageable);
}
