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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
public class InterviewServiceImpl implements InterviewService {
    // Repositories for database access
    private final InterviewRepository interviewRepository;
    private final InterviewFileRepository interviewFileRepository;
    private final UserRepository userRepository;
    private final SuspectRepository suspectRepository;
    private final VictimRepository victimRepository;
    private final WitnessRepository witnessRepository;
    private final QuestionRepository questionRepository;
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
    public InterviewResponseDto createInterview(String caseId, CreateInterviewDto dto, List<MultipartFile> files) {
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

        // STEP 6: Save interview FIRST (without children)
        Interview savedInterview = interviewRepository.save(interview);
        log.info("Interview saved with ID: {}", savedInterview.getInterviewId());

        // STEP 7: Create and save questions manually
        List<Question> questions = interviewMapper.convertToQuestionEntities(dto.getQuesAndAns(), savedInterview, interviewer);
        if (questions != null && !questions.isEmpty()) {
            for (Question question : questions) {
                question.setInterview(savedInterview); // Ensure back reference
            }
            List<Question> savedQuestions = questionRepository.saveAll(questions);
            savedInterview.setQuestions(savedQuestions);
            log.info("Saved {} questions", savedQuestions.size());
        }

        // STEP 8: Create and save interview files manually
        List<InterviewFile> interviewFiles = interviewMapper.convertToInterviewFileEntities(uploadedFilePaths, savedInterview);
        if (interviewFiles != null && !interviewFiles.isEmpty()) {
            for (InterviewFile file : interviewFiles) {
                file.setInterview(savedInterview); // Ensure back reference
            }
            List<InterviewFile> savedFiles = interviewFileRepository.saveAll(interviewFiles);
            savedInterview.setInterviewFileList(savedFiles);
            log.info("Saved {} files", savedFiles.size());
        }

        // STEP 9: Convert to Response DTO and return
        InterviewResponseDto responseDto = interviewMapper.convertToResponseDto(savedInterview);

        log.info("Interview created successfully with ID: {}", savedInterview.getInterviewId());
        return responseDto;
    }


    /*
    @Override
    @Transactional
    public InterviewResponseDto createInterview(String caseId, CreateInterviewDto dto, List<MultipartFile> files) {
        log.info("=== SUPER MINIMAL DEBUG TEST START ===");

        try {
            log.info("Step 1: Validate basic DTO");
            if (dto == null) {
                throw new IllegalArgumentException("DTO is null");
            }
            log.info("✅ DTO validation passed");

            log.info("Step 2: Create minimal interview entity");
            Interview interview = new Interview();
            interview.setLocation("TEST LOCATION");
            interview.setTypeInterviewee("WITNESS");
            interview.setDeleted(false);

            // Set minimal datetime
            interview.setStartTime(LocalDateTime.now());
            interview.setEndTime(LocalDateTime.now().plusHours(1));

            log.info("Step 3: Set all relationships to NULL to avoid FK constraints");
            interview.setUserInterviewer(null);
            interview.setSuspectInterviewee(null);
            interview.setVictimInterviewee(null);
            interview.setWitnessInterviewee(null);
            interview.setCaseInterview(null);
            interview.setQuestions(null);
            interview.setInterviewFileList(null);

            log.info("✅ Interview entity created: {}", interview);

            log.info("Step 4: Attempt to save minimal interview to database");
            Interview savedInterview = interviewRepository.save(interview);
            log.info("✅ SUCCESS: Interview saved with ID: {}", savedInterview.getInterviewId());

            log.info("Step 5: Create fake response for testing");
            InterviewResponseDto response = InterviewResponseDto.builder()
                    .startTime(savedInterview.getStartTime())
                    .endTime(savedInterview.getEndTime())
                    .location(savedInterview.getLocation())
                    .interviewerName("DEBUG TEST INTERVIEWER")
                    .intervieweeType(savedInterview.getTypeInterviewee())
                    .intervieweeName("DEBUG TEST INTERVIEWEE")
                    .totalQuestions(0)
                    .attachedFiles(new ArrayList<>())
                    .createdAt(savedInterview.getCreateAt())
                    .build();

            log.info("✅ Response DTO created: {}", response);
            log.info("=== SUPER MINIMAL DEBUG TEST SUCCESS ===");
            return response;

        } catch (Exception e) {
            log.error("❌ === SUPER MINIMAL DEBUG TEST FAILED ===");
            log.error("❌ Error type: {}", e.getClass().getSimpleName());
            log.error("❌ Error message: {}", e.getMessage());

            // Check specific error types
            if (e.getMessage().contains("Row was updated or deleted")) {
                log.error("❌ ISSUE: Database concurrency/versioning problem");
            } else if (e.getMessage().contains("foreign key constraint")) {
                log.error("❌ ISSUE: Foreign key constraint violation");
            } else if (e.getMessage().contains("Column") && e.getMessage().contains("cannot be null")) {
                log.error("❌ ISSUE: Required database column is null");
            }

            log.error("❌ Full stack trace: ", e);
            throw e;
        }
    }
    */

