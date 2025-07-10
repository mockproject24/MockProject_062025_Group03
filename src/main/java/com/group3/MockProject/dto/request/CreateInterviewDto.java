package com.group3.MockProject.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * CreateInterviewDto
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
 * 7/4/2025      User      Create
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInterviewDto {
    private Instant startTime;
    private Instant endTime;
    private String location;
    private String interviewer;        // Interviewer ID
    private String intervieweeType;    // SUSPECT, VICTIM, WITNESS
    private String intervieweeIdCard;  // Interviewee ID card number
    private String intervieweeName;    // Interviewee name
    private List<QuestionDto> quesAndAns;
}
