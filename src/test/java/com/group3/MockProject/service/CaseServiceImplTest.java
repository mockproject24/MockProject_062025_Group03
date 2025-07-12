package com.group3.MockProject.service;

import com.group3.MockProject.dto.response.CaseDto;
import com.group3.MockProject.dto.response.CaseListDto;
import com.group3.MockProject.elasticsearch.document.EsCase;
import com.group3.MockProject.elasticsearch.service.CaseIndexService;
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

import java.time.LocalDateTime;
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
    private CaseDto caseDto1;
    private CaseDto caseDto2;

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

        caseDto1 = CaseDto.builder()
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

        caseDto2 = CaseDto.builder()
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
        when(caseMapper.toDto(esCase1)).thenReturn(caseDto1);

        // When
        CaseListDto result = caseService.getListCase(page, pageSize, search);

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
        when(caseMapper.toDto(esCase1)).thenReturn(caseDto1);
        when(caseMapper.toDto(esCase2)).thenReturn(caseDto2);

        // When
        CaseListDto result = caseService.getListCase(page, pageSize, search);

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

        CaseListDto result = caseService.getListCase(page, pageSize, search);

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
        when(caseMapper.toDto(esCase1)).thenReturn(caseDto1);

        // When
        CaseListDto result = caseService.getListCase(page, pageSize, search);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getPage()); // page + 1
        assertEquals(5, result.getPageSize());
        assertEquals(10L, result.getTotal());
        assertEquals(1, result.getData().size());

        verify(caseIndexService).searchCases(search, page, pageSize);
    }
}