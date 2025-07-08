package com.group3.MockProject.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * CaseDto
 *
 * Provides case data transfer object for API responses.
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
public class CaseDto {
    
    /**
     * Case unique identifier
     */
    @JsonProperty("case_id")
    private String caseId;
    
    /**
     * Case number for display
     */
    @JsonProperty("case_number")
    private String caseNumber;
    
    /**
     * Type of the case
     */
    @JsonProperty("type_case")
    private String typeCase;
    
    /**
     * Severity level of the case
     */
    @JsonProperty("severity")
    private String severity;
    
    /**
     * Current status of the case
     */
    @JsonProperty("status")
    private String status;
    
    /**
     * Case creation timestamp
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    /**
     * Unit that received the case
     */
    @JsonProperty("receiving_unit")
    private String receivingUnit;
    
    /**
     * Location where the case occurred
     */
    @JsonProperty("location")
    private String location;
}