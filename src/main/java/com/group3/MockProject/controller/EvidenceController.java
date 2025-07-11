package com.group3.MockProject.controller;


/**
 * EvidenceController
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 7/10/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/10/2025      DBD      Create
 */

import com.group3.MockProject.dto.request.CreateEvidenceRequest;
import com.group3.MockProject.dto.response.EvidenceResponse;
import com.group3.MockProject.service.EvidenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/evidences")
@RequiredArgsConstructor
public class EvidenceController {

    private final EvidenceService evidenceService;

    @PutMapping("/{evidenceId}")
    public ResponseEntity<EvidenceResponse> updateEvidence(
            @PathVariable String evidenceId,
            @ModelAttribute CreateEvidenceRequest request,
            @RequestPart(required = false) MultipartFile file
    ) {
        EvidenceResponse response = evidenceService.updateEvidence(evidenceId, request, file);
        return ResponseEntity.ok(response);
    }
}

