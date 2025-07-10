package com.group3.MockProject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group3.MockProject.dto.ResponseDto;
import com.group3.MockProject.dto.request.CreateInterviewDto;
import com.group3.MockProject.dto.response.InterviewResponseDto;
import com.group3.MockProject.service.InterviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * InterviewController
 * <p>
 * Handles HTTP requests related to interviews
 * <p>
 * Version 1.0
 * Date: 7/4/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/4/2025      FongFox      Create
 * 7/10/2025     FongFox      Fix URL path and response format to match API spec
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class InterviewController {
    private final InterviewService interviewService;
    private final ObjectMapper objectMapper;

    /**
     * Create new interview API
     *
     * URL: POST /cases/{caseId}/interviews
     * Content-Type: multipart/form-data
     * Authorization: Bearer Token (handled by security config)
     *
     * Request Body:
     * - data: JSON string containing interview data
     * - file: List of attached files (optional)
     *
     * @param caseId Case ID from URL path
     * @param dataJson JSON string containing interview data
     * @param files List of attached files (optional)
     * @return ResponseDto containing created interview information
     */
    @PostMapping(value = "/cases/{caseId}/interviews",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto<InterviewResponseDto>> createInterview(
            @PathVariable String caseId,
            @RequestParam("data") String dataJson,
            @RequestParam(value = "file", required = false) List<MultipartFile> files) {
        try {
            log.info("Received request to create interview for case: {}", caseId);
            log.debug("Request data: {}", dataJson);
            log.debug("Files count: {}", files != null ? files.size() : 0);

            // STEP 1: Validate caseId
            if (caseId == null || caseId.trim().isEmpty()) {
                throw new IllegalArgumentException("Case ID is required");
            }

            // STEP 2: Parse JSON string to DTO
            CreateInterviewDto dto;
            try {
                dto = objectMapper.readValue(dataJson, CreateInterviewDto.class);
                log.debug("Successfully parsed JSON to DTO: {}", dto);
            } catch (JsonProcessingException e) {
                log.error("Failed to parse JSON data: {}", e.getMessage());
                throw new IllegalArgumentException("Invalid JSON format: " + e.getMessage());
            }

            // STEP 3: Call service to handle interview creation logic
            InterviewResponseDto interviewResponse = interviewService.createInterview(caseId, dto, files);
            log.info("Service successfully created interview");

            // STEP 4: Build success response according to API spec
            ResponseDto<InterviewResponseDto> response = new ResponseDto<>(
                    201,
                    "Interview created successfully",
                    interviewResponse
            );

            log.info("Returning success response to client");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return createErrorResponse(e, HttpStatus.BAD_REQUEST);

        } catch (EntityNotFoundException e) {
            log.error("Entity not found: {}", e.getMessage());
            return createErrorResponse(e, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            log.error("Unexpected error occurred while creating interview: {}", e.getMessage(), e);
            return createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create error response when exception occurs
     */
    private ResponseEntity<ResponseDto<InterviewResponseDto>> createErrorResponse(Exception e, HttpStatus status) {
        ResponseDto<InterviewResponseDto> errorResponse = new ResponseDto<>(
                status.value(),
                e.getMessage(),
                null
        );

        return ResponseEntity.status(status).body(errorResponse);
    }
}
