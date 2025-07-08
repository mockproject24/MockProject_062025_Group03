package com.group3.MockProject.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group3.MockProject.dto.request.CreateRecordInfoDto;
import com.group3.MockProject.dto.response.ApiResponse;
import com.group3.MockProject.dto.response.CaseListDto;
import com.group3.MockProject.dto.response.EvidentDto;
import com.group3.MockProject.dto.response.RecordInfoResponseDto;
import com.group3.MockProject.dto.response.SuspectResponseDto;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Suspect;
import com.group3.MockProject.mapper.SuspectMapper;
import com.group3.MockProject.service.CaseService;

import lombok.RequiredArgsConstructor;

/**
 * CaseController
 *
 * Provides REST API endpoints for case management operations.
 *
 * Version 1.0
 *
 * Date: 08-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE                 AUTHOR          DESCRIPTION
 * -----------------------------------------------------------------------
 * 08-07-2025         Group3            Create
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cases")
public class CaseController {
    
    private final CaseService caseService;
    private final SuspectMapper suspectMapper;

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
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllSuspects(
            @PathVariable("caseId") String caseId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "day", required = false) @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate date) {
        
        try {
            Pageable pageable = PageRequest.of(page - 1, pageSize);
            Page<Suspect> suspectsPage = caseService.getAllSuspectsByCaseId(caseId, pageable, status, date);
            
            List<SuspectResponseDto> suspects = suspectsPage == null ? 
                    new ArrayList<>() : 
                    suspectsPage.getContent().stream()
                            .map(suspectMapper::toSuspectResponseDto)
                            .toList();
            
            Map<String, Object> responseResult = new HashMap<>();
            responseResult.put("suspects", suspects);
            responseResult.put("page", page);
            responseResult.put("pageSize", pageSize);
            responseResult.put("total", (suspectsPage == null ? 0 : suspectsPage.getTotalElements()));
            
            return ResponseEntity.ok(ApiResponse.success("Get suspects successfully", responseResult));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalServerError("Error retrieving suspects: " + e.getMessage()));
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
}
