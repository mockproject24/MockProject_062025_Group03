package com.group3.MockProject.service;

import com.group3.MockProject.entity.Suspect;
import com.group3.MockProject.dto.request.CreateRecordInfoDto;
import com.group3.MockProject.dto.response.RecordInfoResponseDto;
import com.group3.MockProject.dto.response.UserResponseDto;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.RecordInfo;
import com.group3.MockProject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
 * 04/07/2025        Nguyễn Bảo Kha, DQMinh        Create
 */

public interface CaseService {
    Page<Suspect> getAllSuspectsByCaseId(String caseId, Pageable pageable, String status, LocalDate date);
    Page<UserResponseDto> getAssignedOfficers(String caseId, Pageable pageable);
    RecordInfoResponseDto createRecord(String caseId, CreateRecordInfoDto requestDto);
    Case getCaseById(String caseId);
}