    /*
    @Override
    @Transactional
    public InterviewResponseDto createInterview(String caseId, CreateInterviewDto dto, List<MultipartFile> files) {
        log.info("=== STEP BY STEP DEBUG TEST ===");

        try {
            log.info("Step 1: Basic validation");
            if (dto == null) {
                throw new IllegalArgumentException("DTO is null");
            }
            log.info("✅ DTO OK");

            log.info("Step 2: Try to find interviewer by ID: {}", dto.getInterviewerId());
            User interviewer = findInterviewerById(dto.getInterviewerId());
            log.info("✅ Found interviewer: {}", interviewer.getFullName());

            log.info("Step 3: Create interview with REAL data from DTO");
            Interview interview = new Interview();
            // Use REAL data from DTO
            interview.setStartTime(dto.getStartTime().atZone(ZoneOffset.UTC).toLocalDateTime());
            interview.setEndTime(dto.getEndTime().atZone(ZoneOffset.UTC).toLocalDateTime());
            interview.setLocation(dto.getLocation());
            interview.setTypeInterviewee(dto.getIntervieweeType());
            interview.setDeleted(false);

            // Set interviewer (REAL relationship)
            interview.setUserInterviewer(interviewer);

            // Set other relationships to NULL for now
            interview.setSuspectInterviewee(null);
            interview.setVictimInterviewee(null);
            interview.setWitnessInterviewee(null);
            interview.setCaseInterview(null);
            interview.setQuestions(null);
            interview.setInterviewFileList(null);

            log.info("✅ Interview entity created with real data");

            log.info("Step 4: Try to save interview with interviewer relationship");
            Interview savedInterview = interviewRepository.save(interview);
            log.info("✅ SUCCESS: Interview saved with interviewer relationship. ID: {}", savedInterview.getInterviewId());

            log.info("Step 5: Try to find interviewee");
            setIntervieweeByType(savedInterview, dto.getIntervieweeType(), dto.getIntervieweeIdCard());
            log.info("✅ Interviewee set successfully");

            log.info("Step 6: Update interview with interviewee");
            Interview updatedInterview = interviewRepository.save(savedInterview);
            log.info("✅ Interview updated with interviewee. ID: {}", updatedInterview.getInterviewId());

            log.info("Step 7: Create response with real data");
            InterviewResponseDto response = InterviewResponseDto.builder()
                    .startTime(updatedInterview.getStartTime())
                    .endTime(updatedInterview.getEndTime())
                    .location(updatedInterview.getLocation())
                    .interviewerName(updatedInterview.getUserInterviewer().getFullName())
                    .intervieweeType(updatedInterview.getTypeInterviewee())
                    .intervieweeName(getIntervieweeName(updatedInterview))
                    .totalQuestions(0) // Still 0 for now
                    .attachedFiles(new ArrayList<>()) // Still empty for now
                    .createdAt(updatedInterview.getCreateAt())
                    .build();

            log.info("✅ Response created successfully");
            log.info("=== STEP BY STEP DEBUG SUCCESS ===");
            return response;

        } catch (Exception e) {
            log.error("❌ Error at step: {}", e.getMessage());
            log.error("❌ Error type: {}", e.getClass().getSimpleName());
            log.error("❌ Stack trace: ", e);
            throw e;
        }
    }
    */

