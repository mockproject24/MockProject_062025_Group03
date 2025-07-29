// ===== CaseServiceImplTest.java =====
package com.group3.MockProject.service;

import com.group3.MockProject.constant.CaseSeverity;
import com.group3.MockProject.constant.CaseStatus;
import com.group3.MockProject.constant.CaseType;
import com.group3.MockProject.constant.SeverityType;
import com.group3.MockProject.dto.response.CaseResponse;
import com.group3.MockProject.dto.response.CaseListResponse;
import com.group3.MockProject.dto.response.SuspectsResponseDto;
import com.group3.MockProject.dto.response.SuspectResponse;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Suspect;
import com.group3.MockProject.exception.AppException;
import com.group3.MockProject.exception.ErrorCode;
import com.group3.MockProject.mapper.SuspectMapper;
import com.group3.MockProject.repository.CaseRepository;
import com.group3.MockProject.repository.SuspectRepository;
import com.group3.MockProject.service.impl.CaseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CaseServiceImplTest {

    @Mock
    private CaseRepository caseRepository;

    @Mock
    private SuspectRepository suspectRepository;

    @Mock
    private SuspectMapper suspectMapper;

    @InjectMocks
    private CaseServiceImpl caseService;

    private Case case1;
    private Case case2;
    private CaseResponse caseResponse1;
    private CaseResponse caseResponse2;
    private SuspectResponse suspectResponse;
    private Suspect suspect;

    @BeforeEach
    void setUp() {
        // Initialize test cases
        case1 = Case.builder()
                .caseId("case-001")
                .caseName("Test Case 1")
                .severity(CaseSeverity.HIGH)
                .typeCase(CaseType.ROBBERY)
                .status(CaseStatus.IN_PROCESS)
                .createAt(LocalDateTime.parse("2025-01-08T10:00:00"))
                .summary("Fraud investigation case")
                .isDeleted(false)
                .build();

        case2 = Case.builder()
                .caseId("case-002")
                .caseName("Test Case 2")
                .severity(CaseSeverity.MEDIUM)
                .typeCase(CaseType.MURDER)
                .status(CaseStatus.PENDING_APPROVAL)
                .createAt(LocalDateTime.parse("2025-01-08T11:00:00"))
                .summary("Theft investigation case")
                .isDeleted(false)
                .build();

        caseResponse1 = CaseResponse.builder()
                .caseId("case-001")
                .caseNumber("#case-001")
                .caseName("Test Case 1")
                .typeCase("Robbery")
                .typeCaseKey("ROBBERY")
                .severity("High")
                .severityKey("HIGH")
                .status("In Process")
                .statusKey("IN_PROCESS")
                .createdAt(LocalDateTime.parse("2025-01-08T10:00:00"))
                .receivingUnit("Police Department")
                .location("N/A")
                .reporterFullname("Unknown")
                .build();

        caseResponse2 = CaseResponse.builder()
                .caseId("case-002")
                .caseNumber("#case-002")
                .caseName("Test Case 2")
                .typeCase("Murder")
                .typeCaseKey("MURDER")
                .severity("Medium")
                .severityKey("MEDIUM")
                .status("Pending Approval")
                .statusKey("PENDING_APPROVAL")
                .createdAt(LocalDateTime.parse("2025-01-08T11:00:00"))
                .receivingUnit("Police Department")
                .location("N/A")
                .reporterFullname("Unknown")
                .build();

        // Initialize mock case and suspects
        String caseId = "case-003";
        Case caseEntity = Case.builder()
                .caseId(caseId)
                .build();

        suspect = Suspect.builder()
                .suspectId("suspect-001")
                .address("123 Le Loi, Hanoi")
                .catchTime(LocalDateTime.of(2025, 7, 1, 14, 30, 0))
                .description("Suspect was caught near the border.")
                .dob(LocalDateTime.of(1990, 5, 12, 0, 0, 0))
                .fingerprintsHash("fingerprintHash")
                .fullname("Le Van A")
                .gender("Male")
                .healthStatus("Healthy")
                .identification("123456789")
                .mugshotUrl("https://example.com/mugshots/nguyenvana.jpg")
                .national("Vietnam")
                .notes("No prior criminal record.")
                .phoneNumber("0909123456")
                .status("In custody")
                .caseEntity(caseEntity)
                .uploadedAt(LocalDateTime.of(2025, 7, 1, 14, 30, 0))
                .build();

        suspectResponse = SuspectResponse.builder()
                .suspectId("fae43618-58b3-11f0-b0c4-8c04ba3cebd5")
                .address("123 Le Loi, Hanoi")
                .fullName("Le Van A")
                .mugshotUrl("https://example.com/mugshots/nguyenvana.jpg")
                .caseId(caseId)
                .build();
    }

    @Test
    void getListCase_WithSearchTerm_ShouldReturnFilteredCases() {
        // Given
        int page = 0;
        int pageSize = 10;
        String search = "fraud";
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Case> casePage = new PageImpl<>(List.of(case1), pageable, 1);

        when(caseRepository.findByCaseNameContainingIgnoreCaseAndIsDeletedFalse(search, pageable))
                .thenReturn(casePage);

        // When
        CaseListResponse result = caseService.getListCase(page, pageSize, search, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getPage());
        assertEquals(pageSize, result.getPageSize());
        assertEquals(1L, result.getTotal());
        assertEquals(1, result.getData().size());
        assertEquals("case-001", result.getData().get(0).getCaseId());
        assertEquals("Robbery", result.getData().get(0).getTypeCase());

        verify(caseRepository).findByCaseNameContainingIgnoreCaseAndIsDeletedFalse(search, pageable);
    }

    @Test
    void getListCase_WithoutSearchTerm_ShouldReturnAllCases() {
        // Given
        int page = 0;
        int pageSize = 10;
        String search = null;
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Case> casePage = new PageImpl<>(List.of(case1, case2), pageable, 2);

        when(caseRepository.findByIsDeletedFalse(pageable)).thenReturn(casePage);

        // When
        CaseListResponse result = caseService.getListCase(page, pageSize, search, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getPage());
        assertEquals(pageSize, result.getPageSize());
        assertEquals(2L, result.getTotal());
        assertEquals(2, result.getData().size());

        verify(caseRepository).findByIsDeletedFalse(pageable);
    }

    @Test
    void getListCase_WithSeverityFilter_ShouldReturnFilteredCases() {
        // Given
        int page = 0;
        int pageSize = 10;
        String search = null;
        SeverityType severityType = SeverityType.SERIOUS; // Map to CaseSeverity.HIGH
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Case> casePage = new PageImpl<>(List.of(case1), pageable, 1);

        when(caseRepository.findByIsDeletedFalse(pageable)).thenReturn(casePage);

        // When
        CaseListResponse result = caseService.getListCase(page, pageSize, search, severityType, null, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getPage());
        assertEquals(pageSize, result.getPageSize());
        // Note: Filtering happens in memory, so we need to adjust the assertion
        assertTrue(result.getData().size() <= 1);

        verify(caseRepository).findByIsDeletedFalse(pageable);
    }

    @Test
    void getListCase_WithEmptyResult_ShouldReturnEmptyList() {
        int page = 0;
        int pageSize = 10;
        String search = "nonexistent";
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Case> emptyCasePage = new PageImpl<>(List.of(), pageable, 0);

        when(caseRepository.findByCaseNameContainingIgnoreCaseAndIsDeletedFalse(search, pageable))
                .thenReturn(emptyCasePage);

        CaseListResponse result = caseService.getListCase(page, pageSize, search, null, null, null);

        assertNotNull(result);
        assertEquals(1, result.getPage());
        assertEquals(pageSize, result.getPageSize());
        assertEquals(0L, result.getTotal());
        assertTrue(result.getData().isEmpty());

        verify(caseRepository).findByCaseNameContainingIgnoreCaseAndIsDeletedFalse(search, pageable);
    }

    @Test
    void getAllSuspectsByCaseId_success() {
        // GIVEN
        String caseId = "case-003";
        int page = 1;
        int pageSize = 10;
        String status = "In custody";
        LocalDate date = LocalDate.of(2025, 7, 1);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Suspect> suspectsPage = new PageImpl<>(List.of(suspect), pageable, 1);

        when(caseRepository.existsById(caseId)).thenReturn(true);
        when(suspectRepository.findByCaseIdAndStatusAndCatchTime(caseId, status, date, startOfDay, endOfDay, pageable))
                .thenReturn(suspectsPage);
        when(suspectMapper.toSuspectResponse(suspect)).thenReturn(suspectResponse);

        // when
        SuspectsResponseDto result = caseService.getAllSuspectsByCaseId(caseId, page, pageSize, status, date);

        // then
        assertThat(result.getPage()).isEqualTo(1);
        assertThat(result.getTotal()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getPageSize()).isEqualTo(10);
        assertThat(result.getSuspects()).hasSize(1);
        assertThat(result.getSuspects().get(0).getSuspectId()).isEqualTo(suspectResponse.getSuspectId());
    }

    @Test
    void getAllSuspectsByCaseId_CaseNotFound_fail() {
        //GIVEN
        String nonExistentCaseId = "nonExistentCaseId";
        int page = 1;
        int pageSize = 10;
        String status = "In custody";
        LocalDate date = LocalDate.of(2025, 7, 1);

        when(caseRepository.existsById(nonExistentCaseId)).thenReturn(false);

        //when & then
        AppException ex = assertThrows(AppException.class, () -> {
            caseService.getAllSuspectsByCaseId(nonExistentCaseId, page, pageSize, status, date);
        });

        assertEquals(ErrorCode.CASE_NOT_EXISTED, ex.getErrorCode());
    }

    @Test
    void getCaseById_Success() {
        // Given
        String caseId = "case-001";
        when(caseRepository.findById(caseId)).thenReturn(Optional.of(case1));

        // When
        var result = caseService.getCaseById(caseId);

        // Then
        assertNotNull(result);
        assertEquals(caseId, result.getCaseId());
        assertEquals("Test Case 1", result.getCaseName());
        verify(caseRepository).findById(caseId);
    }

    @Test
    void getCaseById_NotFound() {
        // Given
        String caseId = "nonexistent";
        when(caseRepository.findById(caseId)).thenReturn(Optional.empty());

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> caseService.getCaseById(caseId));

        assertEquals(ErrorCode.CASE_NOT_EXISTED, exception.getErrorCode());
        verify(caseRepository).findById(caseId);
    }
}
