package com.group3.MockProject.service;

import com.group3.MockProject.dto.request.CreateInterviewRequest;
import com.group3.MockProject.dto.request.QuestionRequest;
import com.group3.MockProject.dto.response.InterviewResponse;
import com.group3.MockProject.entity.*;
import com.group3.MockProject.exception.AppException;
import com.group3.MockProject.exception.ErrorCode;
import com.group3.MockProject.mapper.InterviewMapper;
import com.group3.MockProject.repository.InterviewFileRepository;
import com.group3.MockProject.repository.InterviewRepository;
import com.group3.MockProject.repository.QuestionRepository;
import com.group3.MockProject.service.impl.InterviewServiceImpl;
import com.group3.MockProject.util.FileUploadUtil;
import com.group3.MockProject.validator.InterviewValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * InterviewServiceImplTest
 * <p>
 * Unit tests for InterviewServiceImpl class (Refactored Version)
 * <p>
 * Version 2.0
 * Date: 7/29/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/28/2025      User      Create
 * 7/29/2025      Claude    Fix based on actual source code
 */
@ExtendWith(MockitoExtension.class)
class InterviewServiceImplTest {

    // Mock dependencies that match refactored InterviewServiceImpl exactly
    @Mock
    private InterviewRepository interviewRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private InterviewFileRepository interviewFileRepository;

    @Mock
    private InterviewValidator interviewValidator;

    @Mock
    private FileUploadUtil fileUploadUtil;

    @Mock
    private InterviewMapper interviewMapper;

    @InjectMocks
    private InterviewServiceImpl interviewService;

    // Test data
    private CreateInterviewRequest validRequest;
    private Case mockCase;
    private User mockInterviewer;
    private Suspect mockSuspect;
    private Interview mockInterview;
    private Question mockQuestion;
    private InterviewResponse mockResponse;

