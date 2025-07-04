package com.group3.MockProject.service;

import com.group3.MockProject.dto.request.CreateEvidenceDto;
import com.group3.MockProject.dto.response.EvidenceResponse;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Evidence;
import com.group3.MockProject.entity.User;
import com.group3.MockProject.mapper.EvidenceMapper;
import com.group3.MockProject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * EvidenceService
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
@Service
@RequiredArgsConstructor
public class EvidenceService {

    private final EvidenceRepository evidenceRepository;
    private final UserRepository userRepository;
    private final CaseRepository caseRepository;
    private final ReportRepository reportRepository;
    private final WarrantRepository warrantRepository;
    private final EvidenceMapper evidenceMapper;

    public EvidenceResponse createEvidence(String caseId, CreateEvidenceDto request) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new IllegalArgumentException("Case not found with id: " + caseId));

        Evidence evidence = new Evidence();
        evidence.setDescription(request.getDescription());
        evidence.setCollectedAt(request.getCollectedAt());
        evidence.setCurrentLocation(request.getCurrentLocation());
        evidence.setAttachFile(request.getAttachFile());
        evidence.setStatus(request.getStatus());
        evidence.setCaseEntity(caseEntity);

        String username = getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        evidence.setUser(user);

        if (request.getReportId() != null) {
            reportRepository.findById(request.getReportId()).ifPresent(evidence::setReport);
        }

        if (request.getWarrantId() != null) {
            warrantRepository.findById(request.getWarrantId()).ifPresent(evidence::setWarrant);
        }

        Evidence saved = evidenceRepository.save(evidence);
        return evidenceMapper.toResponse(saved);
    }

    public EvidenceResponse getEvidence(String caseId, String evidenceId){
        Evidence evidence = evidenceRepository.findById(evidenceId)
                .orElseThrow(() -> new RuntimeException("Evidence not found with id: " + evidenceId));

        if (evidence.getCaseEntity() == null || !evidence.getCaseEntity().getCaseId().equals(caseId)) {
            throw new IllegalArgumentException("Evidence does not belong to the given caseId: " + caseId);
        }

        return evidenceMapper.toResponse(evidence);
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}