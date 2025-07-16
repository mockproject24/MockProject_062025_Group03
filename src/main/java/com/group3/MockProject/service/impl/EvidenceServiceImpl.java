package com.group3.MockProject.service.impl;

import com.group3.MockProject.dto.request.CreateEvidenceRequest;
import com.group3.MockProject.dto.response.EvidenceResponse;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Evidence;
import com.group3.MockProject.exception.AppException;
import com.group3.MockProject.exception.ErrorCode;
import com.group3.MockProject.repository.CaseRepository;
import com.group3.MockProject.repository.EvidenceRepository;
import com.group3.MockProject.service.IEvidenceService;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * EvidenceServiceImpl
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
 * 7/9/2025      NGUYEN NGOC SY      Create
 */

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class EvidenceServiceImpl implements IEvidenceService {
    @Override
    public EvidenceResponse getEvidence(String caseId, String evidenceId) {
        Evidence evidence = evidenceRepository.findByCaseEntity_CaseIdAndEvidenceId(caseId, evidenceId)
                .orElseThrow(() -> new AppException(ErrorCode.EVIDENCE_NOT_FOUND));

        return toEvidenceResponse(evidence, evidence.getAttachFile());
    }

    CaseRepository caseRepository;
    EvidenceRepository evidenceRepository;

    @Value("${spring.upload-file.base-uri}")
    @NonFinal
    String baseURI;

    @Override
    public EvidenceResponse createEvidence(String caseId, CreateEvidenceRequest request, MultipartFile file) {
        try {
            var caseEntity = caseRepository.findById(caseId)
                    .orElseThrow(() -> new AppException(ErrorCode.CASE_NOT_EXISTED));

            String fileUrl = null;
            if (file != null && !file.isEmpty()) {
                String storedFileName = store(file);
                fileUrl = baseURI + storedFileName;
            }

            var evidence = toEvidenceEntity(request, fileUrl, caseEntity);
            evidence = evidenceRepository.save(evidence);

            return toEvidenceResponse(evidence, fileUrl);
        } catch (URISyntaxException | IOException e) {
            log.error("Error while storing file: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    private Evidence toEvidenceEntity(CreateEvidenceRequest request, String fileUrl, Case caseEntity) {
        return Evidence.builder()
                .description(request.getDescription())
                .currentLocation(request.getCurrentLocation())
                .attachFile(fileUrl)
                .collectedAt(request.getCollectedAt() != null ? request.getCollectedAt() : LocalDateTime.now())
                .evidenceType(request.getEvidenceType())
                .status("ACTIVE")
                .caseEntity(caseEntity)
                .isDeleted(false)
                .build();
    }

    private EvidenceResponse toEvidenceResponse(Evidence evidence, String fileUrl) {
        return EvidenceResponse.builder()
                .caseId(evidence.getCaseEntity().getCaseId())
                .evidenceId(evidence.getEvidenceId())
                .description(evidence.getDescription())
                .currentLocation(evidence.getCurrentLocation())
                .attachFile(fileUrl)
                .evidenceType(evidence.getEvidenceType())
                .collectedAt(evidence.getCollectedAt())
                .uploadedAt(Instant.now())
                .build();
    }

    private String store(MultipartFile file) throws URISyntaxException, IOException {
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("jpeg", "png", "jpg", "gif", "mp4", "pdf", "doc", "docx", "ppt", "pptx");

        boolean isValid = allowedExtensions.stream()
                .anyMatch(ext -> fileName.toLowerCase().endsWith(ext));

        if (!isValid) {
            throw new AppException(ErrorCode.FILE_INVALID_EXTENSION);
        }

        URI uri = new URI(baseURI + fileName);
        Path path = Paths.get(uri);

        try (InputStream is = file.getInputStream()) {
            Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
        }

        return fileName;
    }

    @Override
    public EvidenceResponse updateEvidence(String evidenceId, CreateEvidenceRequest request, MultipartFile file) {
        try {
            // Tìm evidence theo evidenceId
            var evidence = evidenceRepository.findById(evidenceId)
                    .orElseThrow(() -> new AppException(ErrorCode.EVIDENCE_NOT_FOUND));

            // Nếu có file mới, thực hiện lưu file và cập nhật đường dẫn
            String fileUrl = evidence.getAttachFile();
            if (file != null && !file.isEmpty()) {
                String storedFileName = store(file);
                fileUrl = baseURI + storedFileName;
            }

            // Cập nhật các trường thông tin
            evidence.setDescription(request.getDescription());
            evidence.setCurrentLocation(request.getCurrentLocation());
            evidence.setEvidenceType(request.getEvidenceType());
            evidence.setCollectedAt(request.getCollectedAt() != null ? request.getCollectedAt() : evidence.getCollectedAt());
            evidence.setAttachFile(fileUrl);

            evidence = evidenceRepository.save(evidence);

            return toEvidenceResponse(evidence, fileUrl);
        } catch (URISyntaxException | IOException e) {
            log.error("Error while updating evidence file: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }


}