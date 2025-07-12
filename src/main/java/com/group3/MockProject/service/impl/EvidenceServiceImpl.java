package com.group3.MockProject.service.impl;

import com.group3.MockProject.dto.request.CreateEvidenceRequest;
import com.group3.MockProject.dto.response.EvidenceResponse;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Evidence;
import com.group3.MockProject.entity.User;
import com.group3.MockProject.exception.ResourceNotFoundException;
import com.group3.MockProject.exception.StorageException;
import com.group3.MockProject.repository.CaseRepository;
import com.group3.MockProject.repository.EvidenceReposit
import com.group3.MockProject.repository.UserRepository;
import com.group3.MockProject.service.EvidenceService;
import com.group3.MockProject.service.IEvidenceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
public class EvidenceServiceImpl implements EvidenceService {

    CaseRepository caseRepository;
    EvidenceRepository evidenceRepository;
    UserRepository userRepository;
    @Override
    public EvidenceResponse getEvidence(String caseId, String evidenceId) {
        Evidence evidence = evidenceRepository.findByCaseEntity_CaseIdAndEvidenceId(caseId, evidenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Evidence not found with ID: " + evidenceId));

        return toEvidenceResponse(evidence, evidence.getAttachFile());
    }

    @Value("${file.upload-dir}")
    @NonFinal
    String baseURI;

    @Override
    public EvidenceResponse createEvidence(String caseId, CreateEvidenceRequest request, MultipartFile file) {
        try {
            var caseEntity = caseRepository.findById(caseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Case not found with ID: " + caseId));

            String fileUrl = null;
            if (file != null && !file.isEmpty()) {
                String storedFileName = store(file);
                fileUrl = baseURI + storedFileName;
            }

            var user = userRepository.findByUsername("sybanh")
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            var evidence = toEvidenceEntity(request, fileUrl, caseEntity,user);
            evidence = evidenceRepository.save(evidence);

            return toEvidenceResponse(evidence, fileUrl);
        } catch (IOException e) {
            log.error("Error while storing file: {}", e.getMessage(), e);
            throw new StorageException("Failed to upload file");
        }
    }

    private Evidence toEvidenceEntity(CreateEvidenceRequest request, String fileUrl, Case caseEntity, User user) {
        return Evidence.builder()
                .description(request.getDescription())
                .currentLocation(request.getCurrentLocation())
                .attachFile(fileUrl)
                .collectedAt(request.getCollectedAt() != null ? request.getCollectedAt() : LocalDateTime.now())
                .evidenceType(request.getEvidenceType())
                .status(request.getStatus() != null ? request.getStatus() : "Waiting for Test")
                .caseEntity(caseEntity)
                .user(user)
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
                .collector(evidence.getUser().getFullName())
                .status(evidence.getStatus())
                .build();
    }


    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            "jpeg", "png", "jpg", "gif", "mp4", "pdf", "doc", "docx", "ppt", "pptx"
    );

    private String store(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new StorageException("Tập tin trống, không thể lưu.");
        }

        // Lấy tên file gốc và chuẩn hóa
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFilename.contains("..")) {
            throw new StorageException("Tên file không hợp lệ: " + originalFilename);
        }

        // Lấy phần mở rộng
        String fileExtension = getFileExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
            throw new StorageException("Định dạng không hợp lệ. Cho phép: " + ALLOWED_EXTENSIONS);
        }

        // Tạo tên file duy nhất
        String fileName = System.currentTimeMillis() + "-" + originalFilename;

        // Tạo đường dẫn đầy đủ
        Path destinationPath = Paths.get(baseURI).toAbsolutePath().normalize().resolve(fileName);

        // Tạo thư mục nếu chưa có
        Files.createDirectories(destinationPath.getParent());

        // Ghi file
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Không thể lưu tập tin: " + fileName);
        }

        return fileName;
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex >= 0) ? fileName.substring(dotIndex + 1) : "";
    }


    private User getAuthorizedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // từ token
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }


    @Override
    public EvidenceResponse updateEvidence(String evidenceId, CreateEvidenceRequest request, MultipartFile file) {
        try {
            // Tìm evidence theo evidenceId
            var evidence = evidenceRepository.findById(evidenceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Evidence not found with ID: " + evidenceId));

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
        } catch (IOException e) {
            log.error("Error while updating evidence file: {}", e.getMessage(), e);
            throw new StorageException("Failed to update evidence");
        }
    }


}