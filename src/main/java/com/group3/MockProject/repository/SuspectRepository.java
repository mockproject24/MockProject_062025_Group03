package com.group3.MockProject.repository;

import com.group3.MockProject.entity.Suspect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * SuspectRepository
 *
 * Repository for Suspect entity operations.
 * Provides data access methods for suspect management and filtering.
 *
 * Version 1.0
 *
 * Date: 08-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE                 AUTHOR          DESCRIPTION
 * -----------------------------------------------------------------------
 * 08-07-2025         Group3            Create
 * 12-07-2025         FongFox           Update
 */
@Repository
public interface SuspectRepository extends JpaRepository<Suspect, String> {
    /**
     * Finds suspects by case ID with optional status and date filtering
     * @param caseId The case identifier
     * @param status The suspect status filter (optional)
     * @param date The date filter (not used in query but kept for compatibility)
     * @param startOfDay Start of day filter for catch time
     * @param endOfDay End of day filter for catch time
     * @param pageable Pagination information
     * @return Page of suspects matching the criteria
     */
    @Query("SELECT s FROM Suspect s WHERE " +
           "(:caseId IS NULL OR s.caseEntity.caseId = :caseId) AND " +
           "(:status IS NULL OR s.status = :status) AND " +
           "(:startOfDay IS NULL OR s.uploadedAt >= :startOfDay) AND " +
           "(:endOfDay IS NULL OR s.uploadedAt <= :endOfDay)" +
            "AND s.isDeleted = false ")
    Page<Suspect> findByCaseIdAndStatusAndCatchTime(
            @Param("caseId") String caseId,
            @Param("status") String status,
            @Param("date") LocalDate date,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            Pageable pageable);

    /**
     * Finds suspect by ID card number
     * Used for interview participant verification
     */
    Optional<Suspect> findBySuspectIdCard(Long suspectIdCard);
}