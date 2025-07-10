package com.group3.MockProject.service;

import com.group3.MockProject.dto.response.SuspectDto;
import com.group3.MockProject.dto.response.SuspectsResponseDto;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Suspect;
import com.group3.MockProject.exception.ResourceNotFoundException;
import com.group3.MockProject.mapper.SuspectMapper;
import com.group3.MockProject.repository.CaseRepository;
import com.group3.MockProject.repository.SuspectRepository;
import com.group3.MockProject.service.impl.CaseServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * CaseServiceImplTest
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 7/9/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/9/2025      DQMinh      Create
 */
@SpringBootTest
public class CaseServiceImplTest {
    @Autowired
    private CaseServiceImpl caseService;

    @MockitoBean
    private SuspectRepository suspectRepository;

    @MockitoBean
    private SuspectMapper suspectMapper;

    @MockitoBean
    private CaseRepository caseRepository;

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
    private SuspectsResponseDto suspectsResponseDto;

    @BeforeEach
    void setUp(){
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
    void getAllSuspectsByCaseId_success(){
        // GIVEN
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