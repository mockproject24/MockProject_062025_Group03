package com.group3.MockProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group3.MockProject.dto.ResponseDto;
import com.group3.MockProject.dto.request.CreateInterviewDto;
import com.group3.MockProject.dto.response.InterviewResponseDto;
import com.group3.MockProject.service.InterviewService;
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
 */
@RestController
@RequestMapping("cases")
@Slf4j
public class InterviewController {
    private final InterviewService interviewService;
    private final ObjectMapper objectMapper;

    public InterviewController(InterviewService interviewService, ObjectMapper objectMapper) {
        this.interviewService = interviewService;
        this.objectMapper = objectMapper;
    }

    /**
     * Create new interview API
     *
     * URL: POST /cases/{caseId}/suspects/{suspectId}/interviews
     * Content-Type: multipart/form-data
     *
     * @param caseId Case ID from URL path
     * @param suspectId Suspect ID from URL path
     * @param dataJson JSON string containing interview data
     * @param files List of attached files (optional)
     * @return ResponseDto containing created interview information
     */
    @PostMapping(value = "/{caseId}/suspects/{suspectId}/interviews",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<InterviewResponseDto>> createInterview(
            @PathVariable String caseId,
            @PathVariable String suspectId,
            @RequestParam("data") String dataJson,
            @RequestParam(value = "file", required = false) List<MultipartFile> files) {

        try {
            log.info("Received request to create interview for case: {}, suspect: {}", caseId, suspectId);

            // STEP 1: Parse JSON string to DTO
            CreateInterviewDto dto = objectMapper.readValue(dataJson, CreateInterviewDto.class);
            log.debug("Successfully parsed JSON to DTO: {}", dto);

            // STEP 2: Call service to handle interview creation logic
            InterviewResponseDto interviewResponse = interviewService.createInterview(caseId, suspectId, dto, files);
            log.info("Service successfully created interview with ID: {}", interviewResponse.getInterviewId());

            // STEP 3: Build success response
            ResponseDto<InterviewResponseDto> response = new ResponseDto<>();
            response.setCode(201);
            response.setMessage("Interview created successfully");
            response.setResult(interviewResponse);
            response.setTimestamp(LocalDateTime.now());

            log.info("Returning success response to client");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("Error occurred while creating interview: {}", e.getMessage(), e);
            return createErrorResponse(e);
        }
    }

    /**
     * Create error response when exception occurs
     */
    private ResponseEntity<ResponseDto<InterviewResponseDto>> createErrorResponse(Exception e) {
        int statusCode = getStatusCodeFromException(e);

        ResponseDto<InterviewResponseDto> errorResponse = new ResponseDto<>();
        errorResponse.setCode(statusCode);
        errorResponse.setMessage(e.getMessage());
        errorResponse.setResult(null);
        errorResponse.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(statusCode).body(errorResponse);
    }

    /**
     * Determine HTTP status code based on exception type
     */
    private int getStatusCodeFromException(Exception e) {
        String exceptionName = e.getClass().getSimpleName();

        return switch (exceptionName) {
            case "EntityNotFoundException" -> 404; // Not Found

            case "IllegalArgumentException", "BadRequestException" -> 400; // Bad Request

            case "JsonProcessingException" -> 400; // Bad Request - Invalid JSON

            default -> 500; // Internal Server Error

        };
    }
}
