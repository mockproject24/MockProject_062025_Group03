package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * InvestigationFileDto
 *
 * DTO for investigation file information in response
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
public class InvestigationFileDto {
    
    /**
     * Original filename
     */
    private String filename;
    
    /**
     * File URL for access
     */
    private String url;
} 