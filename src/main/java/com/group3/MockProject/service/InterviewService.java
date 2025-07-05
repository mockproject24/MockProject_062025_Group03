package com.group3.MockProject.service;

import com.group3.MockProject.dto.request.CreateInterviewDto;
import org.apache.coyote.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * InterviewService
 * <p>
 * Provides business logic for managing employment details.
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
    public void createInterview(String caseId, String suspectId, CreateInterviewDto dto, List<MultipartFile> files) throws BadRequestException;
}
