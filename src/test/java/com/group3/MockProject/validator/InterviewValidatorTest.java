package com.group3.MockProject.validator;

import com.group3.MockProject.dto.request.CreateInterviewRequest;
import com.group3.MockProject.dto.request.QuestionRequest;
import com.group3.MockProject.entity.*;
import com.group3.MockProject.exception.AppException;
import com.group3.MockProject.exception.ErrorCode;
import com.group3.MockProject.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * InterviewValidatorTest
 * <p>
 * Unit tests for InterviewValidator class
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
@ExtendWith(MockitoExtension.class)
public class InterviewValidatorTest {
    @Mock
    private CaseRepository caseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SuspectRepository suspectRepository;

    @Mock
    private VictimRepository victimRepository;

    @Mock
    private WitnessRepository witnessRepository;

    @Mock
    private InterviewRepository interviewRepository;

    @InjectMocks
    private InterviewValidator interviewValidator;

    private CreateInterviewRequest validRequest;
    private User mockInterviewer;
    private Suspect mockSuspect;
    private Case mockCase;

    @BeforeEach
    void setUp() {
        // Setup valid request
        QuestionRequest question = new QuestionRequest("What happened?", "I saw the incident", "a");

        validRequest = new CreateInterviewRequest();
        validRequest.setStartTime(Instant.parse("2025-07-28T10:00:00Z"));
        validRequest.setEndTime(Instant.parse("2025-07-28T11:00:00Z"));
        validRequest.setLocation("Interview Room 1");
        validRequest.setInterviewerId("interviewer123");
        validRequest.setIntervieweeType("SUSPECT");
        validRequest.setIntervieweeIdCard("123456789");
        validRequest.setQuesAndAns(List.of(question));

        // Setup mock entities
        mockCase = new Case();
        mockCase.setCaseId("case123");

        mockInterviewer = new User();
        mockInterviewer.setUsername("interviewer123");

        mockSuspect = new Suspect();
        mockSuspect.setSuspectId("suspect123");
        mockSuspect.setSuspectIdCard(123456789L);
    }

    // ================= CASE VALIDATION TESTS =================

    @Test
    void validateCaseExists_ValidCaseId_ShouldPass() {
        // Given
        when(caseRepository.existsById("case123")).thenReturn(true);

        // When & Then
        assertDoesNotThrow(() -> interviewValidator.validateCaseExists("case123"));
        verify(caseRepository).existsById("case123");
    }

    @Test
    void validateCaseExists_InvalidCaseId_ShouldThrowException() {
        // Given
        when(caseRepository.existsById("invalid")).thenReturn(false);

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewValidator.validateCaseExists("invalid"));

