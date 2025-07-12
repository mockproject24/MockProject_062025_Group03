package com.group3.MockProject.service;

import com.group3.MockProject.dto.request.CreateInterviewDto;
import com.group3.MockProject.dto.response.InterviewResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * IInterviewService
 * <p>
 * Interface defining interview service operations
 * <p>
 * Version 1.0
 * Date: 7/4/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/4/2025      User      Create
 * 7/10/2025     User      Update to match API spec exactly
 */
public interface IInterviewService {
    /**
     * Create new interview
     * @param caseId Case ID from URL path
     * @param dto Interview data containing all required fields
     * @param files Attached files (optional)
     * @return Created interview information
     */
    InterviewResponseDto createInterview(String caseId, CreateInterviewDto dto, List<MultipartFile> files);
}
