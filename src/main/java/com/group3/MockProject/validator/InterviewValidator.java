package com.group3.MockProject.validator;

import com.group3.MockProject.dto.request.CreateInterviewRequest;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Interview;
import com.group3.MockProject.entity.User;
import com.group3.MockProject.exception.AppException;
import com.group3.MockProject.exception.ErrorCode;
import com.group3.MockProject.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

/**
 * InterviewValidator
 * <p>
 * Validation logic for interview operations
 * <p>
 * Version 1.0
 * Date: 7/28/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/28/2025      FongFox      Create
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InterviewValidator {
    private final CaseRepository caseRepository;
    private final UserRepository userRepository;
    private final SuspectRepository suspectRepository;
    private final VictimRepository victimRepository;
    private final WitnessRepository witnessRepository;
    private final InterviewRepository interviewRepository;

    /**
     * Validates complete interview creation request
     *
     * @param caseId The case identifier
     * @param request The interview creation request
     * @param files Optional files to validate
     * @throws AppException if validation fails
     */
    public void validateCreateInterviewRequest(String caseId, CreateInterviewRequest request, List<MultipartFile> files) {
        log.debug("Validating interview creation request for case: {}", caseId);

        validateCaseExists(caseId);
        validateInterviewData(request);
        validateInterviewer(request.getInterviewerId());
        validateInterviewee(request.getIntervieweeType(), request.getIntervieweeIdCard());
        validateTimeConflict(request.getInterviewerId(), request);
        validateFiles(files);

        log.debug("Interview validation completed successfully for case: {}", caseId);
    }

    /**
     * Validates that case exists in database
     *
     * @param caseId The case identifier to check
     * @throws AppException if case does not exist
     */
    public void validateCaseExists(String caseId) {
        if (caseId == null || caseId.trim().isEmpty()) {
            log.error("Case ID is null or empty");
            throw new AppException(ErrorCode.INVALID_KEY, "Case ID is required");
        }

        boolean caseExists = caseRepository.existsById(caseId);
        if (!caseExists) {
            log.error("Case not found with ID: {}", caseId);
            throw new AppException(ErrorCode.CASE_NOT_EXISTED);
        }
        log.debug("Case validation passed for ID: {}", caseId);
    }

    /**
     * Validates basic interview data
     *
     * @param request The interview request to validate
     * @throws AppException if validation fails
     */
    public void validateInterviewData(CreateInterviewRequest request) {
        log.debug("Validating interview data");

        if (request == null) {
            throw new AppException(ErrorCode.INVALID_INTERVIEW_DATA, "Interview request cannot be null");
        }

        // Validate time range: start time must be before end time
        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new AppException(ErrorCode.INVALID_TIME_RANGE, "Start time and end time are required");
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            log.error("Invalid time range: start time {} is after end time {}",
                    request.getStartTime(), request.getEndTime());
            throw new AppException(ErrorCode.INVALID_TIME_RANGE);
        }

        // Validate location
        if (request.getLocation() == null || request.getLocation().trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_LOCATION);
        }

        // Validate interviewee type
        if (request.getIntervieweeType() == null || request.getIntervieweeType().trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INTERVIEWEE_TYPE, "Interviewee type is required");
        }

        String intervieweeType = request.getIntervieweeType().toUpperCase();
        if (!intervieweeType.equals("SUSPECT") && !intervieweeType.equals("VICTIM") && !intervieweeType.equals("WITNESS")) {
            throw new AppException(ErrorCode.INVALID_INTERVIEWEE_TYPE);
        }

        // Validate questions
        if (request.getQuesAndAns() == null || request.getQuesAndAns().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_QUESTION_DATA, "At least one question is required");
        }

        // Validate each question
        request.getQuesAndAns().forEach(question -> {
            if (question.getQuestion() == null || question.getQuestion().trim().isEmpty()) {
                throw new AppException(ErrorCode.INVALID_QUESTION_DATA, "Question content cannot be empty");
            }
            if (question.getAnswer() == null || question.getAnswer().trim().isEmpty()) {
                throw new AppException(ErrorCode.INVALID_QUESTION_DATA, "Answer content cannot be empty");
            }
            if (question.getLevelOfTrust() == null || question.getLevelOfTrust().trim().isEmpty()) {
                throw new AppException(ErrorCode.INVALID_LEVEL_OF_TRUST);
            }

            String trustLevel = question.getLevelOfTrust().toLowerCase();
            if (!trustLevel.equals("a") && !trustLevel.equals("b") && !trustLevel.equals("c")) {
                throw new AppException(ErrorCode.INVALID_LEVEL_OF_TRUST);
            }
        });

        log.debug("Interview data validation passed");
    }

    /**
     * Validates that interviewer exists and is valid
     *
     * @param interviewerId The interviewer identifier
     * @throws AppException if interviewer not found
     */
    public void validateInterviewer(String interviewerId) {
        log.debug("Validating interviewer: {}", interviewerId);

        if (interviewerId == null || interviewerId.trim().isEmpty()) {
            throw new AppException(ErrorCode.INTERVIEWER_NOT_FOUND, "Interviewer ID is required");
        }

        Optional<User> interviewer = userRepository.findByUsernameAndIsDeletedFalse(interviewerId);
        if (interviewer.isEmpty()) {
            log.error("Interviewer not found with ID: {}", interviewerId);
            throw new AppException(ErrorCode.INTERVIEWER_NOT_FOUND);
        }

        log.debug("Interviewer validation passed for ID: {}", interviewerId);
    }

    /**
     * Validates that interviewee exists based on type and ID card
     *
     * @param intervieweeType The type of interviewee (SUSPECT, VICTIM, WITNESS)
     * @param intervieweeIdCard The ID card number
     * @throws AppException if interviewee not found
     */
    public void validateInterviewee(String intervieweeType, String intervieweeIdCard) {
        log.debug("Validating interviewee: type={}, idCard={}", intervieweeType, intervieweeIdCard);

        if (intervieweeIdCard == null || intervieweeIdCard.trim().isEmpty()) {
            throw new AppException(ErrorCode.INTERVIEWEE_NOT_FOUND, "Interviewee ID card is required");
        }

        Long idCard;
        try {
            idCard = Long.parseLong(intervieweeIdCard);
        } catch (NumberFormatException e) {
            throw new AppException(ErrorCode.INVALID_KEY, "Invalid ID card format");
        }

        boolean found = switch (intervieweeType.toUpperCase()) {
            case "SUSPECT" -> suspectRepository.findBySuspectIdCard(idCard).isPresent();
            case "VICTIM" -> victimRepository.findByVictimIdCard(idCard).isPresent();
            case "WITNESS" -> witnessRepository.findByWitnessIdCard(idCard).isPresent();
            default -> false;
        };

        if (!found) {
            log.error("Interviewee not found: type={}, idCard={}", intervieweeType, intervieweeIdCard);
            throw new AppException(ErrorCode.INTERVIEWEE_NOT_FOUND);
        }

        log.debug("Interviewee validation passed: type={}, idCard={}", intervieweeType, intervieweeIdCard);
    }

    /**
     * Validates that there are no time conflicts for the interviewer
     *
     * @param interviewerId The interviewer identifier
     * @param request The interview request with time information
     * @throws AppException if time conflict exists
     */
    public void validateTimeConflict(String interviewerId, CreateInterviewRequest request) {
        log.debug("Checking time conflicts for interviewer: {}", interviewerId);

        LocalDateTime startTime = request.getStartTime().atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endTime = request.getEndTime().atOffset(ZoneOffset.UTC).toLocalDateTime();

        List<Interview> conflictInterviews = interviewRepository.findConflictingInterviews(
                interviewerId, startTime, endTime);

        if (!conflictInterviews.isEmpty()) {
            log.error("Time conflict found for interviewer {} between {} and {}",
                    interviewerId, startTime, endTime);
            throw new AppException(ErrorCode.INTERVIEW_SCHEDULING_CONFLICT);
        }

        log.debug("No time conflicts found for interviewer: {}", interviewerId);
    }

    /**
     * Validates uploaded files
     *
     * @param files List of files to validate
     * @throws AppException if file validation fails
     */
    public void validateFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            log.debug("No files to validate");
            return;
        }

        log.debug("Validating {} files", files.size());

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue; // Skip empty files
            }

            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.trim().isEmpty()) {
                throw new AppException(ErrorCode.INVALID_FILE_NAME);
            }

            // Validate file size (50MB limit)
            long maxFileSize = 50 * 1024 * 1024; // 50MB
            if (file.getSize() > maxFileSize) {
                log.error("File too large: {} bytes", file.getSize());
                throw new AppException(ErrorCode.FILE_TOO_LARGE);
            }

            // Validate file extension
            String extension = getFileExtension(originalFileName);
            if (!isValidFileExtension(extension)) {
                log.error("Invalid file extension: {}", extension);
                throw new AppException(ErrorCode.FILE_INVALID_EXTENSION);
            }
        }

        log.debug("File validation completed successfully");
    }

    /**
     * Gets file extension from filename
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        return "";
    }

    /**
     * Checks if file extension is valid
     */
    private boolean isValidFileExtension(String extension) {
        List<String> allowedExtensions = List.of(
                "jpg", "jpeg", "png", "gif", "bmp", "webp", // Images
                "mp3", "wav", "aac", "flac", "mp4", "avi", "mov", "mkv", "webm", // Audio/Video
                "pdf", "doc", "docx", "txt", "rtf", // Documents
                "zip", "rar", "7z" // Archives
        );
        return allowedExtensions.contains(extension.toLowerCase());
    }

    /**
     * Finds and returns the interviewer entity
     *
     * @param interviewerId The interviewer identifier
     * @return User entity
     * @throws AppException if interviewer not found
     */
    public User getValidatedInterviewer(String interviewerId) {
        return userRepository.findByUsernameAndIsDeletedFalse(interviewerId)
                .orElseThrow(() -> new AppException(ErrorCode.INTERVIEWER_NOT_FOUND));
    }

    /**
     * Finds and returns the case entity
     *
     * @param caseId The case identifier
     * @return Case entity
     * @throws AppException if case not found
     */
    public Case getValidatedCase(String caseId) {
        return caseRepository.findById(caseId)
                .orElseThrow(() -> new AppException(ErrorCode.CASE_NOT_EXISTED));
    }

    /**
     * Finds and returns the interviewee entity
     *
     * @param intervieweeType The type of interviewee
     * @param intervieweeIdCard The ID card number
     * @return Interviewee entity (Suspect, Victim, or Witness)
     * @throws AppException if interviewee not found
     */
    public Object getValidatedInterviewee(String intervieweeType, String intervieweeIdCard) {
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
}
