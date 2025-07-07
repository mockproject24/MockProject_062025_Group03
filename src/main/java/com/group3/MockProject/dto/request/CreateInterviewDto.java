package com.group3.MockProject.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String interviewer;
    private String intervieweeType;
    private String interviewee;
    private List<QuestionDto> quesAndAns;
}
