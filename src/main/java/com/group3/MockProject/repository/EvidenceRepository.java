package com.group3.MockProject.repository;

import com.group3.MockProject.entity.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * EvidenceRepository
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
public interface EvidenceRepository extends JpaRepository<Evidence, String> {
    Optional<Evidence> findByCaseEntity_CaseIdAndEvidenceId(String caseId, String evidenceId);

    /**
     * Finds all active evidences by case ID
     *
     * @param caseId The case identifier
     * @return List of evidences for the case
     */
    @Query("SELECT e FROM Evidence e " +
            "LEFT JOIN FETCH e.digitalInvest " +
            "LEFT JOIN FETCH e.financialInvest " +
            "LEFT JOIN FETCH e.forensicInvest " +
            "LEFT JOIN FETCH e.physicalInvest " +
            "WHERE e.caseEntity.caseId = :caseId AND e.isDeleted = false")
    List<Evidence> findByCaseEntityCaseId(@Param("caseId") String caseId);
}
