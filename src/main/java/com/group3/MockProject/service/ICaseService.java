package com.group3.MockProject.service;

import java.time.LocalDate;
import java.util.List;

import com.group3.MockProject.dto.response.*;
import com.group3.MockProject.dto.response.CaseDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.group3.MockProject.dto.request.CreateRecordInfoRequest;
import com.group3.MockProject.entity.Case;

/**
 * CaseService
 *
 * Provides business logic interface for case management operations.
 *
 * Version 1.0
 *
 * Date: 08-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE        AUTHOR        DESCRIPTION
 * -------------------------------------------------------------
 * 04/07/2025        Nguyễn Bảo Kha        Create
 */

public interface ICaseService {
    List<EvidentResponse<?>> getEvidences(String caseId);

    CaseListResponse getListCase(int page, int pageSize, String search);

    /**
     * Retrieves a case by its unique identifier
     * @param caseId The unique identifier of the case
     * @return Case entity
     */
    CaseDetailResponse getCaseById(String caseId);

    /**
     * Retrieves paginated list of cases with optional search functionality
     * @param page Page number (0-based)
     * @param pageSize Number of items per page
     * @param search Optional search term
     * @return CaseListDto containing paginated case data
     */
    CaseListResponse getListCase(int page, int pageSize, String search);

    /**
     * Retrieves all evidences for a specific case
     * @param caseId The case identifier
     * @return List of evidence DTOs
     */
    List<EvidentResponse<?>> getEvidences(String caseId);

    /**
     * Retrieves assigned officers for a specific case with pagination
     * @param caseId The case identifier
     * @param pageable Pagination information
     * @return Page of UserResponseDto containing officer data
     */
    Page<UserResponseDto> getAssignedOfficers(String caseId, Pageable pageable);

    /**
     * Retrieves officer case details for a specific case with pagination
     * @param caseId The case identifier
     * @param page Page number (0-based)
     * @param pageSize Number of items per page
     * @return List of OfficerCaseDetailDto containing officer case details
     */
    List<OfficerCaseDetailResponse> getOfficerCaseDetails(String caseId, int page, int pageSize);

    /**
     * Creates a new record for a specific case
     * @param caseId The case identifier
     * @param requestDto The record creation data
     * @return RecordInfoResponseDto containing created record data
     */
    RecordInfoResponseResponse createRecord(String caseId, CreateRecordInfoRequest requestDto);

    /**
     * Retrieves suspects for a specific case with filtering options
     * @param caseId The case identifier
     * @param page the page to get
     * @param pageSize number of elements in a page
     * @param status Optional status filter
     * @param date Optional date filter
     * @return Page of suspects matching the criteria
     */
    SuspectsResponseDto getAllSuspectsByCaseId(String caseId, int page, int pageSize, String status, LocalDate date);
}
