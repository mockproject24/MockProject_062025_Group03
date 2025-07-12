package com.group3.MockProject.service.impl;

import com.group3.MockProject.constant.CaseType;
import com.group3.MockProject.constant.SeverityType;
import com.group3.MockProject.dto.request.CreateRecordInfoRequest;
import com.group3.MockProject.dto.response.*;
import com.group3.MockProject.elasticsearch.document.EsCase;
import com.group3.MockProject.elasticsearch.service.CaseIndexService;
import com.group3.MockProject.entity.*;
import com.group3.MockProject.exception.AppException;
import com.group3.MockProject.exception.ErrorCode;
import com.group3.MockProject.mapper.CaseMapper;
import com.group3.MockProject.mapper.EvidentMapper;
import com.group3.MockProject.mapper.SuspectMapper;
import com.group3.MockProject.repository.*;
import com.group3.MockProject.service.ICaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
public class CaseServiceImpl implements ICaseService {

    private final UserRepository userRepository;
    private final RecordInfoRepository recordInfoRepository;
    private final CaseRepository caseRepository;
    private final EvidenceRepository evidenceRepository;
    private final SuspectRepository suspectRepository;
    private final SuspectMapper suspectMapper;

    private final CaseIndexService caseIndexService;
    private final CaseMapper caseMapper;
    private final EvidentRepository evidentRepository;
    private final EvidentMapper mapper;
    /**
     * Retrieves a case by its unique identifier
     * @param caseId The unique identifier of the case
     * @return Case entity
     * @throws RuntimeException if case is not found
     */
    @Override
    public CaseDetailResponse getCaseById(String caseId) {
        return convertToCaseDetailResponse(caseRepository.findById(caseId)
                .orElseThrow(() -> new AppException(ErrorCode.CASE_NOT_EXISTED)));
    }

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
    @Override
    public CaseListResponse getListCase(int page, int pageSize, String search, SeverityType severitType, CaseType caseType, LocalDateTime date) {
        SearchHits<EsCase> searchHits = caseIndexService.searchCases(search, page, pageSize, severitType, caseType, date);

        List<CaseResponse> caseDtos = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(caseMapper::toDto)
                .toList();

        return CaseListResponse.builder()
                .page(page + 1)
                .pageSize(pageSize)
                .total(searchHits.getTotalHits())
                .data(caseDtos)
                .build();
    }

    /**
     * Retrieves case metadata including all available case types and severities
     * @return CaseListMeta containing lists of case types and severities
     */
    @Override
    public CaseListMetaResponse getCaseMeta() {
        List<MetaDto> caseTypes = Arrays.stream(CaseType.values())
                .map(caseType -> MetaDto.builder()
                        .key(caseType.name())
                        .label(caseType.getLabel())
                        .build())
                .collect(Collectors.toList());

        List<MetaDto> severities = Arrays.stream(SeverityType.values())
                .map(severity -> MetaDto.builder()
                        .key(severity.name())
                        .label(severity.getLabel())
                        .build())
                .collect(Collectors.toList());

        return CaseListMetaResponse.builder()
                .caseTypes(caseTypes)
                .severities(severities)
                .build();
    }

