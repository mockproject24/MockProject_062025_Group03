package com.group3.MockProject.dto.response;

import com.group3.MockProject.constant.EvidenceType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvidenceResponse {

    String caseId;
    String evidenceId;
    String description;
    String currentLocation;
    String attachFile;
    EvidenceType evidenceType;
    LocalDateTime collectedAt;
    Instant uploadedAt;
    String collector;
    String status;
}