    @BeforeEach
    void setUp() {
        // Setup request with correct QuestionRequest fields
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setQuestion("What happened?");           // correct field name
        questionRequest.setAnswer("I saw the incident");
        questionRequest.setLevelOfTrust("a");                   // correct field name

        validRequest = new CreateInterviewRequest();
        validRequest.setStartTime(Instant.parse("2025-07-28T10:00:00Z"));
        validRequest.setEndTime(Instant.parse("2025-07-28T11:00:00Z"));
        validRequest.setLocation("Interview Room 1");
        validRequest.setInterviewerId("interviewer123");
        validRequest.setIntervieweeType("SUSPECT");
        validRequest.setIntervieweeIdCard("123456789");
        validRequest.setQuesAndAns(List.of(questionRequest));

        // Setup mock entities
        mockCase = new Case();
        mockCase.setCaseId("case123");

        mockInterviewer = new User();
        mockInterviewer.setUsername("interviewer123");
        mockInterviewer.setFullName("John Interviewer");

        mockSuspect = new Suspect();
        mockSuspect.setSuspectId("suspect123");
        mockSuspect.setFullname("Suspect Name");

        mockInterview = new Interview();
        mockInterview.setInterviewId(UUID.randomUUID().toString());
        mockInterview.setLocation("Interview Room 1");
        mockInterview.setStartTime(LocalDateTime.of(2025, 7, 28, 10, 0));
        mockInterview.setEndTime(LocalDateTime.of(2025, 7, 28, 11, 0));

        mockQuestion = new Question();
        mockQuestion.setQuestionId(UUID.randomUUID().toString());
        mockQuestion.setContent("What happened?");              // Entity uses "content"
        mockQuestion.setAnswer("I saw the incident");
        mockQuestion.setReliability(1.0f);

        mockResponse = InterviewResponse.builder()
                .startTime(LocalDateTime.of(2025, 7, 28, 10, 0))
                .endTime(LocalDateTime.of(2025, 7, 28, 11, 0))
                .location("Interview Room 1")
                .interviewerName("John Interviewer")
                .intervieweeType("SUSPECT")
                .intervieweeName("Suspect Name")
                .totalQuestions(1)
                .attachedFiles(List.of())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // ================= SUCCESS SCENARIOS =================

    @Test
    void createInterview_ValidRequestWithoutFiles_ShouldSucceed() {
        // Given
        setupValidationMocks();
        setupEntityCreationMocks();

        // When
        InterviewResponse result = interviewService.createInterview("case123", validRequest, null);

        // Then
        assertNotNull(result);
        assertEquals("Interview Room 1", result.getLocation());
        assertEquals("John Interviewer", result.getInterviewerName());
        assertEquals("SUSPECT", result.getIntervieweeType());
        assertEquals("Suspect Name", result.getIntervieweeName());
        assertEquals(1, result.getTotalQuestions());

        // Verify all steps were called in correct order based on refactored flow
        verify(interviewValidator).validateCreateInterviewRequest("case123", validRequest, null);
        verify(interviewValidator).getValidatedCase("case123");
        verify(interviewValidator).getValidatedInterviewer("interviewer123");
        verify(interviewValidator).getValidatedInterviewee("SUSPECT", "123456789");
        verify(interviewMapper).createInterviewEntity(validRequest, mockCase, mockInterviewer, mockSuspect);
        verify(interviewRepository).save(any(Interview.class));
        verify(interviewMapper).createQuestions(validRequest.getQuesAndAns(), mockInterview, mockInterviewer);
        verify(questionRepository).saveAll(anyList());
        verify(interviewMapper).buildInterviewResponse(mockInterview, mockInterviewer, mockSuspect, 1, List.of());
    }

    @Test
    void createInterview_ValidRequestWithFiles_ShouldSucceed() {
        // Given
        MockMultipartFile file1 = new MockMultipartFile("file1", "recording.mp3", "audio/mpeg", "audio content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file2", "notes.pdf", "application/pdf", "pdf content".getBytes());
        List<MultipartFile> files = List.of(file1, file2);

        setupValidationMocks();

        // Setup entity creation without pre-configured file response
        when(interviewMapper.createInterviewEntity(eq(validRequest), eq(mockCase), eq(mockInterviewer), eq(mockSuspect)))
                .thenReturn(mockInterview);
        when(interviewRepository.save(any(Interview.class))).thenReturn(mockInterview);

        when(interviewMapper.createQuestions(eq(validRequest.getQuesAndAns()), eq(mockInterview), eq(mockInterviewer)))
                .thenReturn(List.of(mockQuestion));
        when(questionRepository.saveAll(anyList())).thenReturn(List.of(mockQuestion));

        // Mock file upload - each file individually as per handleFileUploads() method
        when(fileUploadUtil.uploadSingleFile(file1)).thenReturn("unique-recording.mp3");
        when(fileUploadUtil.uploadSingleFile(file2)).thenReturn("unique-notes.pdf");

        // Mock InterviewFile repository save - must return saved entity
        InterviewFile mockInterviewFile1 = new InterviewFile();
        mockInterviewFile1.setAttachedFile("unique-recording.mp3");
        mockInterviewFile1.setInterview(mockInterview);

        InterviewFile mockInterviewFile2 = new InterviewFile();
        mockInterviewFile2.setAttachedFile("unique-notes.pdf");
        mockInterviewFile2.setInterview(mockInterview);

        when(interviewFileRepository.save(any(InterviewFile.class)))
                .thenReturn(mockInterviewFile1)
                .thenReturn(mockInterviewFile2);

        // Expected response with original filenames (as per handleFileUploads logic)
        List<String> originalFileNames = List.of("recording.mp3", "notes.pdf");
        InterviewResponse responseWithFiles = InterviewResponse.builder()
                .startTime(LocalDateTime.of(2025, 7, 28, 10, 0))
                .endTime(LocalDateTime.of(2025, 7, 28, 11, 0))
                .location("Interview Room 1")
                .interviewerName("John Interviewer")
                .intervieweeType("SUSPECT")
                .intervieweeName("Suspect Name")
                .totalQuestions(1)
                .attachedFiles(originalFileNames)
                .createdAt(LocalDateTime.now())
                .build();

        // Mock buildInterviewResponse with exact original filename list
        when(interviewMapper.buildInterviewResponse(eq(mockInterview), eq(mockInterviewer), eq(mockSuspect), eq(1), eq(originalFileNames)))
                .thenReturn(responseWithFiles);

        // When
        InterviewResponse result = interviewService.createInterview("case123", validRequest, files);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getAttachedFiles().size());
        assertTrue(result.getAttachedFiles().contains("recording.mp3"));
        assertTrue(result.getAttachedFiles().contains("notes.pdf"));

        // Verify file upload was called for each file
        verify(fileUploadUtil).uploadSingleFile(file1);
        verify(fileUploadUtil).uploadSingleFile(file2);
        verify(interviewFileRepository, times(2)).save(any(InterviewFile.class));
    }

    // ================= VALIDATION FAILURE SCENARIOS =================

    @Test
    void createInterview_ValidationFails_ShouldThrowException() {
        // Given
        doThrow(new AppException(ErrorCode.CASE_NOT_EXISTED))
                .when(interviewValidator).validateCreateInterviewRequest("case123", validRequest, null);

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewService.createInterview("case123", validRequest, null));

        assertEquals(ErrorCode.CASE_NOT_EXISTED, exception.getErrorCode());

