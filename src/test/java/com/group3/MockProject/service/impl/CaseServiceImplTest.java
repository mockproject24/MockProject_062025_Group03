package com.group3.MockProject.service.impl;

import com.group3.MockProject.constant.CaseSeverity;
import com.group3.MockProject.constant.CaseStatus;
import com.group3.MockProject.constant.CaseType;
import com.group3.MockProject.dto.response.CaseDetailDto;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Evidence;
import com.group3.MockProject.entity.Suspect;
import com.group3.MockProject.entity.Warrant;
import com.group3.MockProject.mapper.CaseMapper;
import com.group3.MockProject.mapper.SuspectMapper;
import com.group3.MockProject.repository.CaseRepository;
import com.group3.MockProject.repository.EvidenceRepository;
import com.group3.MockProject.repository.SuspectRepository;
import com.group3.MockProject.repository.WarrantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaseServiceImplTest {
    @Mock
    private CaseRepository caseRepository;
    @Mock
    private SuspectRepository suspectRepository;
    @Mock
    private WarrantRepository warrantRepository;
    @Mock
    private EvidenceRepository evidenceRepository;
    @Mock
    private CaseMapper caseMapper;
    @Mock
    private SuspectMapper suspectMapper;
    @InjectMocks
    private CaseServiceImpl caseService;

    private Case mockCase;
    private List<Suspect> mockSuspects;
    private List<Warrant> mockWarrants;
    private List<Evidence> mockEvidences;
    private CaseDetailDto mockCaseDetailDto;

    @BeforeEach
    void setUp() {
        // Setup mock case
        mockCase = new Case();
        mockCase.setCaseId("CASE-001");
        mockCase.setCaseName("Test Case");
        mockCase.setTypeCase(CaseType.MURDER);
        mockCase.setSeverity(CaseSeverity.HIGH);
        mockCase.setStatus(CaseStatus.IN_PROCESS);
        mockCase.setSummary("Test case summary");
        mockCase.setCreateAt(LocalDateTime.now());

        // Setup mock suspects
        mockSuspects = new ArrayList<>();
        Suspect suspect1 = new Suspect();
        suspect1.setSuspectId("SUSPECT-001");
        suspect1.setFullname("John Doe");
        mockSuspects.add(suspect1);

        // Setup mock warrants
        mockWarrants = new ArrayList<>();
        Warrant warrant1 = new Warrant();
        warrant1.setWarrantId("WARRANT-001");
        warrant1.setWarrantName("Search Warrant");
        mockWarrants.add(warrant1);

        // Setup mock evidences
        mockEvidences = new ArrayList<>();
        Evidence evidence1 = new Evidence();
        evidence1.setEvidenceId("EVIDENCE-001");
        evidence1.setDescription("Test evidence");
        mockEvidences.add(evidence1);

        // Setup mock response DTO
        mockCaseDetailDto = CaseDetailDto.builder()
                .caseId("CASE-001")
                .caseName("Test Case")
                .typeCase(CaseType.MURDER)
                .severity(CaseSeverity.HIGH)
                .status(CaseStatus.IN_PROCESS)
                .summary("Test case summary")
                .createAt(LocalDateTime.now())
                .tasks(new ArrayList<>())
                .suspects(new ArrayList<>())
                .warrants(new ArrayList<>())
                .evidences(new ArrayList<>())
                .build();
    }

    @Test
    void getCaseDetailById_Success() {
        String caseId = "CASE-001";
        when(caseRepository.findByIdWithDetails(caseId)).thenReturn(mockCase);
        when(suspectRepository.findByCaseEntityCaseId(caseId)).thenReturn(mockSuspects);
        when(warrantRepository.findByCaseEntityCaseId(caseId)).thenReturn(mockWarrants);
        when(evidenceRepository.findByCaseEntityCaseId(caseId)).thenReturn(mockEvidences);
        when(caseMapper.toCaseDetailDto(mockCase, mockSuspects, mockWarrants, mockEvidences))
                .thenReturn(mockCaseDetailDto);

        CaseDetailDto result = caseService.getCaseDetailById(caseId);

        assertNotNull(result);
        assertEquals("CASE-001", result.getCaseId());
        assertEquals("Test Case", result.getCaseName());
        assertEquals(CaseType.MURDER, result.getTypeCase());
        assertEquals(CaseSeverity.HIGH, result.getSeverity());
        assertEquals(CaseStatus.IN_PROCESS, result.getStatus());
    }

    @Test
    void getCaseDetailById_CaseNotFound_ThrowsRuntimeException() {
        String caseId = "NONEXISTENT-CASE";
        when(caseRepository.findByIdWithDetails(caseId)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            caseService.getCaseDetailById(caseId);
        });

        assertEquals("Case not found: NONEXISTENT-CASE", exception.getMessage());
        verify(caseRepository).findByIdWithDetails(caseId);
        verify(suspectRepository, never()).findByCaseEntityCaseId(anyString());
        verify(warrantRepository, never()).findByCaseEntityCaseId(anyString());
        verify(evidenceRepository, never()).findByCaseEntityCaseId(anyString());
        verify(caseMapper, never()).toCaseDetailDto(any(), any(), any(), any());
    }

    @Test
    void getCaseDetailById_NullCaseId_ThrowsRuntimeException() {
        when(caseRepository.findByIdWithDetails(null)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            caseService.getCaseDetailById(null);
        });

        assertEquals("Case not found: null", exception.getMessage());
        verify(caseRepository).findByIdWithDetails(null);
    }
}