    /*
    @Override
    @Transactional
    public InterviewResponseDto createInterview(String caseId, CreateInterviewDto dto, List<MultipartFile> files) {
        log.info("=== FINAL COMPLETE TEST - Adding Questions ===");

        try {
            // Steps 1-6 đã work, giữ nguyên
            log.info("Step 1-2: Validation and find interviewer");
            if (dto == null) {
                throw new IllegalArgumentException("DTO is null");
            }
            User interviewer = findInterviewerById(dto.getInterviewerId());
            log.info("✅ Found interviewer: {}", interviewer.getFullName());

            log.info("Step 3-4: Create and save interview");
            Interview interview = new Interview();
            interview.setStartTime(dto.getStartTime().atZone(ZoneOffset.UTC).toLocalDateTime());
            interview.setEndTime(dto.getEndTime().atZone(ZoneOffset.UTC).toLocalDateTime());
            interview.setLocation(dto.getLocation());
            interview.setTypeInterviewee(dto.getIntervieweeType());
            interview.setDeleted(false);
            interview.setUserInterviewer(interviewer);

            // Set other relationships to NULL
            interview.setSuspectInterviewee(null);
            interview.setVictimInterviewee(null);
            interview.setWitnessInterviewee(null);
            interview.setCaseInterview(null);
            interview.setQuestions(null);
            interview.setInterviewFileList(null);

            Interview savedInterview = interviewRepository.save(interview);
            log.info("✅ Interview saved: {}", savedInterview.getInterviewId());

            log.info("Step 5-6: Set interviewee");
            setIntervieweeByType(savedInterview, dto.getIntervieweeType(), dto.getIntervieweeIdCard());
            Interview updatedInterview = interviewRepository.save(savedInterview);
            log.info("✅ Interviewee set: {}", getIntervieweeName(updatedInterview));

            // Thay thế Step 7-8 trong method createInterview:
            // NEW: Add questions step by step - DETAILED DEBUG
            log.info("Step 7: Process questions - START");
            List<Question> questions = new ArrayList<>();

            if (dto.getQuesAndAns() != null && !dto.getQuesAndAns().isEmpty()) {
                log.info("Creating {} questions", dto.getQuesAndAns().size());

                for (int i = 0; i < dto.getQuesAndAns().size(); i++) {
                    QuestionDto questionDto = dto.getQuesAndAns().get(i);
                    log.info("Processing question {}: {}", i + 1, questionDto.getQuestion());

                    Question question = new Question();
                    question.setContent(questionDto.getQuestion());
                    question.setAnswer(questionDto.getAnswer());
                    question.setReliability(convertLevelOfTrustToFloat(questionDto.getLevelOfTrust()));
                    question.setInterview(updatedInterview);
                    question.setUser(interviewer);
                    question.setDeleted(false);

                    questions.add(question);
                    log.info("✅ Question {} created in memory", i + 1);
                }

                log.info("Step 8: Save questions ONE BY ONE to find exact issue");
                List<Question> savedQuestions = new ArrayList<>();

                for (int i = 0; i < questions.size(); i++) {
                    Question question = questions.get(i);
                    try {
                        log.info("Attempting to save question {}: {}", i + 1, question.getContent());
                        log.info("Question details - ID: {}, Interview ID: {}, User ID: {}",
                                question.getQuestionId(),
                                question.getInterview().getInterviewId(),
                                question.getUser().getUsername());

                        Question savedQuestion = questionRepository.save(question);
                        savedQuestions.add(savedQuestion);

                        log.info("✅ Question {} saved successfully with ID: {}", i + 1, savedQuestion.getQuestionId());
                    } catch (Exception e) {
                        log.error("❌ FAILED to save question {}: {}", i + 1, question.getContent());
                        log.error("❌ Question details that failed:");
                        log.error("   - Question ID: {}", question.getQuestionId());
                        log.error("   - Content: {}", question.getContent());
                        log.error("   - Answer: {}", question.getAnswer());
                        log.error("   - Reliability: {}", question.getReliability());
                        log.error("   - Interview ID: {}", question.getInterview() != null ? question.getInterview().getInterviewId() : "NULL");
                        log.error("   - User ID: {}", question.getUser() != null ? question.getUser().getUsername() : "NULL");
                        log.error("   - Is Deleted: {}", question.isDeleted());
                        log.error("❌ Error type: {}", e.getClass().getSimpleName());
                        log.error("❌ Error message: {}", e.getMessage());

                        // Check specific issues
                        if (e.getMessage().contains("foreign key constraint")) {
                            log.error("❌ ISSUE: Foreign key constraint - Interview or User doesn't exist in DB");
                        } else if (e.getMessage().contains("Column") && e.getMessage().contains("cannot be null")) {
                            log.error("❌ ISSUE: Required column is null");
                        } else if (e.getMessage().contains("Duplicate entry")) {
                            log.error("❌ ISSUE: Duplicate question ID");
                        }

                        throw e; // Stop at first error
                    }
                }

                updatedInterview.setQuestions(savedQuestions);
                log.info("✅ ALL {} questions saved successfully", savedQuestions.size());
            } else {
                log.info("No questions to process");
            }

            // NEW: Add files step by step
            log.info("Step 9: Process files - START");
            List<InterviewFile> interviewFiles = new ArrayList<>();

            List<String> uploadedFilePaths = uploadFilesIfProvided(files);
            if (!uploadedFilePaths.isEmpty()) {
                log.info("Creating {} interview files", uploadedFilePaths.size());

                for (String filePath : uploadedFilePaths) {
                    log.info("Processing file: {}", filePath);

                    InterviewFile interviewFile = new InterviewFile();
                    interviewFile.setInterviewFileId(UUID.randomUUID().toString());
                    interviewFile.setAttachedFile(filePath);
                    interviewFile.setInterview(updatedInterview);
                    interviewFile.setDeleted(false);

                    interviewFiles.add(interviewFile);
                    log.info("✅ File processed: {}", filePath);
                }

                log.info("Step 10: Save all files to database");
                List<InterviewFile> savedFiles = interviewFileRepository.saveAll(interviewFiles);
                updatedInterview.setInterviewFileList(savedFiles);
                log.info("✅ Saved {} files successfully", savedFiles.size());
            } else {
                log.info("No files to process");
            }

            log.info("Step 11: Create final response");
            InterviewResponseDto response = InterviewResponseDto.builder()
                    .startTime(updatedInterview.getStartTime())
                    .endTime(updatedInterview.getEndTime())
                    .location(updatedInterview.getLocation())
                    .interviewerName(updatedInterview.getUserInterviewer().getFullName())
                    .intervieweeType(updatedInterview.getTypeInterviewee())
                    .intervieweeName(getIntervieweeName(updatedInterview))
                    .totalQuestions(updatedInterview.getQuestions() != null ? updatedInterview.getQuestions().size() : 0)
                    .attachedFiles(getAttachedFileNames(updatedInterview))
                    .createdAt(updatedInterview.getCreateAt())
                    .build();

            log.info("✅ Final response created successfully");
            log.info("=== FINAL COMPLETE TEST SUCCESS ===");
            return response;

        } catch (Exception e) {
            log.error("❌ Error at step: {}", e.getMessage());
            log.error("❌ Error type: {}", e.getClass().getSimpleName());
            log.error("❌ Stack trace: ", e);
            throw e;
        }
    }
    */

