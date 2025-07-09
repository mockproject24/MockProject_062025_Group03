package com.group3.MockProject.service;

import com.group3.MockProject.constant.EvidenceType;
import com.group3.MockProject.dto.request.CreateEvidenceRequest;
import com.group3.MockProject.dto.response.EvidenceResponse;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Evidence;
import com.group3.MockProject.exception.ResourceNotFoundException;
import com.group3.MockProject.repository.CaseRepository;
import com.group3.MockProject.repository.EvidenceRepository;
import com.group3.MockProject.service.impl.EvidenceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class EvidenceServiceImplTest {

    @Mock
    private EvidenceRepository evidenceRepository;

    @Mock
    private CaseRepository caseRepository;

    @InjectMocks
    private EvidenceService evidenceService;

    private Case testCase;
    private CreateEvidenceRequest testRequest;
    private MockMultipartFile testFile;
    private Evidence testEvidence;

    @BeforeEach
    void setUp() throws Exception {
        testCase = new Case();
        testCase.setCaseId("case123");

        testRequest = CreateEvidenceRequest.builder()
                .description("Test mô tả")
                .currentLocation("Phòng A1")
                .collectedAt(LocalDateTime.of(2025, 7, 9, 10, 0))
                .evidenceType(EvidenceType.PHYSICAL_EVIDENCE)
                .build();

        testFile = new MockMultipartFile(
                "file",
                "evidence.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image-content".getBytes()
        );

        testEvidence = Evidence.builder()
                .evidenceId("evi001")
                .description(testRequest.getDescription())
                .currentLocation(testRequest.getCurrentLocation())
                .collectedAt(testRequest.getCollectedAt())
                .evidenceType(testRequest.getEvidenceType())
                .attachFile("http://localhost/uploads/evidence.jpg")
                .caseEntity(testCase)
                .build();

        // Gán baseURI giả định
        Field baseUriField = EvidenceServiceImpl.class.getDeclaredField("baseURI");
        baseUriField.setAccessible(true);
        baseUriField.set(evidenceService, "http://localhost/uploads/");
    }

    @Test
    void testCreateEvidence_Success() {
        // Arrange
        when(caseRepository.findById("case123")).thenReturn(Optional.of(testCase));
        when(evidenceRepository.save(any(Evidence.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        EvidenceResponse response = evidenceService.createEvidence("case123", testRequest, testFile);

        // Assert
        assertNotNull(response);
        assertEquals("case123", response.getCaseId());
        assertEquals(testRequest.getDescription(), response.getDescription());
        assertTrue(response.getAttachFile().contains("evidence.jpg"));
    }

    @Test
    void testGetEvidence_Success() {
        // Arrange
        when(caseRepository.findById("case123")).thenReturn(Optional.of(testCase));
        when(evidenceRepository.findByCaseEntity_CaseIdAndEvidenceId("case123", "evi001"))
                .thenReturn(Optional.of(testEvidence));

        // Act
        EvidenceResponse response = evidenceService.getEvidence("case123", "evi001");

        // Assert
        assertNotNull(response);
        assertEquals("case123", response.getCaseId());
        assertEquals("Test mô tả", response.getDescription());
    }

    @Test
    void testGetEvidence_NotFound() {
        when(caseRepository.findById("case123")).thenReturn(Optional.of(testCase));
        when(evidenceRepository.findByCaseEntity_CaseIdAndEvidenceId("case123", "notFoundId"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> evidenceService.getEvidence("case123", "notFoundId"));
    }

    @Test
    void testCreateEvidence_InvalidCaseId() {
        when(caseRepository.findById("invalidCase")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> evidenceService.createEvidence("invalidCase", testRequest, testFile));
    }

    @Test
    void testCreateEvidence_EmptyFile() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

        when(caseRepository.findById("case123")).thenReturn(Optional.of(testCase));

        Exception exception = assertThrows(RuntimeException.class, () ->
                evidenceService.createEvidence("case123", testRequest, emptyFile));

        assertTrue(exception.getMessage().contains("File is empty"));
    }
}
