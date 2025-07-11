package com.group3.MockProject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group3.MockProject.dto.request.CreateEvidenceRequest;
import com.group3.MockProject.dto.request.CreateInterviewDto;
import com.group3.MockProject.dto.request.CreateRecordInfoDto;
import com.group3.MockProject.dto.response.*;
import com.group3.MockProject.mapper.SuspectMapper;
import com.group3.MockProject.service.CaseService;
import com.group3.MockProject.service.EvidenceService;
import com.group3.MockProject.service.InterviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

/**
 * CaseController
 *
 * Provides business logic for managing details.
 *
 * Version 1.0
 *
 * Date: 08-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE               AUTHOR           DESCRIPTION
 * -------------------------------------------------------------
 * 04/07/2025        Nguyễn Bảo Kha      Create
 * 7/4/2025          FongFox            Create
 * 7/10/2025         FongFox             Fix URL path and response format to match API spec
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/cases")
public class CaseController {
    private final CaseService caseService;
    private final SuspectMapper suspectMapper;
    private final EvidenceService evidenceService;
    private final InterviewService interviewService;
    private final ObjectMapper objectMapper;


    /**
     * Retrieves detail a specific case by its ID
     *
     * @param caseId The unique identifier of the case
     * @return CaseDetailDto containing the case detail
     */
    @GetMapping("/{caseId}")
    public ApiResponse<CaseDetailDto> getCaseDetail(@PathVariable String caseId) {
        try {
            CaseDetailDto foundCase = caseService.getCaseDetailById(caseId);
            return ApiResponse.success(foundCase);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Error retrieving case: " + e.getMessage());
        }
    }

    /**
     * Retrieves all suspects for a specific case with pagination and filtering
     * @param caseId The case identifier
     * @param page Page number (default: 1)
     * @param pageSize Number of items per page (default: 10)
     * @param status Optional status filter
     * @param date Optional date filter
     * @return ApiResponse containing paginated suspects data
     */
    @GetMapping("/{caseId}/suspects")
    public ApiResponse<?> getAllSuspects(
            @PathVariable("caseId") String caseId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "day", required = false) @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate date) {

        try {
            return ApiResponse.<SuspectsResponseDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("Get suspects successfully")
                    .result(caseService.getAllSuspectsByCaseId(caseId,page,pageSize, status, date))
                    .build();
        } catch (Exception e) {
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error retrieving suspects: " + e.getMessage())
                    .result(null)
                    .build();
        }
    }

    /**
     * Retrieves paginated list of cases with optional search
     * @param page Page number (default: 0)
     * @param pageSize Number of items per page (default: 10)
     * @param search Optional search term
     * @return ResponseEntity containing paginated cases data
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<CaseListDto>> getCaseLists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String search) {

        try {
            if (page < 0 || pageSize <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.badRequest("Page and pageSize must be greater than 0"));
            }

            CaseListDto caseListDtos = caseService.getListCase(page, pageSize, search);
            return ResponseEntity.ok(ApiResponse.success(caseListDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalServerError("Error retrieving cases: " + e.getMessage()));
        }
    }

    /**
     * Creates a new record for a specific case
     * @param caseId The case identifier
     * @param requestDto The record creation data
     * @return ResponseEntity containing the created record data
     */
    @PostMapping("/{caseId}/records")
    public ResponseEntity<ApiResponse<RecordInfoResponseDto>> createRecord(
            @PathVariable String caseId,
            @RequestBody CreateRecordInfoDto requestDto) {

        try {
            RecordInfoResponseDto createdRecord = caseService.createRecord(caseId, requestDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Record created successfully", createdRecord));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalServerError("Error creating record: " + e.getMessage()));
        }
    }

    /**
     * Retrieves all evidences for a specific case
     * @param caseId The case identifier
     * @return ResponseEntity containing case evidences data
     */
    @GetMapping("/{caseId}/evidences")
    public ResponseEntity<ApiResponse<List<EvidentDto<?>>>> getEvidences(@PathVariable String caseId) {
        try {
            List<EvidentDto<?>> evidences = caseService.getEvidences(caseId);

            if (evidences.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.success("No evidences found", evidences));
            }

            return ResponseEntity.ok(ApiResponse.success(evidences));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalServerError("Error retrieving evidences: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/{caseId}/evidences", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<EvidenceResponse>> createEvidence(
            @PathVariable String caseId,
            @RequestPart("request") @Valid CreateEvidenceRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        return ResponseEntity.ok(ApiResponse.success("Get evidence by id successfully!",
                evidenceService.createEvidence(caseId,request,file)));
    }

    @GetMapping("/{caseId}/evidence/{evidenceId}")
    public ResponseEntity<ApiResponse<EvidenceResponse>> getEvidence(
            @PathVariable String caseId,
            @PathVariable String evidenceId
    ){
        EvidenceResponse evidenceResponse = evidenceService.getEvidence(caseId,evidenceId);
        return ResponseEntity.ok(ApiResponse.success("Get evidence by id successfully!",evidenceResponse));
    }

    /**
     * Create new interview API
     *
     * URL: POST /api/cases/{caseId}/interviews
     * Content-Type: multipart/form-data OR application/json
     * Authorization: Bearer Token (handled by security config)
     *
     * For multipart/form-data:
     * - data: JSON string containing interview data
     * - file: List of attached files (optional)
     *
     * For application/json:
     * - JSON body containing interview data
     * - No file upload support
     *
     * @param caseId Case ID from URL path
     * @param dataJson JSON string containing interview data (for multipart)
     * @param requestBody Interview data (for JSON)
     * @param files List of attached files (optional, only for multipart)
     * @return ResponseDto containing created interview information
     */
    @PostMapping(value = "/{caseId}/interviews",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<com.group3.MockProject.dto.ResponseDto<InterviewResponseDto>> createInterview(
            @PathVariable String caseId,
            @RequestParam(value = "data", required = false) String dataJson,
            @RequestBody(required = false) CreateInterviewDto requestBody,
            @RequestParam(value = "file", required = false) List<MultipartFile> files,
            HttpServletRequest request) {

        try {
            log.info("Received request to create interview for case: {}", caseId);

            // Get content type from request
            String contentType = request.getContentType();
            log.debug("Content-Type: {}", contentType);

            // STEP 1: Validate caseId
            if (caseId == null || caseId.trim().isEmpty()) {
                throw new IllegalArgumentException("Case ID is required");
            }

            // STEP 2: Determine request type and parse DTO accordingly
            CreateInterviewDto dto;
            List<MultipartFile> uploadFiles = null;

            if (contentType != null && contentType.startsWith("multipart/form-data")) {
                // Handle multipart/form-data request
                log.debug("Processing multipart/form-data request");

                if (dataJson == null || dataJson.trim().isEmpty()) {
                    throw new IllegalArgumentException("Data parameter is required for multipart request");
                }

                try {
                    dto = objectMapper.readValue(dataJson, CreateInterviewDto.class);
                    uploadFiles = files;
                    log.debug("Successfully parsed JSON from form data: {}", dto);
                    log.debug("Files count: {}", uploadFiles != null ? uploadFiles.size() : 0);
                } catch (JsonProcessingException e) {
                    log.error("Failed to parse JSON data from form: {}", e.getMessage());
                    throw new IllegalArgumentException("Invalid JSON format in data parameter: " + e.getMessage());
                }

            }
            else if (contentType != null && contentType.startsWith("application/json")) {
                // Handle application/json request
                log.debug("Processing application/json request");

                if (requestBody == null) {
                    throw new IllegalArgumentException("Request body is required for JSON request");
                }

                dto = requestBody;
                uploadFiles = null; // No file upload support for JSON requests
                log.debug("Successfully received JSON request body: {}", dto);

            }
            else {
                throw new IllegalArgumentException("Unsupported Content-Type. Use multipart/form-data or application/json");
            }

            // STEP 3: Call service to handle interview creation logic (error here)
            InterviewResponseDto interviewResponse = interviewService.createInterview(caseId, dto, uploadFiles);
            log.info("Service successfully created interview");

            // STEP 4: Build success response according to API spec
            com.group3.MockProject.dto.ResponseDto<InterviewResponseDto> response = new com.group3.MockProject.dto.ResponseDto<>(
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
    private ResponseEntity<com.group3.MockProject.dto.ResponseDto<InterviewResponseDto>> createErrorResponse(Exception e, HttpStatus status) {
        com.group3.MockProject.dto.ResponseDto<InterviewResponseDto> errorResponse = new com.group3.MockProject.dto.ResponseDto<>(
                status.value(),
                e.getMessage(),
                null
        );

        return ResponseEntity.status(status).body(errorResponse);
    }
}
