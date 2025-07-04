package com.group3.MockProject.controller;


import com.group3.MockProject.dto.request.UpdateEvidenceDto;
import com.group3.MockProject.dto.response.EvidentDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
 * 7/4/2025      Phuong Dong      Create
 */

import com.group3.MockProject.dto.request.UpdateEvidenceDto;
import com.group3.MockProject.dto.response.EvidenceResponseDto;
import com.group3.MockProject.service.EvidenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * EvidenceController
 *
 * Handles evidence-related endpoints.
 */
@RestController
@RequestMapping("api/evidences")
@RequiredArgsConstructor
public class EvidenceController {

    private final EvidenceService evidenceService;

    /**
     * Update evidence by ID.
     *
     * Example request:
     * PUT /api/v1/evidences/{evidenceId}
     */
    @PutMapping("/{evidenceId}")
    public ResponseEntity<EvidenceResponseDto> updateEvidence(
            @PathVariable String evidenceId,
            @RequestBody UpdateEvidenceDto request) {
        EvidenceResponseDto response = evidenceService.updateEvidence(evidenceId, request);
        return ResponseEntity.ok(response);
    }
}
