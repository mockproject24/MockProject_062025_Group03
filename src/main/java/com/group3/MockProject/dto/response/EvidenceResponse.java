package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * EvidenceResponse
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
 * 7/4/2025      NGUYEN NGOC SY      Create
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvidenceResponse {
    private String evidenceId;
    private String description;
    private LocalDateTime collectedAt;
    private String currentLocation;
    private String attachFile;
    private String status;
    private boolean isDeleted;

    private String caseId;
    private String username;
    private String reportId;
    private String warrantId;
}
