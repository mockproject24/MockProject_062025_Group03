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
 * 7/4/2025      User      Create
 * 7/10/2025     User      Update to match API spec exactly
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class InterviewServiceImpl implements IInterviewService {
    private final InterviewRepository interviewRepository;
    private final QuestionRepository questionRepository;
    private final InterviewFileRepository interviewFileRepository;
    private final UserRepository userRepository;
    private final SuspectRepository suspectRepository;
    private final VictimRepository victimRepository;
    private final WitnessRepository witnessRepository;
    private final CaseRepository caseRepository;
    private final InterviewMapper interviewMapper;

    @Value("${spring.upload-file.base-uri}")
    private String uploadBasePath;

    @Override
    @Transactional
    public InterviewResponse createInterview(String caseId, CreateInterviewRequest request, List<MultipartFile> files) {
        log.info("Creating interview for case: {}", caseId);

        // Step 1: Validate basic interview data
        validateInterviewData(request);

        // Step 2: Check if case exists
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new AppException(ErrorCode.CASE_NOT_EXISTED));

        // Step 3: Find interviewer user
        User interviewer = userRepository.findById(request.getInterviewerId())
                .orElseThrow(() -> new AppException(ErrorCode.INTERVIEWER_NOT_FOUND));

        // Step 4: Check for time conflicts with existing interviews
        LocalDateTime startTime = request.getStartTime().atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endTime = request.getEndTime().atOffset(ZoneOffset.UTC).toLocalDateTime();
        checkTimeConflict(request.getInterviewerId(), startTime, endTime);

        // Step 5: Find interviewee based on type and ID card
        Object interviewee = findInterviewee(request.getIntervieweeType(), request.getIntervieweeIdCard());

        // Step 6: Create interview entity using mapper
        Interview interview = interviewMapper.createInterviewEntity(request, caseEntity, interviewer, interviewee);
        Interview savedInterview = interviewRepository.save(interview);

        // Step 7: Create questions using mapper
        List<Question> questions = interviewMapper.createQuestions(request.getQuesAndAns(), savedInterview, interviewer);
        questionRepository.saveAll(questions);

        // Step 8: Upload files if provided
        List<String> uploadedFileNames = uploadFiles(files, savedInterview);

        // Step 9: Build response using mapper
        return interviewMapper.buildInterviewResponse(savedInterview, interviewer, interviewee, questions.size(), uploadedFileNames);
    }

    // Validates interview request data
    private void validateInterviewData(CreateInterviewRequest request) {
        // Check time range: start time must be before end time
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new AppException(ErrorCode.INVALID_TIME_RANGE);
        }
    }

    // Checks for interviewer schedule conflicts
    private void checkTimeConflict(String interviewerId, LocalDateTime startTime, LocalDateTime endTime) {
        // Query for interviews with overlapping time slots for same interviewer
        List<Interview> conflictInterviews = interviewRepository.findConflictingInterviews(
                interviewerId, startTime, endTime);

        if (!conflictInterviews.isEmpty()) {
            throw new AppException(ErrorCode.INTERVIEW_SCHEDULING_CONFLICT);
        }
    }

    // Finds interviewee entity based on type and ID card number
    private Object findInterviewee(String intervieweeType, String intervieweeIdCard) {
        Long idCard = Long.parseLong(intervieweeIdCard);

        return switch (intervieweeType.toUpperCase()) {
            case "SUSPECT" -> suspectRepository.findBySuspectIdCard(idCard)
                    .orElseThrow(() -> new AppException(ErrorCode.INTERVIEWEE_NOT_FOUND));
            case "VICTIM" -> victimRepository.findByVictimIdCard(idCard)
                    .orElseThrow(() -> new AppException(ErrorCode.INTERVIEWEE_NOT_FOUND));
            case "WITNESS" -> witnessRepository.findByWitnessIdCard(idCard)
                    .orElseThrow(() -> new AppException(ErrorCode.INTERVIEWEE_NOT_FOUND));
            default -> throw new AppException(ErrorCode.INVALID_INTERVIEWEE_TYPE);
        };
    }

    // Uploads files and saves file information to database
    private List<String> uploadFiles(List<MultipartFile> files, Interview interview) {
        List<String> uploadedFileNames = new ArrayList<>();

        if (files == null || files.isEmpty()) {
            return uploadedFileNames;
        }

        // Create upload directory if not exists
        Path uploadDir = Paths.get(uploadBasePath.replace("file:", ""));
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            try {
                // Generate unique filename: UUID + original extension
                String originalFileName = file.getOriginalFilename();
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String uniqueFileName = UUID.randomUUID().toString() + extension;

                // Save file to disk
                Path filePath = uploadDir.resolve(uniqueFileName);
                Files.copy(file.getInputStream(), filePath);

                // Save file information to database
                InterviewFile interviewFile = new InterviewFile();
                interviewFile.setAttachedFile(uniqueFileName);
                interviewFile.setInterview(interview);
                interviewFileRepository.save(interviewFile);

                uploadedFileNames.add(originalFileName); // Return original name for response

            } catch (IOException e) {
                log.error("File upload error: {}", e.getMessage());
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        return uploadedFileNames;
    }
}