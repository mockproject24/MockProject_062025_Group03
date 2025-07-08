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

/**
 * SuspectRepository
 *
 * Provides data access layer for Suspect entity operations.
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
           "(:startOfDay IS NULL OR s.catchTime >= :startOfDay) AND " +
           "(:endOfDay IS NULL OR s.catchTime <= :endOfDay)")
    Page<Suspect> findByCaseIdAndStatusAndCatchTime(
            @Param("caseId") String caseId, 
            @Param("status") String status, 
            @Param("date") LocalDate date, 
            @Param("startOfDay") LocalDateTime startOfDay, 
            @Param("endOfDay") LocalDateTime endOfDay, 
            Pageable pageable);
} 