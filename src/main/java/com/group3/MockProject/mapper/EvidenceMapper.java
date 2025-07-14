package com.group3.MockProject.mapper;

import com.group3.MockProject.dto.response.EvidenceResponse;
import com.group3.MockProject.dto.response.SuspectResponse;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Evidence;
import com.group3.MockProject.entity.Suspect;
import org.springframework.stereotype.Component;

import java.time.Instant;

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
    public EvidenceResponse toResponse(Evidence evidence, String mugshotUrl, Case caseEntity) {
        return EvidenceResponse.builder()
                .evidenceId(evidence.getEvidenceId())
                .description(evidence.getDescription())
                .collectedAt(evidence.getCollectedAt())
                .currentLocation(evidence.getCurrentLocation())
                .attachFile(evidence.getAttachFile())
//                .status(evidence.getStatus())
//                .isDeleted(evidence.isDeleted())
                .caseId(caseEntity.getCaseId())
                .build();
    }

    private SuspectResponse toSuspectResponse(Suspect suspect, String mugshotUrl, Case caseEntity) {
        return SuspectResponse.builder()
                .caseId(caseEntity.getCaseId())
                .fullName(suspect.getFullname())
                .address(suspect.getAddress())
                .moreInfo(suspect.getNotes())
                .mugshotUrl(mugshotUrl)
                .uploadedAt(Instant.now())
                .build();
    }

}