        assertEquals(ErrorCode.CASE_NOT_EXISTED, exception.getErrorCode());
        verify(caseRepository).existsById("invalid");
    }

    @Test
    void validateCaseExists_NullCaseId_ShouldThrowException() {
        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewValidator.validateCaseExists(null));

        assertEquals(ErrorCode.INVALID_KEY, exception.getErrorCode());
        assertTrue(exception.getMessage().contains("Case ID is required"));
    }

    @Test
    void validateCaseExists_EmptyCaseId_ShouldThrowException() {
        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewValidator.validateCaseExists("   "));

        assertEquals(ErrorCode.INVALID_KEY, exception.getErrorCode());
    }

    // ================= INTERVIEW DATA VALIDATION TESTS =================

    @Test
    void validateInterviewData_ValidRequest_ShouldPass() {
        // When & Then
        assertDoesNotThrow(() -> interviewValidator.validateInterviewData(validRequest));
    }

    @Test
    void validateInterviewData_NullRequest_ShouldThrowException() {
        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewValidator.validateInterviewData(null));

        assertEquals(ErrorCode.INVALID_INTERVIEW_DATA, exception.getErrorCode());
    }

    @Test
    void validateInterviewData_StartTimeAfterEndTime_ShouldThrowException() {
        // Given
        validRequest.setStartTime(Instant.parse("2025-07-28T12:00:00Z"));
        validRequest.setEndTime(Instant.parse("2025-07-28T11:00:00Z"));

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewValidator.validateInterviewData(validRequest));

        assertEquals(ErrorCode.INVALID_TIME_RANGE, exception.getErrorCode());
    }

    @Test
    void validateInterviewData_EmptyLocation_ShouldThrowException() {
        // Given
        validRequest.setLocation("");

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewValidator.validateInterviewData(validRequest));

        assertEquals(ErrorCode.INVALID_LOCATION, exception.getErrorCode());
    }

    @Test
    void validateInterviewData_InvalidIntervieweeType_ShouldThrowException() {
        // Given
        validRequest.setIntervieweeType("INVALID_TYPE");

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewValidator.validateInterviewData(validRequest));

        assertEquals(ErrorCode.INVALID_INTERVIEWEE_TYPE, exception.getErrorCode());
    }

    @Test
    void validateInterviewData_EmptyQuestions_ShouldThrowException() {
        // Given
        validRequest.setQuesAndAns(List.of());

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewValidator.validateInterviewData(validRequest));

        assertEquals(ErrorCode.INVALID_QUESTION_DATA, exception.getErrorCode());
    }

    @Test
    void validateInterviewData_InvalidLevelOfTrust_ShouldThrowException() {
        // Given
        QuestionRequest invalidQuestion = new QuestionRequest("What?", "Answer", "invalid");
        validRequest.setQuesAndAns(List.of(invalidQuestion));

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewValidator.validateInterviewData(validRequest));

        assertEquals(ErrorCode.INVALID_LEVEL_OF_TRUST, exception.getErrorCode());
    }

    // ================= INTERVIEWER VALIDATION TESTS =================

    @Test
    void validateInterviewer_ValidInterviewer_ShouldPass() {
        // Given
        when(userRepository.findByUsernameAndIsDeletedFalse("interviewer123"))
                .thenReturn(Optional.of(mockInterviewer));

        // When & Then
        assertDoesNotThrow(() -> interviewValidator.validateInterviewer("interviewer123"));
        verify(userRepository).findByUsernameAndIsDeletedFalse("interviewer123");
    }

    @Test
    void validateInterviewer_InterviewerNotFound_ShouldThrowException() {
        // Given
        when(userRepository.findByUsernameAndIsDeletedFalse("invalid"))
                .thenReturn(Optional.empty());

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewValidator.validateInterviewer("invalid"));

        assertEquals(ErrorCode.INTERVIEWER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void validateInterviewer_NullInterviewerId_ShouldThrowException() {
        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewValidator.validateInterviewer(null));

        assertEquals(ErrorCode.INTERVIEWER_NOT_FOUND, exception.getErrorCode());
    }

    // ================= INTERVIEWEE VALIDATION TESTS =================

    @Test
    void validateInterviewee_ValidSuspect_ShouldPass() {
        // Given
        when(suspectRepository.findBySuspectIdCard(123456789L))
                .thenReturn(Optional.of(mockSuspect));

        // When & Then
        assertDoesNotThrow(() ->
                interviewValidator.validateInterviewee("SUSPECT", "123456789"));
        verify(suspectRepository).findBySuspectIdCard(123456789L);
    }

    @Test
    void validateInterviewee_ValidVictim_ShouldPass() {
        // Given
        Victim mockVictim = new Victim();
        when(victimRepository.findByVictimIdCard(123456789L))
                .thenReturn(Optional.of(mockVictim));

        // When & Then
        assertDoesNotThrow(() ->
                interviewValidator.validateInterviewee("VICTIM", "123456789"));
        verify(victimRepository).findByVictimIdCard(123456789L);
    }

    @Test
    void validateInterviewee_ValidWitness_ShouldPass() {
        // Given
        Witness mockWitness = new Witness();
        when(witnessRepository.findByWitnessIdCard(123456789L))
                .thenReturn(Optional.of(mockWitness));

        // When & Then
        assertDoesNotThrow(() ->
                interviewValidator.validateInterviewee("WITNESS", "123456789"));
        verify(witnessRepository).findByWitnessIdCard(123456789L);
    }

    @Test
    void validateInterviewee_IntervieweeNotFound_ShouldThrowException() {
        // Given
        when(suspectRepository.findBySuspectIdCard(anyLong()))
                .thenReturn(Optional.empty());

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewValidator.validateInterviewee("SUSPECT", "123456789"));

        assertEquals(ErrorCode.INTERVIEWEE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void validateInterviewee_InvalidIdCardFormat_ShouldThrowException() {
        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewValidator.validateInterviewee("SUSPECT", "invalid"));

        assertEquals(ErrorCode.INVALID_KEY, exception.getErrorCode());
    }

    // ================= TIME CONFLICT VALIDATION TESTS =================

    @Test
    void validateTimeConflict_NoConflict_ShouldPass() {
        // Given
        when(interviewRepository.findConflictingInterviews(anyString(), any(), any()))
                .thenReturn(List.of());

        // When & Then
        assertDoesNotThrow(() ->
                interviewValidator.validateTimeConflict("interviewer123", validRequest));
        verify(interviewRepository).findConflictingInterviews(anyString(), any(), any());
    }

    @Test
    void validateTimeConflict_HasConflict_ShouldThrowException() {
        // Given
        Interview conflictInterview = new Interview();
        when(interviewRepository.findConflictingInterviews(anyString(), any(), any()))
                .thenReturn(List.of(conflictInterview));

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewValidator.validateTimeConflict("interviewer123", validRequest));

        assertEquals(ErrorCode.INTERVIEW_SCHEDULING_CONFLICT, exception.getErrorCode());
    }

    // ================= FILE VALIDATION TESTS =================

    @Test
    void validateFiles_ValidFiles_ShouldPass() {
        // Given
        MockMultipartFile validFile = new MockMultipartFile(
                "file", "test.pdf", "application/pdf", "test content".getBytes());
        List<MultipartFile> files = List.of(validFile);

        // When & Then
        assertDoesNotThrow(() -> interviewValidator.validateFiles(files));
    }

    @Test
    void validateFiles_EmptyFilesList_ShouldPass() {
        // When & Then
        assertDoesNotThrow(() -> interviewValidator.validateFiles(List.of()));
        assertDoesNotThrow(() -> interviewValidator.validateFiles(null));
    }

    @Test
    void validateFiles_FileTooLarge_ShouldThrowException() {
        // Given
        byte[] largeContent = new byte[51 * 1024 * 1024]; // 51MB
        MockMultipartFile largeFile = new MockMultipartFile(
                "file", "large.pdf", "application/pdf", largeContent);
        List<MultipartFile> files = List.of(largeFile);

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewValidator.validateFiles(files));

        assertEquals(ErrorCode.FILE_TOO_LARGE, exception.getErrorCode());
    }

    @Test
    void validateFiles_InvalidFileExtension_ShouldThrowException() {
        // Given
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file", "test.exe", "application/octet-stream", "test content".getBytes());
        List<MultipartFile> files = List.of(invalidFile);

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewValidator.validateFiles(files));

        assertEquals(ErrorCode.FILE_INVALID_EXTENSION, exception.getErrorCode());
    }

    @Test
    void validateFiles_NullFileName_ShouldThrowException() {
        // Given
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file", null, "application/pdf", "test content".getBytes());
        List<MultipartFile> files = List.of(invalidFile);

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewValidator.validateFiles(files));

        assertEquals(ErrorCode.INVALID_FILE_NAME, exception.getErrorCode());
    }

    // ================= GETTER METHODS TESTS =================

    @Test
    void getValidatedCase_ValidCaseId_ShouldReturnCase() {
        // Given
        when(caseRepository.findById("case123")).thenReturn(Optional.of(mockCase));

        // When
        Case result = interviewValidator.getValidatedCase("case123");

        // Then
        assertNotNull(result);
        assertEquals("case123", result.getCaseId());
        verify(caseRepository).findById("case123");
    }

    @Test
    void getValidatedCase_InvalidCaseId_ShouldThrowException() {
        // Given
        when(caseRepository.findById("invalid")).thenReturn(Optional.empty());

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewValidator.getValidatedCase("invalid"));

        assertEquals(ErrorCode.CASE_NOT_EXISTED, exception.getErrorCode());
    }

    @Test
    void getValidatedInterviewer_ValidId_ShouldReturnUser() {
        // Given
        when(userRepository.findByUsernameAndIsDeletedFalse("interviewer123"))
                .thenReturn(Optional.of(mockInterviewer));

        // When
        User result = interviewValidator.getValidatedInterviewer("interviewer123");

        // Then
        assertNotNull(result);
        assertEquals("interviewer123", result.getUsername());
    }

    @Test
    void getValidatedInterviewee_ValidSuspect_ShouldReturnSuspect() {
        // Given
        when(suspectRepository.findBySuspectIdCard(123456789L))
                .thenReturn(Optional.of(mockSuspect));

        // When
        Object result = interviewValidator.getValidatedInterviewee("SUSPECT", "123456789");

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Suspect);
        assertEquals("suspect123", ((Suspect) result).getSuspectId());
    }

    // ================= COMPREHENSIVE VALIDATION TEST =================

    @Test
    void validateCreateInterviewRequest_ValidRequest_ShouldPass() {
        // Given
        when(caseRepository.existsById("case123")).thenReturn(true);
        when(userRepository.findByUsernameAndIsDeletedFalse("interviewer123"))
                .thenReturn(Optional.of(mockInterviewer));
        when(suspectRepository.findBySuspectIdCard(123456789L))
                .thenReturn(Optional.of(mockSuspect));
        when(interviewRepository.findConflictingInterviews(anyString(), any(), any()))
                .thenReturn(List.of());

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", "application/pdf", "content".getBytes());

        // When & Then
        assertDoesNotThrow(() ->
                interviewValidator.validateCreateInterviewRequest("case123", validRequest, List.of(file)));
    }
}
