package com.group3.MockProject.controller;

import com.group3.MockProject.dto.ApiResponse;
import com.group3.MockProject.dto.request.CreateSuspectRequest;
import com.group3.MockProject.dto.response.SuspectResponse;
import com.group3.MockProject.service.ISuspectService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * SuspectController
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 04/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 04/07/2025   Hải Đăng      Create
 */
@RestController
@RequestMapping("/cases")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SuspectController {

    ISuspectService suspectService;

    @PostMapping(value = "/{caseId}/suspects", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<SuspectResponse> createSuspect(
            @PathVariable String caseId,
            @RequestPart("request") @Valid CreateSuspectRequest request,
            @RequestPart(name = "file", required = false) MultipartFile file) {
        return ApiResponse.<SuspectResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Suspect created successfully")
                .data(suspectService.createSuspect(caseId, request, file))
                .build();
    }

}
