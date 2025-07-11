package com.group3.MockProject.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * OfficerCaseDetailDto
 *
 * Provides officer case detail data transfer object for API responses.
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
public class OfficerCaseDetailDto {

    /**
     * Officer unique identifier
     */
    @JsonProperty("officerId")
    private String officerId;

    /**
     * Officer full name
     */
    @JsonProperty("fullName")
    private String fullName;

    /**
     * Current status of the officer
     */
    @JsonProperty("presentStatus")
    private String presentStatus;

    /**
     * Officer role/position
     */
    @JsonProperty("role")
    private String role;

    /**
     * Officer phone number
     */
    @JsonProperty("phoneNumber")
    private String phoneNumber;

    /**
     * Officer assigned zone/sector
     */
    @JsonProperty("zone")
    private String zone;

    /**
     * Case assignment description
     */
    @JsonProperty("description")
    private String description;

    /**
     * Medical information related to the case
     */
    @JsonProperty("medical_info")
    private String medicalInfo;

    /**
     * Support details for the assignment
     */
    @JsonProperty("support_details")
    private String supportDetails;

    /**
     * Assignment date and time
     */
    @JsonProperty("assignmentDate")
    private LocalDateTime assignmentDate;
} 