package com.group3.MockProject.controller;

import com.group3.MockProject.dto.request.CreateEvidenceDto;
import com.group3.MockProject.dto.response.EvidenceResponse;
import com.group3.MockProject.service.EvidenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * EvidenceController
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/cases")
public class EvidenceController {
    private final EvidenceService evidenceService;

    @PostMapping("/{caseId}/evidences")
    public ResponseEntity<EvidenceResponse> addEvidence(
            @PathVariable String caseId,
            @Valid @RequestBody CreateEvidenceDto request
    ) {
        EvidenceResponse evidence = evidenceService.createEvidence(caseId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(evidence);
    }
    @GetMapping("/{caseId}/evidence/{evidenceId}")
    public ResponseEntity<EvidenceResponse> getEvidence(
            @PathVariable String caseId,
            @PathVariable String evidenceId
    ){
        EvidenceResponse evidenceResponse = evidenceService.getEvidence(caseId,evidenceId);
        return ResponseEntity.ok(evidenceResponse);
    }
}