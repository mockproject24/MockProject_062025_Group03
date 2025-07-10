package com.group3.MockProject.dto.response;

/**
 * InterviewResponseDto
 * <p>
 * Provides business logic for managing employment details.
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * InterviewResponseDto
 * DTO return interview had been created
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewResponseDto {
    private String interviewId;        // Generated interview ID
    private String caseId;             // Case ID from URL
    private String suspectId;          // Suspect ID from URL
    private LocalDateTime startTime;   // Interview start time
    private LocalDateTime endTime;     // Interview end time
    private String location;           // Interview location
    private String interviewerName;    // Interviewer full name
    private String intervieweeType;    // Type of interviewee
    private String intervieweeName;    // Interviewee full name
    private Integer totalQuestions;    // Total number of questions
    private List<String> attachedFiles; // List of uploaded file names
    private LocalDateTime createdAt;   // Interview creation timestamp
}
