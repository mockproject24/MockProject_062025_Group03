package com.group3.MockProject.service;

import com.group3.MockProject.dto.request.CreateSuspectRequest;
import com.group3.MockProject.dto.response.SuspectResponse;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Suspect;
import com.group3.MockProject.exception.AppException;
import com.group3.MockProject.repository.CaseRepository;
import com.group3.MockProject.repository.SuspectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * SuspectServiceTest
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 11/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 11/07/2025   Hải Đăng      Create
 */

@SpringBootTest
public class SuspectServiceTest {


    @Autowired
    private ISuspectService suspectService;

    @MockBean
    private SuspectRepository suspectRepository;

    @MockBean
    private CaseRepository caseRepository;

    private CreateSuspectRequest request;
    private SuspectResponse suspectResponse;
    private Suspect suspect;

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

        suspect = Suspect.builder()
                .fullname("Nguyễn Văn A")
                .address("123 Đường ABC")
                .mugshotUrl("upload/1752300462187-TranNguyenHaiDang_CV.pdf")
                .notes("Ghi chú về nghi phạm")
                .build();

    }
    @Test
    void createSuspect_validRequest_success() {
        Case caseEntity = new Case();
        caseEntity.setCaseId("d2a2ddea-5c77-11f0-82e0-0242ac110002");
        when(caseRepository.findById(anyString())).thenReturn(Optional.of(caseEntity));
        when(suspectRepository.save(any())).thenReturn(suspect);

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "TranNguyenHaiDang_CV.pdf",
                "application/pdf",
                "fake content".getBytes()
        );

        var response = suspectService.createSuspect(caseEntity.getCaseId(), request, mockFile);

        assertThat(response.getCaseId()).isEqualTo("d2a2ddea-5c77-11f0-82e0-0242ac110002");
        assertThat(response.getFullName()).isEqualTo("Nguyễn Văn A");
        String mugshotUrl = response.getMugshotUrl();
        assertThat(mugshotUrl)
                .startsWith("upload/1752300462187-TranNguyenHaiDang_CV.pdf");

        assertThat(mugshotUrl)
                .endsWith("TranNguyenHaiDang_CV.pdf");

    }

    @Test
    void createSuspect_caseNotFound_fail() {
        String caseId = "d2a2ddea-5c77-11f0-82e0-0242ac110002";
        when(caseRepository.findById(anyString())).thenReturn(Optional.empty());
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "TranNguyenHaiDang_CV.pdf",
                "application/pdf",
                "fake content".getBytes()
        );

        var exception = assertThrows(AppException.class, () ->
                suspectService.createSuspect(caseId, request, mockFile));
        assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
     assertThat(exception.getMessage()).isEqualTo("Case not existed");
    }
}
