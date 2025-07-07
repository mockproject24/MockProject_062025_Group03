package com.group3.MockProject.service.impl;

import com.group3.MockProject.dto.response.InvestigationPlanResponseDto;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.InvestigationPlan;
import com.group3.MockProject.repository.CaseRepository;
import com.group3.MockProject.repository.InvestigationPlanRepository;
import com.group3.MockProject.service.InvestigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * InvestigationServiceImpl
 *
 * Provides business logic for managing  details.
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

@Service
public class InvestigationServiceImpl implements InvestigationService {
    @Autowired
    private InvestigationPlanRepository investigationPlanRepository;

    @Autowired
    private CaseRepository caseRepository;

    @Override
    public Page<InvestigationPlanResponseDto> getInvestigations(Pageable pageable) {
        Page<InvestigationPlan> plansPage = investigationPlanRepository.findAllActivePlans(pageable);
        return plansPage.map(plan -> {
            // Fetch related Case
            Case caseEntity = caseRepository.findById(plan.getCaseEntity().getCaseId())
                    .orElseThrow(() -> new RuntimeException("Case not found for plan: " + plan.getInvestigationPlanId()));

            return new InvestigationPlanResponseDto(
                    plan.getInvestigationPlanId(),
                    caseEntity.getCaseId(),
                    caseEntity.getSeverity(),
                    plan.getDeadlineDate(),
                    plan.getResult(),
                    plan.getStatus(),
                    plan.getCreatedAt(),
                    plan.getPlanContent(),
                    plan.isDeleted()
            );
        });
    }
}
