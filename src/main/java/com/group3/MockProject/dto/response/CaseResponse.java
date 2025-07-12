package com.group3.MockProject.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaseResponse {

    @JsonProperty("case_id")
    private String caseId;

    @JsonProperty("case_number")
    private String caseNumber;

    @JsonProperty("type_case")
    private String typeCase;

    @JsonProperty("type_case_key")
    private String typeCaseKey;

    @JsonProperty("severity")
    private String severity;

    @JsonProperty("severity_key")
    private String severityKey;

    @JsonProperty("status")
    private String status;

    @JsonProperty("status_key")
    private String statusKey;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("receiving_unit")
    private String receivingUnit;

    @JsonProperty("location")
    private String location;

    @JsonProperty("reporter_fullname")
    private String reporterFullname;
}