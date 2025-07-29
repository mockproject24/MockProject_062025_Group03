package com.group3.MockProject.repository;

import com.group3.MockProject.entity.StatementEvidence;
import com.group3.MockProject.entity.StatementEvidenceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * StatementEvidenceRepository
 * <p>
 * Repository for StatementEvidence entity operations.
 * Handles many-to-many relationship between Statement and Evidence.
 * <p>
 * Version 1.0
 * Date: 7/23/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/23/2025      FongFox      Create
 */
@Repository
public interface StatementEvidenceRepository extends JpaRepository<StatementEvidence, StatementEvidenceId> {
    /**
     * Finds all evidence links for a specific statement (not deleted)
     *
     * @param statementId The statement identifier
     * @return List of statement evidence links
     */
    @Query("SELECT se FROM StatementEvidence se " +
            "JOIN FETCH se.evidence e " +
            "WHERE se.statement.statementId = :statementId " +
            "AND se.isDeleted = false AND e.isDeleted = false")
    List<StatementEvidence> findByStatementIdAndNotDeleted(@Param("statementId") String statementId);
}
