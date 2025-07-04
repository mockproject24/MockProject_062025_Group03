package com.group3.MockProject.mapper;

import com.group3.MockProject.dto.response.EvidenceResponse;
import com.group3.MockProject.entity.Evidence;
import org.springframework.stereotype.Component;

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
    public EvidenceResponse toResponse(Evidence evidence) {
        return EvidenceResponse.builder()
                .evidenceId(evidence.getEvidenceId())
                .description(evidence.getDescription())
                .collectedAt(evidence.getCollectedAt())
                .currentLocation(evidence.getCurrentLocation())
                .attachFile(evidence.getAttachFile())
                .status(evidence.getStatus())
                .isDeleted(evidence.isDeleted())
                .caseId(evidence.getCaseEntity() != null ? evidence.getCaseEntity().getCaseId() : null)
                .username(evidence.getUser() != null ? evidence.getUser().getUsername() : null)
                .reportId(evidence.getReport() != null ? evidence.getReport().getReportId() : null)
                .warrantId(evidence.getWarrant() != null ? evidence.getWarrant().getWarrantId() : null)
                .build();
    }

}