package com.group3.MockProject.repository;

import com.group3.MockProject.entity.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CaseRepository
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 7/4/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/4/2025      NGUYEN NGOC SY      Create
 */


import com.group3.MockProject.entity.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseRepository extends JpaRepository<Case, String> {
    @Query("SELECT c FROM Case c WHERE c.isDeleted = false")
    List<Case> findAllActiveCases();
}
