package com.group3.MockProject.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
 * DATE         AUTHOR     DESCRIPTION
 * -------------------------------------
 * 7/4/2025      User      Create
 * 7/10/2025     User      Update to match API spec exactly
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInterviewDto {
    @NotNull(message = "Start time is required")
    private Instant startTime;

    @NotNull(message = "End time is required")
    private Instant endTime;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Interviewer ID is required")
    private String interviewerId;        // This matches API spec field name exactly

    @NotBlank(message = "Interviewee type is required")
    @Pattern(regexp = "^(WITNESS|SUSPECT|VICTIM)$", message = "Interviewee type must be WITNESS, SUSPECT, or VICTIM")
    private String intervieweeType;      // SUSPECT, VICTIM, WITNESS

    @NotBlank(message = "Interviewee ID card is required")
    @Pattern(regexp = "^\\d{9,12}$", message = "Interviewee ID card must be 9-12 digits")
    private String intervieweeIdCard;    // This matches API spec field name exactly

    @Valid
    @NotNull(message = "Questions and answers are required")
    private List<QuestionDto> quesAndAns;
}
