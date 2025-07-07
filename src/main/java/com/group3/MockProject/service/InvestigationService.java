package com.group3.MockProject.service;


import com.group3.MockProject.dto.response.InvestigationPlanResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InvestigationService {
    Page<InvestigationPlanResponseDto> getInvestigations(Pageable pageable);
}
