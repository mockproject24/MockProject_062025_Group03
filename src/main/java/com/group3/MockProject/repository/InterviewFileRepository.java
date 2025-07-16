package com.group3.MockProject.repository;

import com.group3.MockProject.entity.InterviewFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * InterviewFileRepository
 * <p>
 * Repository for InterviewFile entity operations
 * <p>
 * Version 1.0
 * Date: 7/10/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/10/2025      User      Create
 */
@Repository
public interface InterviewFileRepository extends JpaRepository<InterviewFile, String> {
    /**
     * Find all interview files by interview ID
     * @param interviewId Interview ID
     * @return List of interview files
     */
    List<InterviewFile> findByInterviewInterviewIdAndIsDeletedFalse(String interviewId);
}
