package com.group3.MockProject.repository;

import com.group3.MockProject.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * InterviewRepository
 * <p>
 * Repository for Interview entity operations.
 * Provides data access methods for interview management including conflict detection.
 * <p>
 * Version 1.0
 * Date: 7/4/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 4/7/2025      FongFox      Create
 * 12/7/2025     FongFox      Update
 */
@Repository
public interface InterviewRepository extends JpaRepository<Interview, String> {
    /**
     * Finds interviews with time conflicts for a specific interviewer
     * Uses time overlap logic: NOT (end1 <= start2 OR start1 >= end2)
     *
     * @param interviewerId ID of the interviewer
     * @param startTime New interview start time
     * @param endTime New interview end time
     * @return List of conflicting interviews
     */
    @Query("SELECT i FROM Interview i WHERE i.userInterviewer.username = :interviewerId " +
            "AND i.isDeleted = false " +
            "AND NOT (i.endTime <= :startTime OR i.startTime >= :endTime)")
    List<Interview> findConflictingInterviews(
            @Param("interviewerId") String interviewerId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

}
