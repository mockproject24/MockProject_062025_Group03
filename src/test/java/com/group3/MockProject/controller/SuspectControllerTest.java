package com.group3.MockProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group3.MockProject.dto.request.CreateSuspectRequest;
import com.group3.MockProject.dto.response.SuspectResponse;
import com.group3.MockProject.service.ISuspectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * SuspectControllerTest
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 09/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 09/07/2025     Hải Đăng      Create
 */

@SpringBootTest
@AutoConfigureMockMvc
public class SuspectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISuspectService suspectService;

    private CreateSuspectRequest request;
    private SuspectResponse suspectResponse;

    @BeforeEach
    void initData() {
        request = CreateSuspectRequest.builder()
                .fullName("Nguyễn Văn A")
                .address("123 Đường ABC")
                .moreInfo("Ghi chú về nghi phạm")
                .build();

        suspectResponse = SuspectResponse.builder()
                .caseId("d2a2ddea-5c77-11f0-82e0-0242ac110002")
                .fullName("Nguyễn Văn A")
                .address("123 Đường ABC")
                .moreInfo("Ghi chú về nghi phạm")
                .mugshotUrl("upload/1752300462187-TranNguyenHaiDang_CV.pdf")
                .uploadedAt(Instant.now())
                .build();
    }

    @Test
    void createSuspect_validRequest_Success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        MockMultipartFile jsonPart = new MockMultipartFile(
                "request",
                "request.json",
                "application/json",
                jsonRequest.getBytes()
        );

        // Tạo file part
        MockMultipartFile filePart = new MockMultipartFile(
                "file",
                "TranNguyenHaiDang_CV.pdf",
                "application/pdf",
                "fake-pdf-content".getBytes()
        );

        when(suspectService.createSuspect(anyString(), any(CreateSuspectRequest.class), any(MultipartFile.class)))
                .thenReturn(suspectResponse);

        String caseId = "d2a2ddea-5c77-11f0-82e0-0242ac110002";
        mockMvc.perform(MockMvcRequestBuilders.multipart("/cases/{caseId}/suspects", caseId)
                        .file(jsonPart)
                        .file(filePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath(  "code").value("201"))
                .andExpect(jsonPath("data.caseId").value(caseId));

    }

    @Test
    void createSuspect_invalidFullName_fail() throws Exception {
        // GIVEN - chuẩn bị dữ liệu
        request.setFullName("");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        MockMultipartFile jsonPart = new MockMultipartFile(
                "request",
                "request.json",
                "application/json",
                jsonRequest.getBytes()
        );

        // Tạo file part
        MockMultipartFile filePart = new MockMultipartFile(
                "file",
                "TranNguyenHaiDang_CV.pdf",
                "application/pdf",
                "fake-pdf-content".getBytes()
        );

        // WHEN, THEN
        String caseId = "d2a2ddea-5c77-11f0-82e0-0242ac110002";
        mockMvc.perform(MockMvcRequestBuilders.multipart("/cases/{caseId}/suspects", caseId)
                        .file(jsonPart)
                        .file(filePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(1102))
                .andExpect(jsonPath("message").value("Suspect full name is required and must not be blank"));
    }


}