    /**
     * Retrieves all evidences for a specific case
     * @param caseId The case identifier
     * @return List of evidence DTOs
     */
    /**
     * Retrieves a list of evidences associated with a specific case.
     *
     * @param caseId the unique identifier of the case
     * @return a list of EvidentDto objects containing evidence details
     * @throws MockProjectException if the case is not found
     */
    @Override
    public List<EvidentResponse<?>> getEvidences(String caseId) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new AppException(ErrorCode.CASE_NOT_EXISTED));

        List<Evidence> evidences = evidentRepository.findByCaseEntity(caseEntity);

        List<EvidentResponse<?>> evidentResponses = new ArrayList<>();

        for (Evidence evidence : evidences) {
            EvidentResponse evidenceDto = mapper.toDto(evidence);
            if (evidence.getDigitalInvest() != null) {
                evidenceDto.setInvestigationDetail(new InvestigationDetail<DigitalInvestResponse>(
                        "DigitalInvest",
                        mapper.toDto(evidence.getDigitalInvest())
                ));
            } else if (evidence.getFinancialInvest() != null) {
                evidenceDto.setInvestigationDetail(new InvestigationDetail<>(
                        "FinancialInvest",
                        mapper.toDto(evidence.getFinancialInvest())
                ));
            } else if (evidence.getForensicInvest() != null) {
                evidenceDto.setInvestigationDetail(new InvestigationDetail<>(
                        "ForensicInvest",
                        mapper.toDto(evidence.getForensicInvest())
                ));
            } else if (evidence.getPhysicalInvest() != null) {
                evidenceDto.setInvestigationDetail(new InvestigationDetail<>(
                        "PhysicalInvest",
                        mapper.toDto(evidence.getPhysicalInvest())
                ));
            }

            evidenceDto.setRecordInfo(evidence.getRecordInfos().stream().map(mapper::toDto).collect(Collectors.toSet()));
            evidenceDto.setMeasureSurvey(evidence.getMeasureSurveys().stream().map(mapper::toDto).collect(Collectors.toSet()));

            evidentResponses.add(evidenceDto);
        }

        return evidentResponses;
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
                    user.getFullName(),
                    user.getAvatarUrl(),
                    null, // email removed from User entity
                    user.getPhoneNumber(),
                    user.getRole() != null ? user.getRole().getRoleId() : null
            ));
        } catch (Exception e) {
            throw new AppException(ErrorCode.DATABASE_ERROR, "Error retrieving assigned officers: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves officer case details for a specific case with pagination
     * @param caseId The case identifier
     * @param page Page number (0-based)
     * @param pageSize Number of items per page
     * @return List of OfficerCaseDetailDto containing officer case details
     */
    @Override
    public List<OfficerCaseDetailResponse> getOfficerCaseDetails(String caseId, int page, int pageSize) {
        try {
            // Verify case exists
            CaseDetailResponse caseEntity = getCaseById(caseId);
            
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<User> users = userRepository.findOfficersByCaseId(caseId, pageable);
            
            return users.getContent().stream()
                    .map(user -> convertToOfficerCaseDetailDto(user, caseEntity))
                    .toList();
        } catch (Exception e) {
            throw new AppException(ErrorCode.DATABASE_ERROR, "Error retrieving officer case details: " + e.getMessage(), e);
        }
    }

    /**
     * Converts User entity to OfficerCaseDetailDto
     * @param user The user entity
     * @param caseEntity The case entity
     * @return OfficerCaseDetailDto with officer case details
     */
    private OfficerCaseDetailResponse convertToOfficerCaseDetailDto(User user, CaseDetailResponse caseEntity) {
        return OfficerCaseDetailResponse.builder()
                .officerId(user.getUsername())
                .fullName(user.getFullName())
                .presentStatus(user.getStatus() != null ? user.getStatus().getLabel() : "Active")
                .role(user.getRole() != null ? user.getRole().getRoleId() : "Officer")
                .phoneNumber(user.getPhoneNumber())
                .zone("Sector 5, District 2") // Default zone - can be customized based on business logic
                .description("Case assignment for " + caseEntity.getCaseName())
                .medicalInfo("No medical information reported")
                .supportDetails("Standard support unit assigned")
                .assignmentDate(user.getDateAttended() != null ? user.getDateAttended() : caseEntity.getCreateAt())
                .build();
    }

    /**
     * Creates a new record for a specific case
     * @param caseId The case identifier
     * @param requestDto The record creation data
     * @return RecordInfoResponseDto containing created record data
     * @throws RuntimeException if case is not found or creation fails
     */
    @Override
    public RecordInfoResponseResponse createRecord(String caseId, CreateRecordInfoRequest requestDto) {
        try {
            Case caseEntity = caseRepository.findById(caseId)
                    .orElseThrow(() -> new AppException(ErrorCode.CASE_NOT_EXISTED));

            Evidence evidence = null;
//            if (requestDto.getEvidenceId() != null) {
//                evidence = evidenceRepository.findById(requestDto.getEvidenceId())
//                        .orElseThrow(() -> new RuntimeException("Evidence not found: " + requestDto.getEvidenceId()));
//            }else{
//                 evidence = new Evidence();
//                 evidence.setCaseEntity(caseEntity);
//                 evidenceRepository.save(evidence);
//            }

            RecordInfo record = new RecordInfo();
            record.setTypeName(requestDto.getTypeName());
            record.setSource(requestDto.getSource());
            record.setDateCollected(requestDto.getDateCollected() != null ?
                    requestDto.getDateCollected() : LocalDateTime.now());
            record.setSummary(requestDto.getSummary());
            record.setDeleted(requestDto.getIsDeleted() != null ? requestDto.getIsDeleted() : false);
            record.setEvidence(null); // Set to null as evidence handling is not implemented

            RecordInfo saved = recordInfoRepository.saveAndFlush(record);

            RecordInfoResponseResponse responseDto = new RecordInfoResponseResponse();
            responseDto.setRecordInfoId(saved.getRecordInfoId());
            responseDto.setTypeName(saved.getTypeName());
            responseDto.setSource(saved.getSource());
            responseDto.setDateCollected(saved.getDateCollected());
            responseDto.setSummary(saved.getSummary());
            responseDto.setIsDeleted(saved.isDeleted());
            responseDto.setEvidenceId(null);

            return responseDto;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AppException(ErrorCode.DATABASE_ERROR, "Error creating record: " + ex.getMessage(), ex);
        }
    }


    /**
     * Retrieves suspects for a specific case with filtering options
     * @param caseId The case identifier
     * @param page the page to get
     * @param pageSize number of elements in a page
     * @param status Optional status filter
     * @param date Optional date filter
     * @return Page of suspects matching the criteria
     */
    @Override
    public SuspectsResponseDto getAllSuspectsByCaseId(String caseId,int page, int pageSize, String status, LocalDate date) {
        try {
            Pageable pageable = PageRequest.of(page - 1, pageSize);
            LocalDateTime startOfDay = null;
            LocalDateTime endOfDay = null;

            if (date != null) {
                startOfDay = date.atStartOfDay();
                endOfDay = date.atTime(LocalTime.MAX);
            }

            boolean caseExists = caseRepository.existsById(caseId);
            if (!caseExists) throw new AppException(ErrorCode.CASE_NOT_EXISTED);

            Page<Suspect> suspectsPage =  suspectRepository.findByCaseIdAndStatusAndCatchTime(
                    caseId, status, date, startOfDay, endOfDay, pageable);

            return SuspectsResponseDto.builder()
                    .page(page)
                    .pageSize(pageSize)
                    .total(suspectsPage.getTotalElements())
                    .totalPages(suspectsPage.getTotalPages())
                    .suspects(suspectsPage.getContent().stream().map(suspectMapper::toSuspectResponse).toList())
                    .build();
        } catch (Exception e) {
            throw new AppException(ErrorCode.DATABASE_ERROR, "Error retrieving suspects: " + e.getMessage(), e);
        }
    }

    /**
     * Converts Case entity to CaseDto for API response
     * @param caseEntity The case entity to convert
     * @return CaseDto containing formatted case data
     */
    private CaseDetailResponse convertToCaseDetailResponse(Case caseEntity) {
        return CaseDetailResponse.builder()
                .caseId(caseEntity.getCaseId())
                .caseName("#" + caseEntity.getCaseId()) // Use caseId since caseNumber doesn't exist
                .typeCase(caseEntity.getTypeCase().getLabel())
                .severity(caseEntity.getSeverity().getLabel())
                .status(caseEntity.getStatus().getLabel())
                .createAt(caseEntity.getCreateAt())
                .summary(caseEntity.getSummary())
                .warrants(caseEntity.getWarrants())
                .evidences(caseEntity.getEvidences())
                .isDeleted(caseEntity.isDeleted())
                .suspects(caseEntity.getSuspects())
                .build();
    }
}
