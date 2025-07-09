package com.group3.MockProject.repository;

import com.group3.MockProject.entity.Warrant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * WarrantRepository
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

@Repository
public interface WarrantRepository extends JpaRepository<Warrant, String> {

    /**
     * Finds all active warrants by case ID
     * @param caseId The case identifier
     * @return List of warrants for the case
     */
    @Query("SELECT w FROM Warrant w WHERE w.caseEntity.caseId = :caseId AND w.isDeleted = false")
    List<Warrant> findByCaseEntityCaseId(@Param("caseId") String caseId);
}