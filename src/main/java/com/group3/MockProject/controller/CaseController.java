package com.group3.MockProject.controller;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group3.MockProject.dto.request.CreateEvidenceRequest;
import com.group3.MockProject.dto.request.CreateInterviewDto;
import com.group3.MockProject.dto.request.CreateInvestigationRequest;
import com.group3.MockProject.dto.request.CreateSuspectRequest;
import com.group3.MockProject.dto.response.*;
import com.group3.MockProject.service.EvidenceService;
import com.group3.MockProject.service.InvestigationService;
import com.group3.MockProject.service.ISuspectService;
import com.group3.MockProject.service.InterviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.group3.MockProject.dto.request.CreateRecordInfoDto;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.mapper.SuspectMapper;
import com.group3.MockProject.service.CaseService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

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
    private final InvestigationService investigationService;
    private final ObjectMapper objectMapper;
    private final ISuspectService suspectService;



    /**
     * Retrieves a specific case by its ID
     * @param caseId The unique identifier of the case
     * @return ResponseEntity containing the case data
     */
    @GetMapping("/{caseId}")
    public ResponseEntity<ApiResponse<Case>> getCaseById(@PathVariable String caseId) {
        try {
            Case foundCase = caseService.getCaseById(caseId);
            return ResponseEntity.ok(ApiResponse.success(foundCase));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalServerError("Error retrieving case: " + e.getMessage()));
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
                    .message("Get suspects succesfully")
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
     * Retrieves officer case details for a specific case with pagination
     * @param caseId The case identifier
     * @param page Page number (default: 0)
     * @param pageSize Number of items per page (default: 10)
     * @return ResponseEntity containing officer case details data
     */
    @GetMapping("/{caseId}/officer")
    public ResponseEntity<ApiResponse<List<OfficerCaseDetailDto>>> getOfficerCaseDetails(
            @PathVariable String caseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        try {
            if (page < 0 || pageSize <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.badRequest("Page and pageSize must be greater than 0"));
            }

            List<OfficerCaseDetailDto> officers = caseService.getOfficerCaseDetails(caseId, page, pageSize);
            
            return ResponseEntity.ok(ApiResponse.success("Successfully retrieved officer case details", officers));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalServerError("Error retrieving officer case details: " + e.getMessage()));
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


        if (page < 0 || pageSize <= 0) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("Page and pageSize must be greater than 0"));
        }

        CaseListDto caseListDtos = caseService.getListCase(page, pageSize, search);
        return ResponseEntity.ok(ApiResponse.success(caseListDtos));

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
     *
     */
    @PostMapping(value = "/{caseId}/interviews",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<com.group3.MockProject.dto.ResponseDto<InterviewResponseDto>> createInterview(
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


    @PostMapping(value = "/{caseId}/suspects", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    com.group3.MockProject.dto.ApiResponse<SuspectResponse> createSuspect(
            @PathVariable String caseId,
            @RequestPart("request") @Valid CreateSuspectRequest request,
            @RequestPart(name = "file", required = false) MultipartFile file) {
        return com.group3.MockProject.dto.ApiResponse.<SuspectResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Suspect created successfully")
                .data(suspectService.createSuspect(caseId, request, file))
                .build();
    }

    /**
     * Create new investigation
     * 
     * URL: POST /cases/{caseId}/investigations
     * Content-Type: multipart/form-data
     * 
     * Request Body:
     * - data: JSON string containing investigation data (type, analysist)
     * - file: List of attached files (optional)
     * 
     * @param caseId Case ID from URL path
     * @param dataJson JSON string containing investigation data
     * @param files List of attached files (optional)
     * @return ApiResponse containing created investigation information
     */
    @PostMapping(value = "/{caseId}/investigations",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<InvestigationResponseDto>> createInvestigation(
            @PathVariable String caseId,
            @RequestParam("data") String dataJson,
            @RequestParam(value = "file", required = false) List<MultipartFile> files) {

        try {
            log.info("Creating investigation for case: {}", caseId);
            
            // Parse JSON data to DTO
            CreateInvestigationRequest request = objectMapper.readValue(dataJson, CreateInvestigationRequest.class);
            
            // Create investigation through service
            InvestigationResponseDto response = investigationService.createInvestigation(caseId, request, files);
            
            log.info("Investigation created successfully for case: {}", caseId);
            return ResponseEntity.ok(ApiResponse.success("Success", response));
            
        } catch (JsonProcessingException e) {
            log.error("Invalid JSON format in data parameter: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("Invalid JSON format in data parameter"));
                    
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
                    
        } catch (Exception e) {
            log.error("Unexpected error occurred while creating investigation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalServerError("Error creating investigation: " + e.getMessage()));
        }
    }
}
