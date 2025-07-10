package com.group3.MockProject.service;

import com.group3.MockProject.dto.request.CreateInterviewDto;
import com.group3.MockProject.dto.response.InterviewResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * InterviewService
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
 */
public interface InterviewService {
    /**
     * Create new interview
     * @param caseId Case ID
     * @param suspectId Suspect ID
     * @param dto Interview data
     * @param files Attached files
     * @return Created interview information
     */
    InterviewResponseDto createInterview(String caseId, String suspectId, CreateInterviewDto dto, List<MultipartFile> files);
}
