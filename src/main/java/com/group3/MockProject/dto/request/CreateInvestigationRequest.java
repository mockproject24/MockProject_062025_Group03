package com.group3.MockProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CreateInvestigationRequest
 *
 * DTO for creating investigation with type and analysis details
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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvestigationRequest {
    
    /**
     * Type of investigation
     */
    private String type;
    
    /**
     * Analysis result or description
     */
    private String analysist;
} 