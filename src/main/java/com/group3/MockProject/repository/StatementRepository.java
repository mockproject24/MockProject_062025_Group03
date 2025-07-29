package com.group3.MockProject.repository;

import com.group3.MockProject.entity.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * StatementRepository
 * <p>
 * Repository for Statement entity operations.
 * Provides data access methods for statement management.
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
public interface StatementRepository extends JpaRepository<Statement, String> {
    /**
     * Finds statement by case ID and statement ID (not deleted)
     * Used for the GET API endpoint
     *
     * @param caseId The case identifier
     * @param statementId The statement identifier
     * @return Optional statement matching the criteria
     */
    @Query("SELECT s FROM Statement s WHERE s.caseEntity.caseId = :caseId " +
            "AND s.statementId = :statementId AND s.isDeleted = false")
    Optional<Statement> findByCaseIdAndStatementIdAndNotDeleted(
            @Param("caseId") String caseId,
            @Param("statementId") String statementId
    );

    /**
     * Finds statement by case ID and statement ID with evidence links loaded
     * Uses JOIN FETCH to avoid N+1 query problem
     *
     * @param caseId The case identifier
     * @param statementId The statement identifier
     * @return Optional statement with evidence links loaded
     */
    @Query("SELECT s FROM Statement s " +
            "LEFT JOIN FETCH s.statementEvidences se " +
            "LEFT JOIN FETCH se.evidence " +
            "WHERE s.caseEntity.caseId = :caseId " +
            "AND s.statementId = :statementId AND s.isDeleted = false")
    Optional<Statement> findByCaseIdAndStatementIdWithEvidences(
            @Param("caseId") String caseId,
            @Param("statementId") String statementId
    );

    /**
     * Checks if statement exists by case ID and statement ID
     *
     * @param caseId The case identifier
     * @param statementId The statement identifier
     * @return true if statement exists and not deleted, false otherwise
     */
    @Query("SELECT COUNT(s) > 0 FROM Statement s WHERE s.caseEntity.caseId = :caseId " +
            "AND s.statementId = :statementId AND s.isDeleted = false")
    boolean existsByCaseIdAndStatementId(
            @Param("caseId") String caseId,
            @Param("statementId") String statementId
    );
}
