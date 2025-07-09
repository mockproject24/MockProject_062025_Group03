package com.group3.MockProject.service.impl;

import com.group3.MockProject.dto.request.CreateEvidenceRequest;
import com.group3.MockProject.dto.response.EvidenceResponse;
import com.group3.MockProject.entity.Evidence;
import com.group3.MockProject.exception.ResourceNotFoundException;
import com.group3.MockProject.exception.StorageException;
import com.group3.MockProject.mapper.EvidenceMapper;
import com.group3.MockProject.repository.CaseRepository;
import com.group3.MockProject.repository.EvidenceRepository;
import com.group3.MockProject.service.EvidenceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
 * 7/9/2025      [Your Name]         Refactor file storage logic for correctness
 */

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class EvidenceServiceImpl implements EvidenceService {

    CaseRepository caseRepository;
    EvidenceRepository evidenceRepository;
    EvidenceMapper evidenceMapper;

    @Value("${spring.upload-file.base-uri:/uploads/}") // Sử dụng tên thuộc tính đầy đủ
    @NonFinal
    String baseURI;

    @Value("${file.upload-dir:./uploads}") // Thêm thuộc tính này
    @NonFinal
    String uploadDir;

    @Override
    public EvidenceResponse getEvidence(String caseId, String evidenceId) {
        caseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found with ID: " + caseId));

        Evidence evidence = evidenceRepository.findByCaseEntity_CaseIdAndEvidenceId(caseId, evidenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Evidence not found with ID: " + evidenceId + " for case ID: " + caseId));

        return evidenceMapper.toEvidenceResponse(evidence, evidence.getAttachFile());
    }

    @Override
    public EvidenceResponse createEvidence(String caseId, CreateEvidenceRequest request, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new StorageException("Attach file cannot be null or empty.");
        }

        var caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found with ID: " + caseId));

        String fileUrl;
        try {
            String storedFileName = store(file);
            fileUrl = baseURI + storedFileName;
        } catch (IOException e) {
            log.error("Failed to store file: {}", e.getMessage(), e);
            throw new StorageException("Failed to upload file due to IO error: " + e.getMessage());
        } catch (RuntimeException e) {
            log.error("Storage error: {}", e.getMessage(), e);
            throw e;
        }


        var evidence = evidenceMapper.toEvidenceEntity(request, fileUrl, caseEntity);
        evidence = evidenceRepository.save(evidence);

        return evidenceMapper.toEvidenceResponse(evidence, fileUrl);
    }

    private String store(MultipartFile file) throws IOException {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileName = UUID.randomUUID() + "_" + originalFilename;

        List<String> allowedExtensions = Arrays.asList("jpeg", "png", "jpg", "gif", "mp4", "pdf", "doc", "docx", "ppt", "pptx");
        String fileExtension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            fileExtension = originalFilename.substring(dotIndex + 1).toLowerCase();
        }

        if (!allowedExtensions.contains(fileExtension)) {
            throw new StorageException("Invalid file extension: " + fileExtension + ". Allowed extensions: " + allowedExtensions);
        }

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path targetLocation = uploadPath.resolve(fileName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        }

        return fileName;
    }
}