    // ================================
    // VALIDATION METHODS
    // ================================
    /**
     * Validate all interview data
     */
    private void validateInterviewData(CreateInterviewDto dto) {
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

        // Validate each question
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
    // HELPER METHOD FOR VALIDATION
    // ================================
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
     * Get attached file name
     */
    private List<String> getAttachedFileNames(Interview interview) {
        List<String> fileNames = new ArrayList<>();
        if (interview.getInterviewFileList() != null) {
            for (InterviewFile file : interview.getInterviewFileList()) {
                if (!file.isDeleted() && file.getAttachedFile() != null) {
                    fileNames.add(file.getAttachedFile());
                }
            }
        }
        return fileNames;
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

    /**
     * Helper method to get interviewee name
     */
    private String getIntervieweeName(Interview interview) {
        String type = interview.getTypeInterviewee();
        if ("SUSPECT".equals(type) && interview.getSuspectInterviewee() != null) {
            return interview.getSuspectInterviewee().getFullname();
        } else if ("VICTIM".equals(type) && interview.getVictimInterviewee() != null) {
            return interview.getVictimInterviewee().getFullname();
        } else if ("WITNESS".equals(type) && interview.getWitnessInterviewee() != null) {
            return interview.getWitnessInterviewee().getFullName();
        }
        return "Unknown";
    }

    // ================================
    // CONVERTER METHODS
    // ================================
    /**
     * Convert level of trust to float
     */
    private Float convertLevelOfTrustToFloat(String levelOfTrust) {
        if (levelOfTrust == null) return 0.4f;
        String level = levelOfTrust.toLowerCase().trim();
        switch (level) {
            case "a": return 1.0f;
            case "b": return 0.7f;
            case "c": return 0.4f;
            default: return 0.4f;
        }
    }

}