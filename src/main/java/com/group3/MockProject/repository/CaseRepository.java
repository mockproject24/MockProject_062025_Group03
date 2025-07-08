package com.group3.MockProject.repository;

import com.group3.MockProject.entity.Case;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CaseRepository
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
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

    Page<Case> findByCaseIdContains(String search, Pageable pageable);
}
