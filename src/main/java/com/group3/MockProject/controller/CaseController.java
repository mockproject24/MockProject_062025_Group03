package com.group3.MockProject.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group3.MockProject.constant.CaseType;
import com.group3.MockProject.constant.SeverityType;
import com.group3.MockProject.dto.request.CreateEvidenceRequest;
import com.group3.MockProject.dto.request.CreateInterviewRequest;
import com.group3.MockProject.dto.request.CreateInvestigationRequest;
import com.group3.MockProject.dto.request.CreateSuspectRequest;
import com.group3.MockProject.dto.response.*;
import com.group3.MockProject.exception.AppException;
import com.group3.MockProject.exception.ErrorCode;
import com.group3.MockProject.service.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.group3.MockProject.dto.request.CreateRecordInfoRequest;
import com.group3.MockProject.mapper.SuspectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * CaseController
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
 * 04/07/2025        Nguyễn Bảo Kha      Create
 * 4/7/2025          FongFox            Create
 * 10/7/2025         FongFox            Fix URL path and response format to match API spec
 * 12/7/2025         FongFox            Fix URL path and response format to match API spec
 * 23/7/2025         FongFox            Develop Get statement detail API.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/cases")
public class CaseController {
    private final ICaseService caseService;
    private final IEvidenceService evidenceService;
    private final IInterviewService interviewService;
    private final InvestigationService investigationService;
    private final IStatementService statementService;
    private final ISuspectService suspectService;

    private final SuspectMapper suspectMapper;
    private final ObjectMapper objectMapper;

    /**
     * Retrieves case metadata including case types and severities
     * @return ResponseEntity containing case metadata
     */
    @GetMapping("/case-meta")
    public ApiResponse<CaseListMetaResponse> getCaseMeta() {
        CaseListMetaResponse caseMeta = caseService.getCaseMeta();
        return ApiResponse.<CaseListMetaResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get meta data successfully")
                .result(caseMeta)
                .build();
    }

