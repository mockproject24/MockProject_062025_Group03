package com.group3.MockProject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * QuestionDto
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
 * 7/10/2025     User      Add validation annotations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {
    @NotBlank(message = "Question content is required")
    private String question;      // Question content

    @NotBlank(message = "Answer is required")
    private String answer;        // Answer content

    @NotBlank(message = "Level of trust is required")
    @Pattern(regexp = "^[aAbBcC]$", message = "Level of trust must be 'a', 'b', or 'c'")
    private String levelOfTrust;  // Trust level: "a", "b", "c"
}
