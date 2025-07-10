package com.group3.MockProject.dto.request;

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
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private String question;      // Question content
    private String answer;        // Answer content
    private String levelOfTrust;  // Trust level: "a", "b", "c"
}
