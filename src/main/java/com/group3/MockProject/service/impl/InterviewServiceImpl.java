package com.group3.MockProject.service.impl;

import com.group3.MockProject.dto.request.CreateInterviewRequest;
import com.group3.MockProject.dto.request.QuestionRequest;
import com.group3.MockProject.dto.response.InterviewResponse;
import com.group3.MockProject.entity.*;
import com.group3.MockProject.exception.AppException;
import com.group3.MockProject.exception.ErrorCode;
import com.group3.MockProject.mapper.InterviewMapper;
import com.group3.MockProject.repository.*;
import com.group3.MockProject.service.IInterviewService;

import com.group3.MockProject.util.FileUploadUtil;
import com.group3.MockProject.validator.InterviewValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * InterviewServiceImpl
 *
 * Service implementation for managing interview operations.
 *
 * <p>
 * Main Features:
 * - Creates interviews for suspects, victims, or witnesses
 * - Validates interview data and checks for scheduling conflicts
 * - Manages question and answer records with trust levels
 * - Handles file uploads for interview recordings/documents
 * - Converts trust levels from letters (a,b,c) to numeric values (1.0, 0.7, 0.4)
 * Business Rules:
 * - Start time must be before end time
 * - Interviewer cannot have overlapping interview schedules
 * - Interviewee must exist in the system (by ID card number)
 * - Files are stored with UUID naming to prevent conflicts
 * - Trust levels: 'a' = 1.0 (high), 'b' = 0.7 (medium), 'c' = 0.4 (low)
 * <p>
 *
 * Version 1.0
 * Date: 7/4/2025
 *
 * <p>
 * Copyright
 * <p>
 *
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/4/2025      FongFox      Create
 * 7/10/2025     FongFox      Update to match API spec exactly
 * 7/28/2025     FongFox      Refactor: extract validation and file upload logic
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class InterviewServiceImpl implements IInterviewService {
    // Repositories
    private final InterviewRepository interviewRepository;
    private final QuestionRepository questionRepository;
    private final InterviewFileRepository interviewFileRepository;

    // Validation and utilities
    private final InterviewValidator interviewValidator;
    private final FileUploadUtil fileUploadUtil;

    // Mappers
    private final InterviewMapper interviewMapper;

    @Override
    @Transactional
    public InterviewResponse createInterview(String caseId, CreateInterviewRequest request, List<MultipartFile> files) {
        log.info("Creating interview for case: {}", caseId);

        try {
            // Step 1: Comprehensive validation using validator
            interviewValidator.validateCreateInterviewRequest(caseId, request, files);

            // Step 2: Get validated entities
            Case caseEntity = interviewValidator.getValidatedCase(caseId);
            User interviewer = interviewValidator.getValidatedInterviewer(request.getInterviewerId());
            Object interviewee = interviewValidator.getValidatedInterviewee(
                    request.getIntervieweeType(),
                    request.getIntervieweeIdCard()
            );

            // Step 3: Create interview entity using mapper
            Interview interview = interviewMapper.createInterviewEntity(request, caseEntity, interviewer, interviewee);
            Interview savedInterview = interviewRepository.save(interview);
            log.debug("Interview entity saved with ID: {}", savedInterview.getInterviewId());

            // Step 4: Create questions using mapper
            List<Question> questions = interviewMapper.createQuestions(request.getQuesAndAns(), savedInterview, interviewer);
            questionRepository.saveAll(questions);
            log.debug("Saved {} questions for interview", questions.size());

            // Step 5: Handle file uploads using utility
            List<String> uploadedFileNames = handleFileUploads(files, savedInterview);

            // Step 6: Build response using mapper
            InterviewResponse response = interviewMapper.buildInterviewResponse(
                    savedInterview, interviewer, interviewee, questions.size(), uploadedFileNames
            );

            log.info("Interview created successfully for case: {} with ID: {}", caseId, savedInterview.getInterviewId());
            return response;

        } catch (Exception e) {
            log.error("Failed to create interview for case: {}", caseId, e);
            throw e; // Re-throw to be handled by GlobalExceptionHandler
        }
    }

    /**
     * Handles file uploads for interview
     * Uses FileUploadUtil and saves file information to database
     *
     * @param files List of files to upload
     * @param interview The interview entity to associate files with
     * @return List of original filenames that were uploaded
     */
    private List<String> handleFileUploads(List<MultipartFile> files, Interview interview) {
        if (files == null || files.isEmpty()) {
            log.debug("No files to upload for interview: {}", interview.getInterviewId());
            return new ArrayList<>();
        }

        log.info("Processing {} files for interview: {}", files.size(), interview.getInterviewId());

        List<String> originalFileNames = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                log.debug("Skipping empty file");
                continue;
            }

            try {
                // Upload file using utility
                String uniqueFileName = fileUploadUtil.uploadSingleFile(file);

                // Save file information to database
                InterviewFile interviewFile = new InterviewFile();
                interviewFile.setAttachedFile(uniqueFileName);
                interviewFile.setInterview(interview);
                interviewFileRepository.save(interviewFile);

                // Keep track of original filename for response
                originalFileNames.add(file.getOriginalFilename());

                log.debug("File processed successfully: {} -> {}", file.getOriginalFilename(), uniqueFileName);

            } catch (Exception e) {
                log.error("Failed to process file: {}", file.getOriginalFilename(), e);
                // Continue processing other files, but re-throw the exception
                throw e;
            }
        }

        log.info("Successfully processed {} files for interview: {}", originalFileNames.size(), interview.getInterviewId());
        return originalFileNames;
    }
}