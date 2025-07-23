package com.group3.MockProject.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * StatementResponse
 * <p>
 * DTO for statement data returned by API
 * <p>
 * Version 1.0
 * Date: 7/23/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/23/2025      FongFox      Create
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementResponse {
    @JsonProperty("statement_id")
    private String statementId;

    @JsonProperty("case_id")
    private String caseId;

    @JsonProperty("initial_name")
    private String initialName;

    @JsonProperty("statement_date")
    private Instant statementDate;

    @JsonProperty("contact_information")
    private String contactInformation;

    @JsonProperty("initial_role_type")
    private String initialRoleType;

    @JsonProperty("content")
    private String content;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    // Information about the person who gave the statement
    @JsonProperty("person_info")
    private PersonInfo personInfo;

    // Evidence links
    @JsonProperty("evidence_links")
    private List<EvidenceLink> evidenceLinks;

    /**
     * Nested class for person information
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonInfo {
        @JsonProperty("person_id")
        private String personId;

        @JsonProperty("full_name")
        private String fullName;

        @JsonProperty("type")
        private String type; // SUSPECT, VICTIM, WITNESS

        @JsonProperty("contact")
        private String contact;
    }

    /**
     * Nested class for evidence link information
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EvidenceLink {
        @JsonProperty("evidence_id")
        private String evidenceId;

        @JsonProperty("file_name")
        private String fileName;

        @JsonProperty("file_url")
        private String fileUrl;

        @JsonProperty("evidence_type")
        private String evidenceType;

        @JsonProperty("description")
        private String description;
    }
}
