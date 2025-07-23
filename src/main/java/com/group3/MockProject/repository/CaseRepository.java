package com.group3.MockProject.repository;

import com.group3.MockProject.constant.CaseSeverity;
import com.group3.MockProject.constant.CaseType;
import com.group3.MockProject.entity.Case;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
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

    Page<Case> findByCaseNameContains(String search, Pageable pageable);

    Page<Case> findByIsDeletedFalse(Pageable pageable);

    Page<Case> findByCaseNameContainingIgnoreCaseAndIsDeletedFalse(String caseName, Pageable pageable);

    @Query("SELECT c FROM Case c WHERE c.isDeleted = false AND " +
            "(:search IS NULL OR LOWER(c.caseName) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:severityType IS NULL OR c.severity = :severityType) AND " +
            "(:caseType IS NULL OR c.typeCase = :caseType)")
    Page<Case> findCasesWithFilters(@Param("search") String search,
                                    @Param("severityType") CaseSeverity severityType,
                                    @Param("caseType") CaseType caseType,
                                    Pageable pageable);

}
