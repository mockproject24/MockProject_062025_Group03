package com.group3.MockProject.service;

import com.group3.MockProject.constant.EvidenceType;
import com.group3.MockProject.dto.request.CreateEvidenceRequest;
import com.group3.MockProject.dto.response.EvidenceResponse;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Evidence;
import com.group3.MockProject.entity.User;
import com.group3.MockProject.exception.ResourceNotFoundException;
import com.group3.MockProject.repository.CaseRepository;
import com.group3.MockProject.repository.EvidenceRepository;
import com.group3.MockProject.repository.UserRepository;
import com.group3.MockProject.service.impl.EvidenceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EvidenceServiceImplTest {

    @Mock
    private EvidenceRepository evidenceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CaseRepository caseRepository;

    @InjectMocks
    private EvidenceServiceImpl evidenceService;

    private Case testCase;
    private CreateEvidenceRequest testRequest;
    private MockMultipartFile testFile;
    private Evidence testEvidence;
    private User testUser;

    @BeforeEach
    void setUp() throws Exception {
        testUser = new User();
        testUser.setFullName("Test User");

        testCase = new Case();
        testCase.setCaseId("case123");

        testRequest = CreateEvidenceRequest.builder()
                .description("Test mô tả")
                .currentLocation("Phòng A1")
                .collectedAt(LocalDateTime.of(2025, 7, 9, 10, 0))
                .evidenceType(EvidenceType.PHYSICAL_EVIDENCE)
                .build();

        testFile = new MockMultipartFile(
                "file", "evidence.jpg", MediaType.IMAGE_JPEG_VALUE,
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
                .user(testUser)
                .build();

        Field baseUriField = EvidenceServiceImpl.class.getDeclaredField("baseURI");
        baseUriField.setAccessible(true);
        baseUriField.set(evidenceService, "http://localhost/uploads/");

        // ✅ Mock SecurityContext để tránh NullPointerException
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

    }

    @Test
    void testCreateEvidence_Success() {
        when(caseRepository.findById("case123")).thenReturn(Optional.of(testCase));
        when(evidenceRepository.save(any(Evidence.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EvidenceResponse response = evidenceService.createEvidence("case123", testRequest, null);

        assertNotNull(response);
        assertEquals("case123", response.getCaseId());
        assertEquals("Test mô tả", response.getDescription());
    }

    @Test
    void testGetEvidence_Success() {
        when(evidenceRepository.findByCaseEntity_CaseIdAndEvidenceId("case123", "evi001"))
                .thenReturn(Optional.of(testEvidence));

        EvidenceResponse response = evidenceService.getEvidence("case123", "evi001");

        assertNotNull(response);
        assertEquals("case123", response.getCaseId());
        assertEquals("Test mô tả", response.getDescription());
        assertEquals("Test User", response.getCollector());
    }

    @Test
    void testGetEvidence_NotFound() {
        when(evidenceRepository.findByCaseEntity_CaseIdAndEvidenceId("case123", "notFound"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> evidenceService.getEvidence("case123", "notFound"));
    }

    @Test
    void testCreateEvidence_InvalidCaseId() {
        when(caseRepository.findById("invalidCase")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> evidenceService.createEvidence("invalidCase", testRequest, null));
    }

    @Test
    void testUpdateEvidence_NoFile_Success() {
        when(evidenceRepository.findById("evi001")).thenReturn(Optional.of(testEvidence));
        when(evidenceRepository.save(any(Evidence.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateEvidenceRequest updateRequest = CreateEvidenceRequest.builder()
                .description("Updated desc")
                .currentLocation("Updated location")
                .evidenceType(EvidenceType.DIGITAL_EVIDENCE)
                .collectedAt(LocalDateTime.of(2025, 7, 10, 8, 30))
                .build();

        EvidenceResponse response = evidenceService.updateEvidence("evi001", updateRequest, null);

        assertEquals("Updated desc", response.getDescription());
        assertEquals("Updated location", response.getCurrentLocation());
        assertEquals(EvidenceType.DIGITAL_EVIDENCE, response.getEvidenceType());
    }

    @Test
    void testUpdateEvidence_NotFound() {
        when(evidenceRepository.findById("notFound")).thenReturn(Optional.empty());

        CreateEvidenceRequest request = CreateEvidenceRequest.builder()
                .description("desc").currentLocation("loc")
                .evidenceType(EvidenceType.PHYSICAL_EVIDENCE).build();

        assertThrows(ResourceNotFoundException.class,
                () -> evidenceService.updateEvidence("notFound", request, null));
    }
}
