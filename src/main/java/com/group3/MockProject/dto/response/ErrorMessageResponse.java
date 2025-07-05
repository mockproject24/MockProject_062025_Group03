package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * ErrorMessageResponse
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 7/5/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/5/2025      User      Create
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessageResponse {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String details;
}
