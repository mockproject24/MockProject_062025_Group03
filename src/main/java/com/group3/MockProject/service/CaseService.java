package com.group3.MockProject.service;

import com.group3.MockProject.dto.request.CreateRecordInfoDto;
import com.group3.MockProject.dto.response.RecordInfoResponseDto;
import com.group3.MockProject.dto.response.UserResponseDto;
import com.group3.MockProject.entity.RecordInfo;
import com.group3.MockProject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * CaseService
 *
 * Provides business logic for managing details.
 *
 * Version 1.0
 *
 * Date: 04/07/2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE        AUTHOR        DESCRIPTION
 * -------------------------------------------------------------
 * 04/07/2025        Nguyễn Bảo Kha        Create
 */

public interface CaseService {
    Page<UserResponseDto> getAssignedOfficers(String caseId, Pageable pageable);

    RecordInfoResponseDto createRecord(String caseId, CreateRecordInfoDto requestDto);
}