        // Verify no further processing occurred
        verify(interviewValidator, never()).getValidatedCase(any());
        verify(interviewRepository, never()).save(any());
    }

    @Test
    void createInterview_CaseNotFound_ShouldThrowException() {
        // Given
        doNothing().when(interviewValidator).validateCreateInterviewRequest(any(), any(), any());
        when(interviewValidator.getValidatedCase("invalid")).thenThrow(new AppException(ErrorCode.CASE_NOT_EXISTED));

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewService.createInterview("invalid", validRequest, null));

        assertEquals(ErrorCode.CASE_NOT_EXISTED, exception.getErrorCode());
    }

    @Test
    void createInterview_InterviewerNotFound_ShouldThrowException() {
        // Given
        doNothing().when(interviewValidator).validateCreateInterviewRequest(any(), any(), any());
        when(interviewValidator.getValidatedCase("case123")).thenReturn(mockCase);
        when(interviewValidator.getValidatedInterviewer("invalid")).thenThrow(new AppException(ErrorCode.INTERVIEWER_NOT_FOUND));

        validRequest.setInterviewerId("invalid");

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewService.createInterview("case123", validRequest, null));

        assertEquals(ErrorCode.INTERVIEWER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void createInterview_IntervieweeNotFound_ShouldThrowException() {
        // Given
        doNothing().when(interviewValidator).validateCreateInterviewRequest(any(), any(), any());
        when(interviewValidator.getValidatedCase("case123")).thenReturn(mockCase);
        when(interviewValidator.getValidatedInterviewer("interviewer123")).thenReturn(mockInterviewer);
        when(interviewValidator.getValidatedInterviewee("SUSPECT", "invalid")).thenThrow(new AppException(ErrorCode.INTERVIEWEE_NOT_FOUND));

        validRequest.setIntervieweeIdCard("invalid");

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> interviewService.createInterview("case123", validRequest, null));

        assertEquals(ErrorCode.INTERVIEWEE_NOT_FOUND, exception.getErrorCode());
    }

    // ================= INTEGRATION FAILURE SCENARIOS =================

    @Test
    void createInterview_InterviewSaveFails_ShouldThrowException() {
        // Given
        setupValidationMocks();

        when(interviewMapper.createInterviewEntity(eq(validRequest), eq(mockCase), eq(mockInterviewer), eq(mockSuspect)))
                .thenReturn(mockInterview);
        when(interviewRepository.save(any(Interview.class))).thenThrow(new RuntimeException("Interview save failed"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> interviewService.createInterview("case123", validRequest, null));

        assertEquals("Interview save failed", exception.getMessage());

        // Verify processing stopped at interview save
        verify(interviewRepository).save(any(Interview.class));
        verify(questionRepository, never()).saveAll(any());
    }

    @Test
    void createInterview_QuestionsSaveFails_ShouldThrowException() {
        // Given
        setupValidationMocks();
        setupEntityCreationMocksPartial();

        when(questionRepository.saveAll(anyList())).thenThrow(new RuntimeException("Questions save failed"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> interviewService.createInterview("case123", validRequest, null));

        assertEquals("Questions save failed", exception.getMessage());

        // Verify interview was saved but questions failed
        verify(interviewRepository).save(any(Interview.class));
        verify(questionRepository).saveAll(anyList());
    }

    @Test
    void createInterview_FileUploadFails_ShouldThrowException() {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "test.mp3", "audio/mpeg", "content".getBytes());
        List<MultipartFile> files = List.of(file);

        setupValidationMocks();

        // Setup entity creation mocks
        when(interviewMapper.createInterviewEntity(eq(validRequest), eq(mockCase), eq(mockInterviewer), eq(mockSuspect)))
                .thenReturn(mockInterview);
        when(interviewRepository.save(any(Interview.class))).thenReturn(mockInterview);

        when(interviewMapper.createQuestions(eq(validRequest.getQuesAndAns()), eq(mockInterview), eq(mockInterviewer)))
                .thenReturn(List.of(mockQuestion));
        when(questionRepository.saveAll(anyList())).thenReturn(List.of(mockQuestion));

        // Mock file upload failure
        when(fileUploadUtil.uploadSingleFile(file)).thenThrow(new RuntimeException("File upload failed"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> interviewService.createInterview("case123", validRequest, files));

        assertEquals("File upload failed", exception.getMessage());

        // Verify processing stopped at file upload
        verify(fileUploadUtil).uploadSingleFile(file);
        verify(interviewFileRepository, never()).save(any(InterviewFile.class));
    }

    @Test
    void createInterview_EmptyFileSkipped_ShouldSucceed() {
        // Given
        MockMultipartFile validFile = new MockMultipartFile("file1", "document.pdf", "application/pdf", "content".getBytes());
        MockMultipartFile emptyFile = new MockMultipartFile("file2", "empty.txt", "text/plain", new byte[0]);
        List<MultipartFile> files = List.of(validFile, emptyFile);

        setupValidationMocks();

        // Setup entity creation without pre-configured response
        when(interviewMapper.createInterviewEntity(eq(validRequest), eq(mockCase), eq(mockInterviewer), eq(mockSuspect)))
                .thenReturn(mockInterview);
        when(interviewRepository.save(any(Interview.class))).thenReturn(mockInterview);

        when(interviewMapper.createQuestions(eq(validRequest.getQuesAndAns()), eq(mockInterview), eq(mockInterviewer)))
                .thenReturn(List.of(mockQuestion));
        when(questionRepository.saveAll(anyList())).thenReturn(List.of(mockQuestion));

        // Mock file upload for valid file only
        when(fileUploadUtil.uploadSingleFile(validFile)).thenReturn("unique-document.pdf");

        // Mock InterviewFile save
        InterviewFile mockInterviewFile = new InterviewFile();
        mockInterviewFile.setAttachedFile("unique-document.pdf");
        mockInterviewFile.setInterview(mockInterview);
        when(interviewFileRepository.save(any(InterviewFile.class))).thenReturn(mockInterviewFile);

        // Expected response with only the valid file (original filename)
        List<String> originalFileNames = List.of("document.pdf");
        InterviewResponse responseWithFile = InterviewResponse.builder()
                .startTime(LocalDateTime.of(2025, 7, 28, 10, 0))
                .endTime(LocalDateTime.of(2025, 7, 28, 11, 0))
                .location("Interview Room 1")
                .interviewerName("John Interviewer")
                .intervieweeType("SUSPECT")
                .intervieweeName("Suspect Name")
                .totalQuestions(1)
                .attachedFiles(originalFileNames)
                .createdAt(LocalDateTime.now())
                .build();

        when(interviewMapper.buildInterviewResponse(eq(mockInterview), eq(mockInterviewer), eq(mockSuspect), eq(1), eq(originalFileNames)))
                .thenReturn(responseWithFile);

        // When
        InterviewResponse result = interviewService.createInterview("case123", validRequest, files);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getAttachedFiles().size());
        assertEquals("document.pdf", result.getAttachedFiles().get(0));

        // Verify only non-empty file was processed
        verify(fileUploadUtil, times(1)).uploadSingleFile(validFile);
        verify(fileUploadUtil, never()).uploadSingleFile(emptyFile);
        verify(interviewFileRepository, times(1)).save(any(InterviewFile.class));
    }

    // ================= HELPER METHODS =================

    private void setupValidationMocks() {
        doNothing().when(interviewValidator).validateCreateInterviewRequest(eq("case123"), eq(validRequest), any());
        when(interviewValidator.getValidatedCase("case123")).thenReturn(mockCase);
        when(interviewValidator.getValidatedInterviewer("interviewer123")).thenReturn(mockInterviewer);
        when(interviewValidator.getValidatedInterviewee("SUSPECT", "123456789")).thenReturn(mockSuspect);
    }

    private void setupEntityCreationMocks() {
        when(interviewMapper.createInterviewEntity(eq(validRequest), eq(mockCase), eq(mockInterviewer), eq(mockSuspect)))
                .thenReturn(mockInterview);
        when(interviewRepository.save(any(Interview.class))).thenReturn(mockInterview);

        when(interviewMapper.createQuestions(eq(validRequest.getQuesAndAns()), eq(mockInterview), eq(mockInterviewer)))
                .thenReturn(List.of(mockQuestion));
        when(questionRepository.saveAll(anyList())).thenReturn(List.of(mockQuestion));

        when(interviewMapper.buildInterviewResponse(eq(mockInterview), eq(mockInterviewer), eq(mockSuspect), eq(1), eq(List.of())))
                .thenReturn(mockResponse);
    }

    private void setupEntityCreationMocksPartial() {
        when(interviewMapper.createInterviewEntity(eq(validRequest), eq(mockCase), eq(mockInterviewer), eq(mockSuspect)))
                .thenReturn(mockInterview);
        when(interviewRepository.save(any(Interview.class))).thenReturn(mockInterview);

        when(interviewMapper.createQuestions(eq(validRequest.getQuesAndAns()), eq(mockInterview), eq(mockInterviewer)))
                .thenReturn(List.of(mockQuestion));
        // Don't mock questionRepository.saveAll to allow failure
    }
}