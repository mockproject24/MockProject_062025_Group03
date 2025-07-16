package com.group3.MockProject.controller;

import com.group3.MockProject.dto.request.CreateSuspectRequest;
import com.group3.MockProject.dto.response.ApiResponse;
import com.group3.MockProject.dto.response.SuspectResponse;
import com.group3.MockProject.service.ISuspectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * SuspectController
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
@RequestMapping("/cases")
@RequiredArgsConstructor
public class SuspectController {
    private final ISuspectService suspectService;

    /**
     * Creates a new suspect for a specific case
     * @param caseId The unique identifier of the case
     * @param request The suspect creation request data
     * @param file Optional suspect photo file
     * @return ApiResponse containing the created suspect data
     */
    @PostMapping(value = "/{caseId}/suspects", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<SuspectResponse> createSuspect(@PathVariable String caseId,
            @RequestPart(name = "request") @Valid CreateSuspectRequest request,
            @RequestPart(name = "file", required = false) MultipartFile file) {
        return ApiResponse.<SuspectResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Suspect created successfully")
                .result(suspectService.createSuspect(caseId, request, file))
                .build();
    }
}
