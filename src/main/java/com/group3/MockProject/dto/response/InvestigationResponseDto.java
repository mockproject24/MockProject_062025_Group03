package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * InvestigationResponseDto
 *
 * DTO for investigation creation response
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestigationResponseDto {
    
    /**
     * Type of investigation
     */
    private String type;
    
    /**
     * Analysis result or description
     */
    private String analysist;
    
    /**
     * List of uploaded files
     */
    private List<InvestigationFileDto> files;
} 