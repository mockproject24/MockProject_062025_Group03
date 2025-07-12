package com.group3.MockProject.service.impl;

import com.group3.MockProject.dto.request.CreateSuspectRequest;
import com.group3.MockProject.dto.response.SuspectResponse;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Suspect;
import com.group3.MockProject.exception.AppException;
import com.group3.MockProject.exception.ErrorCode;
import com.group3.MockProject.repository.CaseRepository;
import com.group3.MockProject.repository.SuspectRepository;
import com.group3.MockProject.service.ISuspectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * SuspectServiceImpl
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 04/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 04/07/2025   Hải Đăng      Create
 * 7/12/2025      Ngoc Nghia      update
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SuspectServiceImpl implements ISuspectService {

    private final SuspectRepository suspectRepository;
    private final CaseRepository caseRepository;

    @Value("${spring.upload-file.base-uri}")
    @NonFinal
    private String baseURI;

    @Override
    public SuspectResponse createSuspect(String caseId, CreateSuspectRequest request, MultipartFile file) {
        SuspectResponse response;
        try {
            var caseEntity = caseRepository.findById(caseId).orElseThrow(() ->
                    new AppException(ErrorCode.CASE_NOT_EXISTED));

            String mugshotUrl;
            if (file == null || file.isEmpty()) {
                throw new AppException(ErrorCode.FILE_EMPTY);
            } else {
                String fileName = store(file);
                mugshotUrl = baseURI + fileName;
            }
            var suspect = toSuspect(request, mugshotUrl, caseEntity);

            suspect = suspectRepository.save(suspect);

            response = toSuspectResponse(suspect);

        } catch (URISyntaxException | IOException e) {
            log.error("Lỗi khi lưu suspect hoặc upload file: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        return response;

    }


    private Suspect toSuspect(CreateSuspectRequest request, String mugshotUrl, Case caseEntity) {
        return Suspect.builder()
                .fullname(request.getFullName())
                .address(request.getAddress())
                .mugshotUrl(mugshotUrl)
                .notes(request.getMoreInfo())
                .caseEntity(caseEntity)
                .build();

    }

    private SuspectResponse toSuspectResponse(Suspect suspect) {
        return SuspectResponse.builder()
                .caseId(suspect.getCaseEntity().getCaseId())
                .fullName(suspect.getFullname())
                .address(suspect.getAddress())
                .moreInfo(suspect.getNotes())
                .mugshotUrl(suspect.getMugshotUrl())
                .uploadedAt(Instant.now())
                .build();
    }


    private String store(MultipartFile file) throws URISyntaxException, IOException {
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("jpeg", "png", "jpg", "gif", "mp4", "pdf", "fsd", "ai", "doc", "docx", "ppt", "pptx");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
        if (!isValid) {
            throw new AppException(ErrorCode.FILE_INVALID_EXTENSION);
        }
        URI uri = new URI(baseURI + fileName);
        Path path = Paths.get(uri);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        }
        return fileName;
    }
}

