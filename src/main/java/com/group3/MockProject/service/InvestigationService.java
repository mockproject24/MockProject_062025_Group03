package com.group3.MockProject.service;


import com.group3.MockProject.dto.request.CreateInvestigationRequest;
import com.group3.MockProject.dto.response.InvestigationPlanResponseDto;
import com.group3.MockProject.dto.response.InvestigationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InvestigationService {
    Page<InvestigationPlanResponseDto> getInvestigations(Pageable pageable);
    
    /**
     * Create investigation with files
     * @param caseId The case identifier
     * @param request Investigation request data
     * @param files List of attached files
     * @return Investigation response with file URLs
     */
    InvestigationResponseDto createInvestigation(String caseId, CreateInvestigationRequest request, List<MultipartFile> files);
}
