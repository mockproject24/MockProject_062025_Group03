package com.group3.MockProject.controller;



import com.group3.MockProject.dto.request.CreateEvidenceRequest;
import com.group3.MockProject.dto.response.ApiResponse;
import com.group3.MockProject.dto.response.CaseListResponse;
import com.group3.MockProject.dto.response.EvidenceResponse;
import com.group3.MockProject.service.IEvidenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

/**
 * EvidenceController
 * <p>
 * Provides business logic for managing details.
 * <p>
 * Version 1.0
 * <p>
 * Date: 08-07-2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE               AUTHOR           DESCRIPTION
 * -------------------------------------------------------------
 * 08/07/2025         Ngoc Nghia       Create
 */
@RestController
@RequestMapping("api/evidences")
@RequiredArgsConstructor
public class EvidenceController {

    private final IEvidenceService IEvidenceService;

    /**
     * Updates an existing evidence by ID
     * @param evidenceId The unique identifier of the evidence
     * @param request The evidence update request data
     * @param file Optional evidence file
     * @return ApiResponse containing the updated evidence data
     */
    @PutMapping("/{evidenceId}")
    public ApiResponse<EvidenceResponse> updateEvidence(
            @PathVariable String evidenceId,
            @ModelAttribute CreateEvidenceRequest request,
            @RequestPart(required = false) MultipartFile file
    ) {
        EvidenceResponse response = IEvidenceService.updateEvidence(evidenceId, request, file);
        return ApiResponse.<EvidenceResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update evidence by id successfully")
                .result(response)
                .build();
    }
}

