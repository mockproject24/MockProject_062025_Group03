package com.group3.MockProject.service.impl;

import com.group3.MockProject.dto.request.ApiResponse;
import com.group3.MockProject.dto.request.PaginatedResponse;
import com.group3.MockProject.dto.response.CaseResponseDto;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Report;
import com.group3.MockProject.entity.User;
import com.group3.MockProject.repository.CaseRepository;
import com.group3.MockProject.repository.ReportRepository;
import com.group3.MockProject.repository.UserRepository;
import com.group3.MockProject.service.CaseService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Arrest class
 * <p>
 * Provides business logic for managing details.
 * <p>
 * Version 1.0
 * <p>
 * Date: 01/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE        AUTHOR        DESCRIPTION
 * -------------------------------------------------------------
 * 04/07/2025        Bui Van Trang        Create
 */

@Service
@RequiredArgsConstructor
public class CaseServiceImpl implements CaseService {
    @Autowired
    private CaseRepository caseRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ApiResponse<PaginatedResponse<CaseResponseDto>> getAllCase(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Case> casesPage = caseRepository.findAll(pageable);

        List<CaseResponseDto> data = casesPage.getContent().stream().map(caseEntity -> {
            // Find the first non-deleted report for the case
            Report report = (Report) reportRepository
                    .findFirstByCaseEntity_CaseIdAndIsDeletedFalse(caseEntity.getCaseId())
                    .orElse(null);

            // Find user based on report
            String reporterFullname = null;
            String location = null;

            if (report != null) {
                location = report.getCaseLocation();
                if (report.getUser() != null) {
                    User user = userRepository.findById(report.getUser().getUsername()).orElse(null);
                    if (user != null) {
                        reporterFullname = user.getFullname();
                    }
                }
            }

            return CaseResponseDto.builder()
                    .caseId(caseEntity.getCaseId())
                    .caseNumber("#" + caseEntity.getCaseNumber())
                    .typeCase(caseEntity.getTypeCase())
                    .severity(caseEntity.getSeverity())
                    .createdAt(caseEntity.getCreateAt())
                    .status(caseEntity.getStatus())
                    .reporterFullname(reporterFullname)
                    .location(location)
                    .build();
        }).toList();

        PaginatedResponse<CaseResponseDto> paginatedResult = PaginatedResponse.<CaseResponseDto>builder()
                .page(page)
                .pageSize(pageSize)
                .total(casesPage.getTotalElements())
                .data(data)
                .build();

        return ApiResponse.<PaginatedResponse<CaseResponseDto>>builder()
                .code(200)
                .message("Success")
                .result(paginatedResult)
                .build();
    }
}