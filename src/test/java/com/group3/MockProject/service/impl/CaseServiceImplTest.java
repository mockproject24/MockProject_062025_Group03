package com.group3.MockProject.service.impl;

import com.group3.MockProject.constant.CaseSeverity;
import com.group3.MockProject.constant.CaseStatus;
import com.group3.MockProject.constant.CaseType;
import com.group3.MockProject.dto.response.CaseDetailDto;
import com.group3.MockProject.dto.response.SuspectDto;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Evidence;
import com.group3.MockProject.entity.Suspect;
import com.group3.MockProject.entity.Warrant;
import com.group3.MockProject.exception.ResourceNotFoundException;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    // DQMinh CaseServiceImplTest
    private String caseId;
    private int page;
    private int pageSize;
    private String status;
    private LocalDate date;
    private LocalDateTime startOfDay;
    private LocalDateTime endOfDay;
    private Pageable pageable;

    private SuspectDto suspectDto;
    private Suspect suspect;
    private Page<Suspect> suspectsPage;

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


        // DQMinh CaseServiceImplTest
        caseId = "3b7f2d9e-4c6a-4f2e-9a1d-8e2b6c7f9a3d";
        page = 1;
        pageSize = 10;
        status = "In custody";
        date = LocalDate.of(2025,7,1);
        startOfDay = date.atStartOfDay();
        endOfDay = date.atTime(LocalTime.MAX);
        pageable = PageRequest.of(page-1,pageSize);

        Case caseEntity = Case.builder()
                .caseId(caseId)
                .build();
        suspect = Suspect.builder()
                .suspectId("fae43618-58b3-11f0-b0c4-8c04ba3cebd5")
                .address("123 Le Loi, Hanoi")
                .catchTime(LocalDateTime.of(2025,7,1,14, 30,0))
                .description("Suspect was caught near the border.")
                .dob(LocalDateTime.of(1990,5,12,0,0,0))
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
                .build();

        suspectDto = SuspectDto.builder()
                .suspectId("fae43618-58b3-11f0-b0c4-8c04ba3cebd5")
                .address("123 Le Loi, Hanoi")
                .catchTime(LocalDateTime.of(2025,7,1,14, 30,0))
                .description("Suspect was caught near the border.")
                .dob(LocalDateTime.of(1990,5,12,0,0,0))
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
                .caseId(caseId)
                .build();

        suspectsPage = new PageImpl<Suspect>(List.of(suspect), pageable, 1);

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

    @Test
    void getAllSuspectsByCaseId_success(){
        // GIVEN
        Mockito.when(caseRepository.existsById(caseId)).thenReturn(true);
        Mockito.when(suspectRepository.findByCaseIdAndStatusAndCatchTime(caseId,status,date,startOfDay,endOfDay,pageable))
                .thenReturn(suspectsPage);
        Mockito.when(suspectMapper.toSuspectDto(suspect)).thenReturn(suspectDto);
        // when
        var suspectsResponseDto = caseService.getAllSuspectsByCaseId(caseId, page,pageSize,status,date);

        // then

        assertThat(suspectsResponseDto.getPage()).isEqualTo(1);
        assertThat(suspectsResponseDto.getTotal()).isEqualTo(1);
        assertThat(suspectsResponseDto.getTotalPages()).isEqualTo(1);
        assertThat(suspectsResponseDto.getPageSize()).isEqualTo(10);
        assertThat(suspectsResponseDto.getSuspects().get(0)).isEqualTo(suspectDto);
    }

    @Test
    void getAllSuspectsByCaseId_CaseNotFound_fail(){
        //GIVEN
        String nonExistentCaseId = "nonExistentCaseId";
        Mockito.when(caseRepository.existsById(nonExistentCaseId))
                .thenReturn(false);
        String expectedMessage = "Case " + nonExistentCaseId + " not found";
        //when & then
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, ()->{
            caseService.getAllSuspectsByCaseId(nonExistentCaseId, page,pageSize,status,date);
        });

        assertEquals(expectedMessage, ex.getMessage());
    }
}