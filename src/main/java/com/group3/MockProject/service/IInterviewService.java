package com.group3.MockProject.service;

import com.group3.MockProject.dto.request.CreateInterviewRequest;
import com.group3.MockProject.dto.response.InterviewResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * IInterviewService
 * <p>
 * Service for managing interview operations.
 * <p>
 * Version 1.0
 * Date: 7/4/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 4/7/2025      FongFox      Create
 * 10/7/2025     FongFox      Update to match API spec exactly
 * 12/7/2025     FongFox      Update service
 */
public interface IInterviewService {
    public InterviewResponse createInterview(String caseId, CreateInterviewRequest request, List<MultipartFile> files);
}
