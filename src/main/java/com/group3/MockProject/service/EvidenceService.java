package com.group3.MockProject.service;

import com.group3.MockProject.dto.request.CreateEvidenceRequest;
import com.group3.MockProject.dto.response.EvidenceResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * EvidenceService
 *
 * Provides business logic for managing employment details.
 *
 * Version 1.0
 * Date: 7/9/2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/9/2025      NGUYEN NGOC SY      Create
 */

@Service
public interface EvidenceService {
    EvidenceResponse createEvidence(String caseId, CreateEvidenceRequest request, MultipartFile file);
    EvidenceResponse getEvidence(String caseId, String evidenceId);

}