    /**
     * Retrieves paginated list of cases with optional search
     * @param page Page number (default: 0)
     * @param pageSize Number of items per page (default: 10)
     * @param search Optional search term
     * @return ResponseEntity containing paginated cases data
     */
    @GetMapping("")
    public ApiResponse<CaseListResponse> getCaseLists(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) SeverityType severitType,
            @RequestParam(required = false) CaseType caseType,
            @RequestParam(required = false) LocalDateTime date) {


        if (page < 0 || pageSize <= 0) {
           throw new AppException(ErrorCode.CASE_PAGE_SIZE);
        }

        CaseListResponse caseListDtos = caseService.getListCase(page, pageSize, search, severitType, caseType, date);
        return ApiResponse.<CaseListResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get list data cases successfully")
                .result(caseListDtos)
                .build();

    }

    /**
     * Retrieves a specific case by its ID
     *
     * @param caseId The unique identifier of the case
     * @return ResponseEntity containing the case data
     */
    @GetMapping("/{caseId}")
    public ApiResponse<CaseDetailResponse> getCaseById(@PathVariable String caseId) {
        return ApiResponse.<CaseDetailResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get case detail")
                .result(caseService.getCaseById(caseId))
                .build();
    }

    /**
     * Retrieves all suspects for a specific case with pagination and filtering
     *
     * @param caseId   The case identifier
     * @param page     Page number (default: 1)
     * @param pageSize Number of items per page (default: 10)
     * @param status   Optional status filter
     * @param date     Optional date filter
     * @return ApiResponse containing paginated suspects data
     */
    @GetMapping("/{caseId}/suspects")
    public ApiResponse<?> getAllSuspects(
            @PathVariable("caseId") String caseId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "day", required = false) @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate date) {
            return ApiResponse.<SuspectsResponseDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("Get suspects succesfully")
                    .result(caseService.getAllSuspectsByCaseId(caseId, page, pageSize, status, date))
                    .build();
    }

    /**
     * Retrieves officer case details for a specific case with pagination
     *
     * @param caseId   The case identifier
     * @param page     Page number (default: 0)
     * @param pageSize Number of items per page (default: 10)
     * @return ResponseEntity containing officer case details data
     */
    @GetMapping("/{caseId}/officer")
    public ApiResponse<List<OfficerCaseDetailResponse>> getOfficerCaseDetails(
            @PathVariable String caseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        return ApiResponse.<List<OfficerCaseDetailResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get Officer Case Details succesfully")
                .result(caseService.getOfficerCaseDetails(caseId, page, pageSize))
                .build();
    }


    /**
     * Creates a new record for a specific case
     * @param caseId The case identifier
     * @param requestDto The record creation data
     * @return ResponseEntity containing the created record data
     */
    @PostMapping("/{caseId}/records")
    public ApiResponse<RecordInfoResponseResponse> createRecord(
            @PathVariable String caseId,
            @RequestBody CreateRecordInfoRequest requestDto) {

        RecordInfoResponseResponse createdRecord = caseService.createRecord(caseId, requestDto);
        return ApiResponse.<RecordInfoResponseResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Record created successfully")
                .result(createdRecord)
                .build();
    }


    /**
     * Retrieves paginated list of officers assigned to a specific case
     *
     * @param caseId   The unique identifier of the case
     * @param page     Page number (default: 0)
     * @param pageSize Number of items per page (default: 10)
     * @return ResponseEntity containing paginated officers data
     */
    @GetMapping("/{caseId}/assigned-officers")
    public ApiResponse<Map<String, Object>> getAssignedOfficers(
            @PathVariable String caseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page and pageSize must be greater than 0");
        }

        Page<UserResponseDto> officerPage = caseService.getAssignedOfficers(caseId, PageRequest.of(page, pageSize));
        Map<String, Object> result = new HashMap<>();
        result.put("content", officerPage.getContent());
        result.put("totalElements", officerPage.getTotalElements());
        result.put("totalPages", officerPage.getTotalPages());
        result.put("size", officerPage.getSize());
        result.put("number", officerPage.getNumber());

        return new ApiResponse<>(HttpStatus.OK.value(), "Assigned officers retrieved successfully", result);
    }

    /**
     * Retrieves all evidences for a specific case
     *
     * @param caseId The case identifier
     * @return ResponseEntity containing case evidences data
     */
    @GetMapping("/{caseId}/evidences")
    public ApiResponse<List<EvidentResponse<?>>> getEvidences(@PathVariable String caseId) {
        List<EvidentResponse<?>> evidences = caseService.getEvidences(caseId);
        if (evidences.isEmpty()) {
            return ApiResponse.<List<EvidentResponse<?>>>builder()
                    .code(HttpStatus.OK.value())
                    .message("No evidences found")
                    .result(evidences)
                    .build();
        }
        return ApiResponse.<List<EvidentResponse<?>>>builder()
                .code(HttpStatus.OK.value())
                .message("Get evidences successfully")
                .result(evidences)
                .build();

    }

    /**
     * Creates a new evidence for a specific case
     * @param caseId The unique identifier of the case
     * @param request The evidence creation request data
     * @param file Optional evidence file
     * @return ResponseEntity containing the created evidence data
     */
    @PostMapping(value = "/{caseId}/evidences", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<EvidenceResponse> createEvidence(
            @PathVariable String caseId,
            @RequestPart("request") @Valid CreateEvidenceRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        return ApiResponse.<EvidenceResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Evidence created successfully!")
                .result(evidenceService.createEvidence(caseId, request, file))
                .build();
    }

    /**
     * Retrieves a specific evidence by case ID and evidence ID
     * @param caseId The unique identifier of the case
     * @param evidenceId The unique identifier of the evidence
     * @return ApiResponse containing the evidence data
     */
    @GetMapping("/{caseId}/evidence/{evidenceId}")
    public ApiResponse<EvidenceResponse> getEvidence(
            @PathVariable String caseId,
            @PathVariable String evidenceId
    ) {
        EvidenceResponse evidenceResponse = evidenceService.getEvidence(caseId, evidenceId);
        return ApiResponse.<EvidenceResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get evidence by id successfully!")
                .result(evidenceResponse)
                .build();
    }

    /**
     * Creates a new interview for a case
     *
     * This endpoint handles interview creation with the following features:
     * - Supports multipart/form-data for file uploads
     * - Validates interview data and checks for scheduling conflicts
     * - Creates questions with trust level conversion
     * - Handles optional file attachments (audio/video recordings)
     * - Returns comprehensive interview information
     *
     * @param caseId The case identifier from URL path
     * @param dataJson JSON string containing interview data (questions, timing, participants)
     * @param files Optional list of attached files (recordings, documents)
     * @return ApiResponse containing created interview details
     */
    @PostMapping(value = "/{caseId}/interviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<InterviewResponse> createInterview(
            @PathVariable String caseId,
            @RequestParam("data") String dataJson,
            @RequestParam(value = "file", required = false) List<MultipartFile> files
    ) {
        try {
            log.info("Creating interview for case: {}", caseId);

            // Parse JSON data to CreateInterviewRequest object
            CreateInterviewRequest request = objectMapper.readValue(dataJson, CreateInterviewRequest.class);

            // Call service to create interview with questions and files
            InterviewResponse response = interviewService.createInterview(caseId, request, files);

            return ApiResponse.<InterviewResponse>builder()
                    .code(HttpStatus.CREATED.value())
                    .message("Interview created successfully")
                    .result(response)
                    .build();

        } catch (JsonProcessingException e) {
            log.error("Invalid JSON format: {}", e.getMessage());
            throw new AppException(ErrorCode.INVALID_DATA_FORMAT, "Invalid JSON format in data parameter");
        }
    }


    /**
     * Creates a new suspect for a specific case
     * @param caseId The unique identifier of the case
     * @param request The suspect creation request data
     * @param file Optional suspect photo file
     * @return ApiResponse containing the created suspect data
     */
    @PostMapping(value = "/{caseId}/suspects", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<SuspectResponse> createSuspect(
            @PathVariable String caseId,
            @RequestPart("request") @Valid CreateSuspectRequest request,
            @RequestPart(name = "file", required = false) MultipartFile file) {
        return ApiResponse.<SuspectResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Suspect created successfully")
                .result(suspectService.createSuspect(caseId, request, file))
                .build();
    }

    /**
     * Create new investigation
     * <p>
     * URL: POST /cases/{caseId}/investigations
     * Content-Type: multipart/form-data
     * <p>
     * Request Body:
     * - data: JSON string containing investigation data (type, analysist)
     * - file: List of attached files (optional)
     *
     * @param caseId   Case ID from URL path
     * @param dataJson JSON string containing investigation data
     * @param files    List of attached files (optional)
     * @return ApiResponse containing created investigation information
     */
    @PostMapping(value = "/{caseId}/investigations",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<InvestigationResponse> createInvestigation(
            @PathVariable String caseId,
            @RequestParam("data") String dataJson,
            @RequestParam(value = "file", required = false) List<MultipartFile> files) {

        try {
            log.info("Creating investigation for case: {}", caseId);

            // Parse JSON data to DTO
            CreateInvestigationRequest request = objectMapper.readValue(dataJson, CreateInvestigationRequest.class);

            // Create investigation through service
            InvestigationResponse response = investigationService.createInvestigation(caseId, request, files);

            log.info("Investigation created successfully for case: {}", caseId);
            return ApiResponse.<InvestigationResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Investigation created successfully")
                    .result(response)
                    .build();

        } catch (JsonProcessingException e) {
            log.error("Invalid JSON format in data parameter: {}", e.getMessage());
            throw new AppException(ErrorCode.INVALID_KEY, "Invalid JSON format in data parameter");

        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            throw new AppException(ErrorCode.INVALID_KEY, e.getMessage());

        } catch (Exception e) {
            log.error("Unexpected error occurred while creating investigation: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Error creating investigation: " + e.getMessage());
        }
    }

    /**
     * Retrieves a statement by case ID and statement ID
     * <p>
     * This endpoint retrieves detailed information about a specific statement
     * including person information and evidence links.
     * </p>
     *
     * @param caseId The unique identifier of the case
     * @param statementId The unique identifier of the statement
     * @return ApiResponse containing statement details
     *
     * @throws AppException if case or statement not found
     */
    @GetMapping("/{caseId}/statements/{statementId}")
    public ApiResponse<StatementResponse> getStatement(
            @PathVariable String caseId,
            @PathVariable String statementId) {

        log.info("GET /api/cases/{}/statements/{} - Request received", caseId, statementId);

        try {
            StatementResponse statementResponse = statementService.getStatementByCaseIdAndStatementId(caseId, statementId);

            log.info("GET /api/cases/{}/statements/{} - Request completed successfully", caseId, statementId);

            return ApiResponse.<StatementResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Statement retrieved successfully")
                    .result(statementResponse)
                    .build();

        } catch (Exception e) {
            log.error("GET /api/cases/{}/statements/{} - Request failed: {}", caseId, statementId, e.getMessage());
            // Exception will be handled by GlobalExceptionHandler
            throw e;
        }
    }
}
