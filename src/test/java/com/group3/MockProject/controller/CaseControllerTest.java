package com.group3.MockProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.group3.MockProject.dto.response.ApiResponse;

import com.group3.MockProject.dto.response.SuspectResponse;
import com.group3.MockProject.dto.response.SuspectsResponseDto;

import com.group3.MockProject.service.ICaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * CaseControllerTest
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
@Slf4j
@SpringBootTest
// to create mock request to controller
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class CaseControllerTest {

    @Autowired
    private MockMvc mockMvc ;

    @MockitoBean
    private ICaseService caseService;

    private String caseId;
    private int page;
    private int pageSize;
    private String status;
    private LocalDate date;

    private SuspectsResponseDto suspectsResponseDto;
    private ApiResponse<?> apiResponse;
    @BeforeEach
    void setUp(){
        caseId = "3b7f2d9e-4c6a-4f2e-9a1d-8e2b6c7f9a3d";
        page = 1;
        pageSize = 10;
        status = "In custody";
        date = LocalDate.of(2025,7,1);

        SuspectResponse suspectResponse = SuspectResponse.builder()
                .suspectId("fae43618-58b3-11f0-b0c4-8c04ba3cebd5")
                .address("123 Le Loi, Hanoi")
                .fullName("Le Van A")
                .mugshotUrl("https://example.com/mugshots/nguyenvana.jpg")
                .caseId(caseId)
                .build();
        suspectsResponseDto = SuspectsResponseDto.builder()
                .page(page)
                .pageSize(pageSize)
                .totalPages(1)
                .total(1L)
                .suspects(List.of(suspectResponse))
                .build();
        apiResponse = ApiResponse.<SuspectsResponseDto>builder()
                .code(HttpStatus.OK.value())
                .message("Get suspects successfully")
                .result(suspectsResponseDto)
                .build();
    }

    @Test
    void getAllSuspects_success() throws Exception {

        // GIVEN
        Mockito.when(caseService.getAllSuspectsByCaseId(caseId, page, pageSize, status, date))
                .thenReturn(suspectsResponseDto);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String expectedResponseJson = objectMapper.writeValueAsString(apiResponse);

        //WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/cases/{caseId}/suspects", caseId)
                        .param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("status", status)
                        .param("day", date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponseJson));
    }
}
