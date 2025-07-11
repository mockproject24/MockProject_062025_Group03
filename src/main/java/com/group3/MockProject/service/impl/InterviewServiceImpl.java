package com.group3.MockProject.service.impl;

import com.group3.MockProject.dto.request.CreateInterviewDto;
import com.group3.MockProject.dto.request.QuestionDto;
import com.group3.MockProject.dto.response.InterviewResponseDto;
import com.group3.MockProject.entity.*;
import com.group3.MockProject.mapper.InterviewMapper;
import com.group3.MockProject.repository.*;
import com.group3.MockProject.service.InterviewService;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * InterviewServiceImpl
 *
 * Service implementation for managing interviews in the case management system.
 * Handles interview creation with questions, file uploads, and participant management.
 *
 * Version 1.0
 * Date: 7/4/2025
 *
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 4/7/2025      FongFox      Create
 * 10/7/2025     FongFox      Update to match API spec exactly
 * 11/7/2025     FongFox      Fix and organize code
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    // ================================
    // DEPENDENCIES
    // ================================

    private final InterviewRepository interviewRepository;
    private final InterviewFileRepository interviewFileRepository;
    private final UserRepository userRepository;
    private final SuspectRepository suspectRepository;
    private final VictimRepository victimRepository;
    private final WitnessRepository witnessRepository;
    private final QuestionRepository questionRepository;
    private final InterviewMapper interviewMapper;

    // ================================
    // CONFIGURATION
    // ================================

    @Value("${spring.upload-file.base-uri}")
    private String baseUri;

    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList(
            "mp4", "mp3", "wav", "avi", "mov", "pdf", "doc", "docx", "jpg", "png"
    );
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

    // ================================
    // PUBLIC API METHODS
    // ================================

    @Override
    @Transactional
    public InterviewResponseDto createInterview(String caseId, CreateInterviewDto dto, List<MultipartFile> files) {
        log.info("Starting interview creation process for case: {}", caseId);

        // Step 1: Validate input data
        validateInterviewData(dto);

        // Step 2: Find interviewer by ID
        User interviewer = findInterviewerById(dto.getInterviewerId());

        // Step 3: Upload files if provided
        List<String> uploadedFilePaths = uploadFilesIfProvided(files);

        // Step 4: Create and save interview entity
        Interview savedInterview = createAndSaveInterview(dto, interviewer);

        // Step 5: Create and save questions
        saveQuestionsForInterview(dto.getQuesAndAns(), savedInterview, interviewer);

        // Step 6: Create and save interview files
        saveFilesForInterview(uploadedFilePaths, savedInterview);

        // Step 7: Convert to response DTO
        InterviewResponseDto responseDto = interviewMapper.convertToResponseDto(savedInterview);

        log.info("Interview created successfully with ID: {}", savedInterview.getInterviewId());
        return responseDto;
    }

    // ================================
    // CORE BUSINESS LOGIC METHODS
    // ================================

    /**
     * Create and save interview entity with interviewer and interviewee
     */
    private Interview createAndSaveInterview(CreateInterviewDto dto, User interviewer) {
        // Create interview entity
        Interview interview = interviewMapper.convertToInterviewEntity(dto, interviewer);

        // Set interviewee based on type
        setIntervieweeByType(interview, dto.getIntervieweeType(), dto.getIntervieweeIdCard());

        // Save and return
        Interview savedInterview = interviewRepository.save(interview);
        log.info("Interview saved with ID: {}", savedInterview.getInterviewId());

        return savedInterview;
    }

    /**
     * Create and save questions for the interview
     */
    private void saveQuestionsForInterview(List<QuestionDto> questionDtos, Interview interview, User interviewer) {
        if (questionDtos == null || questionDtos.isEmpty()) {
            log.info("No questions to save for interview: {}", interview.getInterviewId());
            return;
        }

        List<Question> questions = interviewMapper.convertToQuestionEntities(questionDtos, interview, interviewer);

        // Ensure back references are set
        questions.forEach(question -> question.setInterview(interview));

        List<Question> savedQuestions = questionRepository.saveAll(questions);
        interview.setQuestions(savedQuestions);

        log.info("Saved {} questions for interview: {}", savedQuestions.size(), interview.getInterviewId());
    }

    /**
     * Create and save interview files
     */
    private void saveFilesForInterview(List<String> filePaths, Interview interview) {
        if (filePaths == null || filePaths.isEmpty()) {
            log.info("No files to save for interview: {}", interview.getInterviewId());
            return;
        }

        List<InterviewFile> interviewFiles = interviewMapper.convertToInterviewFileEntities(filePaths, interview);

        // Ensure back references are set
        interviewFiles.forEach(file -> file.setInterview(interview));

        List<InterviewFile> savedFiles = interviewFileRepository.saveAll(interviewFiles);
        interview.setInterviewFileList(savedFiles);

        log.info("Saved {} files for interview: {}", savedFiles.size(), interview.getInterviewId());
    }

    // ================================
    // VALIDATION METHODS
    // ================================

    /**
     * Validate all interview input data
     */
    private void validateInterviewData(CreateInterviewDto dto) {
        log.debug("Validating interview data");

        if (dto == null) {
            throw new IllegalArgumentException("Interview data is required");
        }

        validateTimeFields(dto);
        validateRequiredFields(dto);
        validateQuestionsList(dto.getQuesAndAns());
    }

    /**
     * Validate start and end time
     */
    private void validateTimeFields(CreateInterviewDto dto) {
        if (dto.getStartTime() == null) {
            throw new IllegalArgumentException("Start time is required");
        }
        if (dto.getEndTime() == null) {
            throw new IllegalArgumentException("End time is required");
        }
        if (dto.getEndTime().isBefore(dto.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }

    /**
     * Validate required fields
     */
    private void validateRequiredFields(CreateInterviewDto dto) {
        if (isStringEmpty(dto.getLocation())) {
            throw new IllegalArgumentException("Location is required");
        }
        if (isStringEmpty(dto.getInterviewerId())) {
            throw new IllegalArgumentException("Interviewer ID is required");
        }
        if (isStringEmpty(dto.getIntervieweeType())) {
            throw new IllegalArgumentException("Interviewee type is required");
        }
        if (!isValidIntervieweeType(dto.getIntervieweeType())) {
            throw new IllegalArgumentException("Interviewee type must be SUSPECT, VICTIM, or WITNESS");
        }
        if (isStringEmpty(dto.getIntervieweeIdCard())) {
            throw new IllegalArgumentException("Interviewee ID card is required");
        }
        if (!isValidIdCardFormat(dto.getIntervieweeIdCard())) {
            throw new IllegalArgumentException("Interviewee ID card must be 9-12 digits");
        }
    }

    /**
     * Validate questions list
     */
    private void validateQuestionsList(List<QuestionDto> questions) {
        if (questions == null || questions.isEmpty()) {
            throw new IllegalArgumentException("At least one question is required");
        }

        for (int i = 0; i < questions.size(); i++) {
            validateSingleQuestion(questions.get(i), i + 1);
        }
    }

    /**
     * Validate single question
     */
    private void validateSingleQuestion(QuestionDto question, int questionNumber) {
        if (question == null) {
            throw new IllegalArgumentException("Question " + questionNumber + " cannot be null");
        }
        if (isStringEmpty(question.getQuestion())) {
            throw new IllegalArgumentException("Question " + questionNumber + ": Question text is required");
        }
        if (isStringEmpty(question.getAnswer())) {
            throw new IllegalArgumentException("Question " + questionNumber + ": Answer is required");
        }
        if (!isValidLevelOfTrust(question.getLevelOfTrust())) {
            throw new IllegalArgumentException("Question " + questionNumber + ": Level of trust must be 'a', 'b', or 'c'");
        }
    }

    // ================================
    // HELPER METHODS FOR VALIDATION
    // ================================

    private boolean isStringEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean isValidIntervieweeType(String type) {
        return type != null && (type.equals("SUSPECT") || type.equals("VICTIM") || type.equals("WITNESS"));
    }

    private boolean isValidIdCardFormat(String idCard) {
        return idCard != null && idCard.matches("^\\d{9,12}$");
    }

    private boolean isValidLevelOfTrust(String level) {
        return level != null && level.matches("^[aAbBcC]$");
    }

    // ================================
    // ENTITY FINDER METHODS
    // ================================

    /**
     * Find interviewer user by ID
     */
    private User findInterviewerById(String interviewerId) {
        return userRepository.findById(interviewerId)
                .orElseThrow(() -> new EntityNotFoundException("Interviewer not found with ID: " + interviewerId));
    }

    // ================================
    // INTERVIEWEE MANAGEMENT METHODS
    // ================================

    /**
     * Set interviewee based on type and ID card
     */
    private void setIntervieweeByType(Interview interview, String intervieweeType, String intervieweeIdCard) {
        Long idCardNumber = Long.parseLong(intervieweeIdCard);
        String type = intervieweeType.toUpperCase();

        switch (type) {
            case "SUSPECT":
                setSuspectAsInterviewee(interview, idCardNumber);
                break;
            case "VICTIM":
                setVictimAsInterviewee(interview, idCardNumber);
                break;
            case "WITNESS":
                setWitnessAsInterviewee(interview, idCardNumber);
                break;
            default:
                throw new IllegalArgumentException("Invalid interviewee type: " + intervieweeType);
        }
    }

    /**
     * Set suspect as interviewee
     */
    private void setSuspectAsInterviewee(Interview interview, Long idCard) {
        Suspect suspect = suspectRepository.findAll().stream()
                .filter(s -> s.getSuspectIdCard() != null &&
                        s.getSuspectIdCard().equals(idCard) &&
                        !s.getIsDeleted())
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Suspect not found with ID card: " + idCard));

        interview.setSuspectInterviewee(suspect);
        log.info("Set suspect as interviewee: {}", suspect.getFullname());
    }

    /**
     * Set victim as interviewee
     */
    private void setVictimAsInterviewee(Interview interview, Long idCard) {
        Victim victim = victimRepository.findAll().stream()
                .filter(v -> v.getVictimId().equals(idCard.toString()) && !v.isDeleted())
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Victim not found with ID card: " + idCard));

        interview.setVictimInterviewee(victim);
        log.info("Set victim as interviewee: {}", victim.getFullname());
    }

    /**
     * Set witness as interviewee
     */
    private void setWitnessAsInterviewee(Interview interview, Long idCard) {
        Witness witness = witnessRepository.findAll().stream()
                .filter(w -> w.getWitnessIdCard() != null &&
                        w.getWitnessIdCard().equals(idCard) &&
                        !w.isDeleted())
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Witness not found with ID card: " + idCard));

        interview.setWitnessInterviewee(witness);
        log.info("Set witness as interviewee: {}", witness.getFullName());
    }

    // ================================
    // FILE UPLOAD METHODS
    // ================================

    /**
     * Upload files if provided
     */
    private List<String> uploadFilesIfProvided(List<MultipartFile> files) {
        List<String> uploadedFileNames = new ArrayList<>();

        if (files == null || files.isEmpty()) {
            log.info("No files provided for upload");
            return uploadedFileNames;
        }

        log.info("Uploading {} files", files.size());

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    String savedFileName = uploadSingleFile(file);
                    uploadedFileNames.add(savedFileName);
                    log.info("File uploaded successfully: {}", savedFileName);
                } catch (Exception e) {
                    log.error("Failed to upload file: {}", file.getOriginalFilename(), e);
                    throw new RuntimeException("Failed to upload file: " + file.getOriginalFilename());
                }
            }
        }

        return uploadedFileNames;
    }

    /**
     * Upload single file
     */
    private String uploadSingleFile(MultipartFile file) throws IOException {
        validateFileBeforeUpload(file);

        String uniqueFileName = createUniqueFileName(file.getOriginalFilename());
        String uploadDirectory = extractDirectoryFromBaseUri(baseUri);

        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("Created upload directory: {}", uploadPath.toAbsolutePath());
        }

        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("File saved to: {}", filePath.toAbsolutePath());
        return uniqueFileName;
    }

    /**
     * Validate file before upload
     */
    private void validateFileBeforeUpload(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be empty");
        }

        String fileExtension = getFileExtension(originalFileName);
        if (!ALLOWED_FILE_TYPES.contains(fileExtension.toLowerCase())) {
            throw new IllegalArgumentException(
                    "File type not allowed: " + fileExtension + ". Allowed types: " + ALLOWED_FILE_TYPES);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size too large. Maximum allowed: 50MB");
        }
    }

    /**
     * Get file extension from filename
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }

    /**
     * Create unique filename to avoid conflicts
     */
    private String createUniqueFileName(String originalFileName) {
        return System.currentTimeMillis() + "-" + originalFileName;
    }

    /**
     * Extract directory path from baseUri configuration
     */
    private String extractDirectoryFromBaseUri(String baseUri) {
        if (baseUri == null || baseUri.isEmpty()) {
            return "uploads";
        }

        String directory = baseUri;
        if (directory.startsWith("file:")) {
            directory = directory.substring(5);
        }
        if (directory.endsWith("/")) {
            directory = directory.substring(0, directory.length() - 1);
        }
        if (directory.isEmpty()) {
            directory = "uploads";
        }

        log.debug("Extracted upload directory from baseUri '{}': '{}'", baseUri, directory);
        return directory;
    }
}