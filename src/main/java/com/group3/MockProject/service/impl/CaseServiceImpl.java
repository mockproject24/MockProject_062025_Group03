package com.group3.MockProject.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.group3.MockProject.dto.request.CreateRecordInfoDto;
import com.group3.MockProject.dto.response.CaseDto;
import com.group3.MockProject.dto.response.CaseListDto;
import com.group3.MockProject.dto.response.EvidentDto;
import com.group3.MockProject.dto.response.RecordInfoResponseDto;
import com.group3.MockProject.dto.response.UserResponseDto;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Evidence;
import com.group3.MockProject.entity.RecordInfo;
import com.group3.MockProject.entity.Suspect;
import com.group3.MockProject.entity.User;
import com.group3.MockProject.repository.CaseRepository;
import com.group3.MockProject.repository.EvidenceRepository;
import com.group3.MockProject.repository.RecordInfoRepository;
import com.group3.MockProject.repository.SuspectRepository;
import com.group3.MockProject.repository.UserRepository;
import com.group3.MockProject.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * CaseServiceImpl
 *
 * Provides business logic implementation for case management operations.
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
@Service
@RequiredArgsConstructor
public class CaseServiceImpl implements CaseService {

    private final UserRepository userRepository;
    private final RecordInfoRepository recordInfoRepository;
    private final CaseRepository caseRepository;
    private final EvidenceRepository evidenceRepository;
    private final SuspectRepository suspectRepository;

    /**
     * Retrieves a case by its unique identifier
     * @param caseId The unique identifier of the case
     * @return Case entity
     * @throws RuntimeException if case is not found
     */
    @Override
    public Case getCaseById(String caseId) {
        return caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found: " + caseId));
    }

    /**
     * Retrieves paginated list of cases with optional search functionality
     * @param page Page number (0-based)
     * @param pageSize Number of items per page
     * @param search Optional search term
     * @return CaseListDto containing paginated case data
     */
    @Override
    public CaseListDto getListCase(int page, int pageSize, String search) {
        try {
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<Case> casePage;

            if (search != null && !search.trim().isEmpty()) {
                // TODO: Implement search functionality when search repository method is available
                casePage = caseRepository.findAll(pageable);
            } else {
                casePage = caseRepository.findAll(pageable);
            }

            List<CaseDto> caseDtos = casePage.getContent().stream()
                    .map(this::convertToCaseDto)
                    .toList();

            return CaseListDto.builder()
                    .page(page + 1) // Convert to 1-based for response
                    .pageSize(pageSize)
                    .total(casePage.getTotalElements())
                    .data(caseDtos)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving cases: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all evidences for a specific case
     * @param caseId The case identifier
     * @return List of evidence DTOs
     */
    @Override
    public List<EvidentDto<?>> getEvidences(String caseId) {
        try {
            // TODO: Implement evidence retrieval when evidence repository methods are available
            return new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving evidences: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves assigned officers for a specific case with pagination
     * @param caseId The case identifier
     * @param pageable Pagination information
     * @return Page of UserResponseDto containing officer data
     */
    @Override
    public Page<UserResponseDto> getAssignedOfficers(String caseId, Pageable pageable) {
        try {
            Page<User> users = userRepository.findOfficersByCaseId(caseId, pageable);
            return users.map(user -> new UserResponseDto(
                    user.getUsername(),
                    user.getFullname(),
                    user.getAvatarUrl(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getRole() != null ? user.getRole().getRoleId() : null
            ));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving assigned officers: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a new record for a specific case
     * @param caseId The case identifier
     * @param requestDto The record creation data
     * @return RecordInfoResponseDto containing created record data
     * @throws RuntimeException if case is not found or creation fails
     */
    @Override
    public RecordInfoResponseDto createRecord(String caseId, CreateRecordInfoDto requestDto) {
        try {
            Case caseEntity = caseRepository.findById(caseId)
                    .orElseThrow(() -> new RuntimeException("Case not found: " + caseId));

            RecordInfo record = new RecordInfo();
            record.setTypeName(requestDto.getTypeName());
            record.setSource(requestDto.getSource());
            record.setDateCollected(requestDto.getDateCollected() != null ?
                    requestDto.getDateCollected() : LocalDateTime.now());
            record.setSummary(requestDto.getSummary());
            record.setDeleted(requestDto.getIsDeleted() != null ? requestDto.getIsDeleted() : false);
            record.setEvidence(null); // Set to null as evidence handling is not implemented

            RecordInfo saved = recordInfoRepository.saveAndFlush(record);

            RecordInfoResponseDto responseDto = new RecordInfoResponseDto();
            responseDto.setRecordInfoId(saved.getRecordInfoId());
            responseDto.setTypeName(saved.getTypeName());
            responseDto.setSource(saved.getSource());
            responseDto.setDateCollected(saved.getDateCollected());
            responseDto.setSummary(saved.getSummary());
            responseDto.setIsDeleted(saved.isDeleted());
            responseDto.setEvidenceId(null);

            return responseDto;
        } catch (Exception ex) {
            throw new RuntimeException("Error creating record: " + ex.getMessage(), ex);
        }
    }

    /**
     * Retrieves suspects for a specific case with filtering options
     * @param caseId The case identifier
     * @param pageable Pagination information
     * @param status Optional status filter
     * @param date Optional date filter
     * @return Page of suspects matching the criteria
     */
    @Override
    public Page<Suspect> getAllSuspectsByCaseId(String caseId, Pageable pageable, String status, LocalDate date) {
        try {
            LocalDateTime startOfDay = null;
            LocalDateTime endOfDay = null;

            if (date != null) {
                startOfDay = date.atStartOfDay();
                endOfDay = date.atTime(LocalTime.MAX);
            }

            return suspectRepository.findByCaseIdAndStatusAndCatchTime(
                    caseId, status, date, startOfDay, endOfDay, pageable);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving suspects: " + e.getMessage(), e);
        }
    }

    /**
     * Converts Case entity to CaseDto for API response
     * @param caseEntity The case entity to convert
     * @return CaseDto containing formatted case data
     */
    private CaseDto convertToCaseDto(Case caseEntity) {
        return CaseDto.builder()
                .caseId(caseEntity.getCaseId())
                .caseNumber(caseEntity.getCaseNumber() != null ? caseEntity.getCaseNumber() : "#" + caseEntity.getCaseId())
                .typeCase(caseEntity.getTypeCase())
                .severity(caseEntity.getSeverity())
                .status(caseEntity.getStatus())
                .createdAt(caseEntity.getCreateAt())
                .receivingUnit("Local PD – Investigation Division") // Default value since field doesn't exist
                .location("Not specified") // Default value since field doesn't exist
                .build();
    }
}
