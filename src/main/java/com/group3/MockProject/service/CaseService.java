package com.group3.MockProject.service;

import com.group3.MockProject.constant.CaseType;
import com.group3.MockProject.constant.SeverityType;
import com.group3.MockProject.dto.request.CreateRecordInfoDto;
import com.group3.MockProject.dto.response.*;
import com.group3.MockProject.entity.Case;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

public interface CaseService {

    /**
     * Retrieves a case by its unique identifier
     * @param caseId The unique identifier of the case
     * @return Case entity
     */
    Case getCaseById(String caseId);

    /**
     * Retrieves paginated list of cases with optional search functionality
     *
     * @param page        Page number (0-based)
     * @param pageSize    Number of items per page
     * @param search      Optional search term
     * @param severitType
     * @param caseType
     * @param date
     * @return CaseListDto containing paginated case data
     */
    CaseListDto getListCase(int page, int pageSize, String search, SeverityType severitType, CaseType caseType, LocalDateTime date);

    CaseListMeta getCaseMeta();
    /**
     * Retrieves all evidences for a specific case
     * @param caseId The case identifier
     * @return List of evidence DTOs
     */
    List<EvidentDto<?>> getEvidences(String caseId);

    /**
     * Retrieves assigned officers for a specific case with pagination
     * @param caseId The case identifier
     * @param pageable Pagination information
     * @return Page of UserResponseDto containing officer data
     */
    Page<UserResponseDto> getAssignedOfficers(String caseId, Pageable pageable);

    /**
     * Creates a new record for a specific case
     * @param caseId The case identifier
     * @param requestDto The record creation data
     * @return RecordInfoResponseDto containing created record data
     */
    RecordInfoResponseDto createRecord(String caseId, CreateRecordInfoDto requestDto);

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
