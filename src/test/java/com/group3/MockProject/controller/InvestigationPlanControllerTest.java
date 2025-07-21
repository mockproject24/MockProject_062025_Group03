//package com.group3.MockProject.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.group3.MockProject.dto.response.InvestigationPlanResponse;
//import com.group3.MockProject.service.InvestigationService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.*;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//class InvestigationPlanControllerTest {
//
//    @Mock
//    private InvestigationService investigationService;
//
//    @InjectMocks
//    private InvestigationPlanController investigationPlanController;
//
//    private MockMvc mockMvc;
//
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//
//        mockMvc = MockMvcBuilders
//                .standaloneSetup(investigationPlanController)
//                .build();
//    }
//
//    @Test
//    void getInvestigations_success() throws Exception {
//        // Arrange
//        InvestigationPlanResponse dto = new InvestigationPlanResponse(
//                "plan-uuid-1",
//                "case-uuid-1",
//                "Low",
//                LocalDateTime.parse("2025-07-28T10:15:00"),
//                "Awaiting lab results.",
//                "IN_PROGRESS",
//                LocalDateTime.parse("2025-07-07T08:55:00"),
//                "Collect and send samples for lab testing.",
//                false
////                "john_doe"
//        );
//
//
//        Page<InvestigationPlanResponse> mockPage = new PageImpl<>(
//                List.of(dto),
//                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")),
//                1
//        );
//
//        when(investigationService.getInvestigations(any(Pageable.class)))
//                .thenReturn(mockPage);
//
//        // Act
//        var result = mockMvc.perform(
//                get("/api/investigation-plans")
//                        .param("page", "0")
//                        .param("size", "10")
//                        .param("sort", "createdAt,desc")
//                        .contentType(MediaType.APPLICATION_JSON)
//        );
//
//        // Assert
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("Success"))
//                .andExpect(jsonPath("$.result.content[0].investigationPlanId").value("plan-uuid-1"))
//                .andExpect(jsonPath("$.result.content[0].status").value("IN_PROGRESS"))
//                .andExpect(jsonPath("$.result.totalElements").value(1))
//                .andExpect(jsonPath("$.result.totalPages").value(1))
//                .andExpect(jsonPath("$.result.size").value(10))
//                .andExpect(jsonPath("$.result.number").value(0));
//    }
//
//    @Test
//    void getInvestigations_emptyResult() throws Exception {
//        // Arrange
//        Page<InvestigationPlanResponse> mockPage = new PageImpl<>(
//                Collections.emptyList(),
//                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")),
//                0
//        );
//
//        when(investigationService.getInvestigations(any(Pageable.class)))
//                .thenReturn(mockPage);
//
//        // Act
//        var result = mockMvc.perform(
//                get("/api/investigation-plans")
//                        .param("page", "0")
//                        .param("size", "10")
//                        .param("sort", "createdAt,desc")
//                        .contentType(MediaType.APPLICATION_JSON)
//        );
//
//        // Assert
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("Success"))
//                .andExpect(jsonPath("$.result.content").isEmpty())
//                .andExpect(jsonPath("$.result.totalElements").value(0))
//                .andExpect(jsonPath("$.result.totalPages").value(0))
//                .andExpect(jsonPath("$.result.size").value(10))
//                .andExpect(jsonPath("$.result.number").value(0));
//    }
//
//    @Test
//    void getInvestigations_internalServerError() throws Exception {
//        // Arrange
//        when(investigationService.getInvestigations(any(Pageable.class)))
//                .thenThrow(new RuntimeException("Unexpected error occurred"));
//
//        // Act
//        var result = mockMvc.perform(
//                get("/api/investigation-plans")
//                        .param("page", "0")
//                        .param("size", "10")
//                        .param("sort", "createdAt,desc")
//                        .contentType(MediaType.APPLICATION_JSON)
//        );
//
//        // Assert
//        result.andExpect(status().isInternalServerError());
//    }
//}
