package com.group3.MockProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.group3.MockProject.dto.request.CreateRecordInfoRequest;
import com.group3.MockProject.dto.response.ApiResponse;
import com.group3.MockProject.dto.response.RecordInfoResponse;
import com.group3.MockProject.dto.response.UserResponseDto;
import com.group3.MockProject.service.ICaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CaseControllerTest {

    @Mock
    private ICaseService caseService;

    @InjectMocks
    private CaseController caseController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @RestControllerAdvice
    static class GlobalExceptionHandler {
        @ExceptionHandler(ResourceNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ApiResponse handleNotFound(ResourceNotFoundException ex) {
            return new ApiResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ApiResponse handleValidation(MethodArgumentNotValidException ex) {
            return new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Invalid request body", null);
        }

        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public ApiResponse handleGeneral(Exception ex) {
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), null);
        }
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders
                .standaloneSetup(caseController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // getAssignedOfficers Tests
    @Test
    void getAssignedOfficers_success() throws Exception {
        String caseId = "CASE001";
        UserResponseDto officer = new UserResponseDto();
        officer.setUsername("john_doe");
        officer.setFullname("John Doe");
        officer.setAvatarUrl("https://example.com/avatar/john.jpg");
        officer.setPhoneNumber("0909123456");
        officer.setRole("OFFICER");

        Page<UserResponseDto> mockPage =
                new PageImpl<>(List.of(officer), PageRequest.of(0, 10), 1);

        when(caseService.getAssignedOfficers(eq(caseId), any(PageRequest.class)))
                .thenReturn(mockPage);

        var result = mockMvc.perform(get("/api/cases/{caseId}/assigned-officers?page=0&pageSize=10", caseId)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Assigned officers retrieved successfully"))
                .andExpect(jsonPath("$.result.content[0].username").value("john_doe"))
                .andExpect(jsonPath("$.result.content[0].fullname").value("John Doe"))
                .andExpect(jsonPath("$.result.content[0].avatarUrl").value("https://example.com/avatar/john.jpg"))
                .andExpect(jsonPath("$.result.content[0].phoneNumber").value("0909123456"))
                .andExpect(jsonPath("$.result.content[0].role").value("OFFICER"))
                .andExpect(jsonPath("$.result.totalElements").value(1))
                .andExpect(jsonPath("$.result.totalPages").value(1));
    }

    @Test
    void getAssignedOfficers_emptyResult() throws Exception {
        String caseId = "CASE001";
        Page<UserResponseDto> emptyPage =
                new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);

        when(caseService.getAssignedOfficers(eq(caseId), any(PageRequest.class)))
                .thenReturn(emptyPage);

        var result = mockMvc.perform(get("/api/cases/{caseId}/assigned-officers?page=0&pageSize=10", caseId)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Assigned officers retrieved successfully"))
                .andExpect(jsonPath("$.result.content").isArray())
                .andExpect(jsonPath("$.result.content").isEmpty())
                .andExpect(jsonPath("$.result.totalElements").value(0))
                .andExpect(jsonPath("$.result.totalPages").value(0));
    }

    @Test
    void getAssignedOfficers_caseNotFound() throws Exception {
        String caseId = "INVALID_CASE";

        when(caseService.getAssignedOfficers(eq(caseId), any(PageRequest.class)))
                .thenThrow(new ResourceNotFoundException("Case not found"));

        var result = mockMvc.perform(get("/api/cases/{caseId}/assigned-officers?page=0&pageSize=10", caseId)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Case not found"));
    }

    @Test
    void getAssignedOfficers_invalidPage() throws Exception {
        String caseId = "CASE001";

        var result = mockMvc.perform(get("/api/cases/{caseId}/assigned-officers?page=-1&pageSize=10", caseId)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Page and pageSize must be greater than 0"));
    }

    @Test
    void getAssignedOfficers_invalidPageSize() throws Exception {
        String caseId = "CASE001";

        var result = mockMvc.perform(get("/api/cases/{caseId}/assigned-officers?page=0&pageSize=0", caseId)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Page and pageSize must be greater than 0"));
    }

    @Test
    void getAssignedOfficers_internalServerError() throws Exception {
        String caseId = "CASE001";

        when(caseService.getAssignedOfficers(eq(caseId), any(PageRequest.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        var result = mockMvc.perform(get("/api/cases/{caseId}/assigned | officers?page=0&pageSize=10", caseId)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.message").value("Error retrieving assigned officers: Unexpected error"));
    }

    // createRecord Tests
//    @Test
//    void createRecord_success() throws Exception {
//        String caseId = "550e8400-e29b-41d4-a716-446655440000";
//        CreateRecordInfoRequest requestDto = buildValidRecordDto();
//
//        RecordInfoResponse responseDto = new RecordInfoResponse();
//        responseDto.setRecordInfoId("record-001");
//        responseDto.setTypeName(requestDto.getTypeName());
//        responseDto.setSource(requestDto.getSource());
//        responseDto.setDateCollected(requestDto.getDateCollected());
//        responseDto.setSummary(requestDto.getSummary());
////        responseDto.setIsDele(requestDto.getIsDeleted());
////        responseDto.setEvidenceId(null);
//
//        when(caseService.createRecord(eq(caseId), any(CreateRecordInfoRequest.class)))
//                .thenReturn(responseDto);
//
//        var result = mockMvc.perform(post("/api/cases/{caseId}/records", caseId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(requestDto)));
//
//        result.andExpect(status().isCreated())
//                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
//                .andExpect(jsonPath("$.message").value("Record created successfully"))
//                .andExpect(jsonPath("$.result.recordInfoId").value("record-001"))
//                .andExpect(jsonPath("$.result.typeName").value("Interview"))
//                .andExpect(jsonPath("$.result.source").value("Witness Statement"))
//                .andExpect(jsonPath("$.result.summary").value("Initial witness interview"))
//                .andExpect(jsonPath("$.result.isDeleted").value(false))
//                .andExpect(jsonPath("$.result.evidenceId").value("null"));
//    }

    @Test
    void createRecord_invalidRequestBody() throws Exception {
        String caseId = "550e8400-e29b-41d4-a716-446655440000";
        CreateRecordInfoRequest invalidDto = new CreateRecordInfoRequest();

        var result = mockMvc.perform(post("/api/cases/{caseId}/records", caseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)));

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Invalid request body"));
    }

    @Test
    void createRecord_caseNotFound() throws Exception {
        String caseId = "INVALID_CASE";
        CreateRecordInfoRequest requestDto = buildValidRecordDto();

        when(caseService.createRecord(eq(caseId), any(CreateRecordInfoRequest.class)))
                .thenThrow(new ResourceNotFoundException("Case not found"));

        var result = mockMvc.perform(post("/api/cases/{caseId}/records", caseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Case not found"));
    }

    @Test
    void createRecord_isDeletedTrue_shouldReturnError() throws Exception {
        String caseId = "550e8400-e29b-41d4-a716-446655440000";
        CreateRecordInfoRequest requestDto = buildValidRecordDto();
        requestDto.setIsDeleted(true);

        when(caseService.createRecord(eq(caseId), any(CreateRecordInfoRequest.class)))
                .thenThrow(new IllegalArgumentException("Cannot create deleted record"));

        var result = mockMvc.perform(post("/api/cases/{caseId}/records", caseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Cannot create deleted record"));
    }

    @Test
    void createRecord_internalServerError() throws Exception {
        String caseId = "550e8400-e29b-41d4-a716-446655440000";
        CreateRecordInfoRequest requestDto = buildValidRecordDto();

        when(caseService.createRecord(eq(caseId), any(CreateRecordInfoRequest.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        var result = mockMvc.perform(post("/api/cases/{caseId}/records", caseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.message").value("Error creating record: Unexpected error"));
    }

    private CreateRecordInfoRequest buildValidRecordDto() {
        CreateRecordInfoRequest dto = new CreateRecordInfoRequest();
        dto.setTypeName("Interview");
        dto.setSource("Witness Statement");
        dto.setDateCollected(LocalDate.of(2025,12,12));
        dto.setSummary("Initial witness interview");
        dto.setIsDeleted(false);
        return dto;
    }
}