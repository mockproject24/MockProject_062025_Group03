package com.group3.MockProject.mapper;

import com.group3.MockProject.dto.request.CreateEvidenceRequest;
import com.group3.MockProject.dto.response.EvidenceResponse;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Evidence;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * EvidenceMapper
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
@Component
public class EvidenceMapper {
    public Evidence toEvidenceEntity(CreateEvidenceRequest request, String fileUrl, Case caseEntity) {
        return Evidence.builder()
                .description(request.getDescription())
                .currentLocation(request.getCurrentLocation())
                .attachFile(fileUrl)
                .collectedAt(request.getCollectedAt() != null ? request.getCollectedAt() : LocalDateTime.now())
                .evidenceType(request.getEvidenceType())
                .status("ACTIVE")
                .caseEntity(caseEntity)
                .isDeleted(false)
                .build();
    }

    public EvidenceResponse toEvidenceResponse(Evidence evidence, String fileUrl) {
        return EvidenceResponse.builder()
                .caseId(evidence.getCaseEntity().getCaseId())
                .evidenceId(evidence.getEvidenceId())
                .description(evidence.getDescription())
                .currentLocation(evidence.getCurrentLocation())
                .attachFile(fileUrl)
                .evidenceType(evidence.getEvidenceType())
                .collectedAt(evidence.getCollectedAt())
                .uploadedAt(Instant.now())
                .build();
    }

}