package com.group3.MockProject.service;

import com.group3.MockProject.dto.response.CaseResponse;
import com.group3.MockProject.dto.response.CaseListResponse;
import com.group3.MockProject.dto.response.SuspectResponse;
import com.group3.MockProject.elasticsearch.document.EsCase;
import com.group3.MockProject.elasticsearch.service.CaseIndexService;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Suspect;
import com.group3.MockProject.mapper.CaseMapper;
import com.group3.MockProject.service.impl.CaseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaseServiceImplTest {

    @Mock
    private CaseIndexService caseIndexService;

    @Mock
    private CaseMapper caseMapper;

    @Mock
    private SearchHits<EsCase> searchHits;

    @Mock
    private SearchHit<EsCase> searchHit1;

    @Mock
    private SearchHit<EsCase> searchHit2;

    @InjectMocks
    private CaseServiceImpl caseService;

    private EsCase esCase1;
    private EsCase esCase2;
    private CaseResponse caseResponse1;
    private CaseResponse caseResponse2;


    private String caseId;
    private int page;
    private int pageSize;
    private String status;
    private LocalDate date;
    private LocalDateTime startOfDay;
    private LocalDateTime endOfDay;
    private Pageable pageable;

    private SuspectResponse suspectResponse;
    private Suspect suspect;
    private Page<Suspect> suspectsPage;

    @BeforeEach
    void setUp() {
        esCase1 = EsCase.builder()
                .caseId("case-001")
                .caseName("Test Case 1")
                .severityKey("HIGH")
                .severityLabel("High Priority")
                .typeCaseKey("FRAUD")
                .typeCaseLabel("Fraud Investigation")
                .statusKey("OPEN")
                .statusLabel("Open")
                .createAt("2025-01-08T10:00:00")
                .reporterFullname("John Doe")
                .caseLocation("Downtown")
                .build();

        esCase2 = EsCase.builder()
                .caseId("case-002")
                .caseName("Test Case 2")
                .severityKey("MEDIUM")
                .severityLabel("Medium Priority")
                .typeCaseKey("THEFT")
                .typeCaseLabel("Theft Investigation")
                .statusKey("PENDING")
                .statusLabel("Pending")
                .createAt("2025-01-08T11:00:00")
                .reporterFullname("Jane Smith")
                .caseLocation("Uptown")
                .build();

        caseResponse1 = CaseResponse.builder()
                .caseId("case-001")
                .caseNumber("#case-001")
                .typeCase("Fraud Investigation")
                .typeCaseKey("FRAUD")
                .severity("High Priority")
                .severityKey("HIGH")
                .status("Open")
                .statusKey("OPEN")
                .createdAt(LocalDateTime.parse("2025-01-08T10:00:00"))
                .receivingUnit("Local PD – Investigation Division")
                .location("Downtown")
                .reporterFullname("John Doe")
                .build();

        caseResponse2 = CaseResponse.builder()
                .caseId("case-002")
                .caseNumber("#case-002")
                .typeCase("Theft Investigation")
                .typeCaseKey("THEFT")
                .severity("Medium Priority")
                .severityKey("MEDIUM")
                .status("Pending")
                .statusKey("PENDING")
                .createdAt(LocalDateTime.parse("2025-01-08T11:00:00"))
                .receivingUnit("Local PD – Investigation Division")
                .location("Uptown")
                .reporterFullname("Jane Smith")
                .build();
        
        // initialize mock case and suspects
        caseId = "case-003";
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

        suspectResponse = SuspectResponse.builder()
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
    void getListCase_WithSearchTerm_ShouldReturnFilteredCases() {
        // Given
        int page = 0;
        int pageSize = 10;
        String search = "fraud";

        List<SearchHit<EsCase>> searchHitList = List.of(searchHit1);

        when(caseIndexService.searchCases(search, page, pageSize)).thenReturn(searchHits);
        when(searchHits.getSearchHits()).thenReturn(searchHitList);
        when(searchHits.getTotalHits()).thenReturn(1L);
        when(searchHit1.getContent()).thenReturn(esCase1);
        when(caseMapper.toDto(esCase1)).thenReturn(caseResponse1);

        // When
        CaseListResponse result = caseService.getListCase(page, pageSize, search);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getPage());
        assertEquals(pageSize, result.getPageSize());
        assertEquals(1L, result.getTotal());
        assertEquals(1, result.getData().size());
        assertEquals("case-001", result.getData().get(0).getCaseId());
        assertEquals("Fraud Investigation", result.getData().get(0).getTypeCase());

        verify(caseIndexService).searchCases(search, page, pageSize);
        verify(caseMapper).toDto(esCase1);
    }

    @Test
    void getListCase_WithoutSearchTerm_ShouldReturnAllCases() {
        // Given
        int page = 0;
        int pageSize = 10;
        String search = null;

        List<SearchHit<EsCase>> searchHitList = Arrays.asList(searchHit1, searchHit2);

        when(caseIndexService.searchCases(search, page, pageSize)).thenReturn(searchHits);
        when(searchHits.getSearchHits()).thenReturn(searchHitList);
        when(searchHits.getTotalHits()).thenReturn(2L);
        when(searchHit1.getContent()).thenReturn(esCase1);
        when(searchHit2.getContent()).thenReturn(esCase2);
        when(caseMapper.toDto(esCase1)).thenReturn(caseResponse1);
        when(caseMapper.toDto(esCase2)).thenReturn(caseResponse2);

        // When
        CaseListResponse result = caseService.getListCase(page, pageSize, search);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getPage());
        assertEquals(pageSize, result.getPageSize());
        assertEquals(2L, result.getTotal());
        assertEquals(2, result.getData().size());

        verify(caseIndexService).searchCases(search, page, pageSize);
        verify(caseMapper, times(2)).toDto(any(EsCase.class));
    }

    @Test
    void getListCase_WithEmptyResult_ShouldReturnEmptyList() {
        int page = 0;
        int pageSize = 10;
        String search = "nonexistent";

        List<SearchHit<EsCase>> emptySearchHitList = List.of();

        when(caseIndexService.searchCases(search, page, pageSize)).thenReturn(searchHits);
        when(searchHits.getSearchHits()).thenReturn(emptySearchHitList);
        when(searchHits.getTotalHits()).thenReturn(0L);

        CaseListResponse result = caseService.getListCase(page, pageSize, search);

        assertNotNull(result);
        assertEquals(1, result.getPage());
        assertEquals(pageSize, result.getPageSize());
        assertEquals(0L, result.getTotal());
        assertTrue(result.getData().isEmpty());

        verify(caseIndexService).searchCases(search, page, pageSize);
        verify(caseMapper, never()).toDto(any(EsCase.class));
    }

    @Test
    void getListCase_WithDifferentPageSize_ShouldReturnCorrectPagination() {
        // Given
        int page = 1;
        int pageSize = 5;
        String search = "";

        List<SearchHit<EsCase>> searchHitList = List.of(searchHit1);

        when(caseIndexService.searchCases(search, page, pageSize)).thenReturn(searchHits);
        when(searchHits.getSearchHits()).thenReturn(searchHitList);
        when(searchHits.getTotalHits()).thenReturn(10L);
        when(searchHit1.getContent()).thenReturn(esCase1);
        when(caseMapper.toDto(esCase1)).thenReturn(caseResponse1);

        // When
        CaseListResponse result = caseService.getListCase(page, pageSize, search);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getPage()); // page + 1
        assertEquals(5, result.getPageSize());
        assertEquals(10L, result.getTotal());
        assertEquals(1, result.getData().size());

        verify(caseIndexService).searchCases(search, page, pageSize);
    }
}