package com.group3.MockProject.service;


import com.group3.MockProject.dto.request.CreateInvestigationRequest;
import com.group3.MockProject.dto.response.InvestigationPlanResponse;
import com.group3.MockProject.dto.response.InvestigationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InvestigationService {
    Page<InvestigationPlanResponse> getInvestigations(Pageable pageable);
    
    /**
     * Create investigation with files
     * @param caseId The case identifier
     * @param request Investigation request data
     * @param files List of attached files
     * @return Investigation response with file URLs
     */
    InvestigationResponse createInvestigation(String caseId, CreateInvestigationRequest request, List<MultipartFile> files);
}
