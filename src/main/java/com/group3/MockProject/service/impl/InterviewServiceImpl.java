package com.group3.MockProject.service.impl;

import com.group3.MockProject.dto.request.CreateInterviewRequest;
import com.group3.MockProject.dto.request.QuestionRequest;
import com.group3.MockProject.dto.response.InterviewResponse;
import com.group3.MockProject.entity.*;
import com.group3.MockProject.mapper.InterviewMapper;
import com.group3.MockProject.repository.*;
import com.group3.MockProject.service.IInterviewService;
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
 * 7/10/2025     User      Update to match API spec exactly
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class InterviewServiceImpl implements IInterviewService {
    // Repositories for database access
    private final InterviewRepository interviewRepository;
    private final InterviewFileRepository interviewFileRepository;
    private final UserRepository userRepository;
    private final SuspectRepository suspectRepository;
    private final VictimRepository victimRepository;
    private final WitnessRepository witnessRepository;

    // Mapper for DTO/Entity conversion
    private final InterviewMapper interviewMapper;

    @Value("${spring.upload-file.base-uri}")
    private String baseUri;

    // Constants for file upload
    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList(
            "mp4", "mp3", "wav", "avi", "mov", "pdf", "doc", "docx", "jpg", "png"
    );
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

    @Override
    @Transactional
    public InterviewResponse createInterview(String caseId, CreateInterviewRequest dto, List<MultipartFile> files) {
        log.info("Starting interview creation process for case: {}", caseId);

        // STEP 1: Validate input data
        validateInterviewData(dto);

        // STEP 2: Find interviewer by ID
        User interviewer = findInterviewerById(dto.getInterviewerId());

        // STEP 3: Upload files if provided
        List<String> uploadedFilePaths = uploadFilesIfProvided(files);

        // STEP 4: Create Interview entity from DTO
        Interview interview = interviewMapper.convertToInterviewEntity(dto, interviewer);

        // STEP 5: Set interviewee based on type
        setIntervieweeByType(interview, dto.getIntervieweeType(), dto.getIntervieweeIdCard());

        // STEP 6: Create questions from DTO
        List<Question> questions = interviewMapper.convertToQuestionEntities(dto.getQuesAndAns(), interview, interviewer);
        interview.setQuestions(questions);

        // STEP 7: Create InterviewFile entities
        List<InterviewFile> interviewFiles = interviewMapper.convertToInterviewFileEntities(uploadedFilePaths, interview);
        interview.setInterviewFileList(interviewFiles);

        // STEP 8: Save interview to database (cascade will save questions and files)
        Interview savedInterview = interviewRepository.save(interview);

        // STEP 9: Convert to Response DTO and return
        InterviewResponse responseDto = interviewMapper.convertToResponseDto(savedInterview);

        log.info("Interview created successfully with ID: {}", savedInterview.getInterviewId());
        return responseDto;
    }

    // ================================
    // VALIDATION METHODS
    // ================================

    /**
     * Validate all interview data
     */
    private void validateInterviewData(CreateInterviewRequest dto) {
        log.debug("Validating interview data");

        if (dto == null) {
            throw new IllegalArgumentException("Interview data is required");
        }

        // Validate time fields
        validateTimeFields(dto);

        // Validate required fields
        validateRequiredFields(dto);

        // Validate questions list
        validateQuestionsList(dto.getQuesAndAns());
    }

    /**
     * Validate start and end time
     */
    private void validateTimeFields(CreateInterviewRequest dto) {
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
    private void validateRequiredFields(CreateInterviewRequest dto) {
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
    private void validateQuestionsList(List<QuestionRequest> questions) {
        if (questions == null || questions.isEmpty()) {
            throw new IllegalArgumentException("At least one question is required");
        }

        // Validate each question
        for (int i = 0; i < questions.size(); i++) {
            validateSingleQuestion(questions.get(i), i + 1);
        }
    }

    /**
     * Validate single question
     */
    private void validateSingleQuestion(QuestionRequest question, int questionNumber) {
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

    // Helper methods for validation
    private boolean isStringEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean isValidIntervieweeType(String type) {
        return type != null &&
                (type.equals("SUSPECT") || type.equals("VICTIM") || type.equals("WITNESS"));
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
     * Find interviewer by ID
     */
    private User findInterviewerById(String interviewerId) {
        return userRepository.findById(interviewerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Interviewer not found with ID: " + interviewerId));
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

        // Upload each file
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
        // Validate file before upload
        validateFileBeforeUpload(file);

        // Create unique filename
        String uniqueFileName = createUniqueFileName(file.getOriginalFilename());

        // Parse baseUri to get the actual directory path
        String uploadDirectory = extractDirectoryFromBaseUri(baseUri);

        // Create upload directory if not exists
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("Created upload directory: {}", uploadPath.toAbsolutePath());
        }

        // Save file to the configured directory
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

        // Check file extension
        String fileExtension = getFileExtension(originalFileName);
        if (!ALLOWED_FILE_TYPES.contains(fileExtension.toLowerCase())) {
            throw new IllegalArgumentException(
                    "File type not allowed: " + fileExtension +
                            ". Allowed types: " + ALLOWED_FILE_TYPES
            );
        }

        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size too large. Maximum allowed: 50MB");
        }
    }

    /**
     * Get file extension
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
     * Extract directory path from baseUri config
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

    // ================================
    // INTERVIEWEE SETTER METHODS
    // ================================

    /**
     * Set interviewee based on type
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
        List<Suspect> allSuspects = suspectRepository.findAll();

        Suspect foundSuspect = null;
        for (Suspect suspect : allSuspects) {
            if (suspect.getSuspectIdCard() != null &&
                    suspect.getSuspectIdCard().equals(idCard) &&
                    !suspect.getIsDeleted()) {
                foundSuspect = suspect;
                break;
            }
        }

        if (foundSuspect == null) {
            throw new EntityNotFoundException("Suspect not found with ID card: " + idCard);
        }

        interview.setSuspectInterviewee(foundSuspect);
        log.info("Set suspect as interviewee: {}", foundSuspect.getFullname());
    }

    /**
     * Set victim as interviewee
     */
    private void setVictimAsInterviewee(Interview interview, Long idCard) {
        List<Victim> allVictims = victimRepository.findAll();

        Victim foundVictim = null;
        for (Victim victim : allVictims) {
            if (victim.getVictimId().equals(idCard.toString()) && !victim.isDeleted()) {
                foundVictim = victim;
                break;
            }
        }

        if (foundVictim == null) {
            throw new EntityNotFoundException("Victim not found with ID card: " + idCard);
        }

        interview.setVictimInterviewee(foundVictim);
        log.info("Set victim as interviewee: {}", foundVictim.getFullname());
    }

    /**
     * Set witness as interviewee
     */
    private void setWitnessAsInterviewee(Interview interview, Long idCard) {
        List<Witness> allWitnesses = witnessRepository.findAll();

        Witness foundWitness = null;
        for (Witness witness : allWitnesses) {
            if (witness.getWitnessIdCard() != null &&
                    witness.getWitnessIdCard().equals(idCard) &&
                    !witness.isDeleted()) {
                foundWitness = witness;
                break;
            }
        }

        if (foundWitness == null) {
            throw new EntityNotFoundException("Witness not found with ID card: " + idCard);
        }

        interview.setWitnessInterviewee(foundWitness);
        log.info("Set witness as interviewee: {}", foundWitness.getFullName());
    }
}