package com.group3.MockProject.service.impl;

import com.group3.MockProject.dto.request.CreateInvestigationRequest;
import com.group3.MockProject.dto.response.InvestigationFile;
import com.group3.MockProject.dto.response.InvestigationPlanProjection;
import com.group3.MockProject.dto.response.InvestigationPlanResponse;
import com.group3.MockProject.dto.response.InvestigationResponse;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.InvestigationPlan;
import com.group3.MockProject.exception.AppException;
import com.group3.MockProject.exception.ErrorCode;
import com.group3.MockProject.repository.CaseRepository;
import com.group3.MockProject.repository.InvestigationPlanRepository;
import com.group3.MockProject.service.InvestigationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * InvestigationServiceImpl
 * <p>
 * Provides business logic for managing  details.
 * <p>
 * Version 1.0
 * <p>
 * Date: 04/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE        AUTHOR        DESCRIPTION
 * -------------------------------------------------------------
 * 04/07/2025        Nguyễn Bảo Kha        Create
 */

@Service
@Slf4j
public class InvestigationServiceImpl implements InvestigationService {
    @Autowired
    private InvestigationPlanRepository investigationPlanRepository;

    @Autowired
    private CaseRepository caseRepository;

    @Value("${spring.upload-file.base-uri}")
    private String baseURI;

    // Constants for file upload
    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList(
            "png", "jpg", "jpeg", "gif", "pdf", "doc", "docx", "mp4", "avi", "mov"
    );
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

    @Override
    public Page<InvestigationPlanResponse> getInvestigations(Pageable pageable) {
        Page<InvestigationPlanProjection> projections = investigationPlanRepository.findAllInvestigationPlans(pageable);
        return projections.map(p -> new InvestigationPlanResponse(
                p.getInvestigationPlanId(),
                p.getCaseId(),
                p.getTypeOfCrime(),
                p.getLevelSeverity(),
                p.getDate(),
                p.getReporter(),
                p.getLocation(),
                p.getStatus()
        ));
    }

    @Override
    public InvestigationResponse createInvestigation(String caseId, CreateInvestigationRequest request, List<MultipartFile> files) {
        log.info("Starting investigation creation for case: {}", caseId);

        try {
            // Validate case exists
            Case caseEntity = caseRepository.findById(caseId)
                    .orElseThrow(() -> new AppException(ErrorCode.CASE_NOT_EXISTED));

            // Upload files if provided
            List<InvestigationFile> uploadedFiles = uploadFiles(files);

            // Build response
            InvestigationResponse response = InvestigationResponse.builder()
                    .type(request.getType())
                    .analysist(request.getAnalysist())
                    .files(uploadedFiles)
                    .build();

            log.info("Investigation created successfully for case: {}", caseId);
            return response;

        } catch (Exception e) {
            log.error("Error creating investigation for case {}: {}", caseId, e.getMessage(), e);
            throw new AppException(ErrorCode.INVESTIGATION_PLAN_CREATION_FAILED, "Failed to create investigation: " + e.getMessage(), e);
        }
    }

    /**
     * Upload multiple files and return file information
     */
    private List<InvestigationFile> uploadFiles(List<MultipartFile> files) {
        List<InvestigationFile> fileResults = new ArrayList<>();

        if (files == null || files.isEmpty()) {
            log.info("No files provided for upload");
            return fileResults;
        }

        log.info("Uploading {} files", files.size());

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    String savedFileName = uploadSingleFile(file);
                    String fileUrl = baseURI + savedFileName;

                    InvestigationFile fileDto = InvestigationFile.builder()
                            .filename(file.getOriginalFilename())
                            .url(fileUrl)
                            .build();

                    fileResults.add(fileDto);
                    log.info("File uploaded successfully: {}", savedFileName);

                } catch (Exception e) {
                    log.error("Failed to upload file: {}", file.getOriginalFilename(), e);
                    throw new AppException(ErrorCode.FILE_EMPTY);
                }
            }
        }

        return fileResults;
    }

    /**
     * Upload single file
     */
    private String uploadSingleFile(MultipartFile file) throws URISyntaxException, IOException {
        // Validate file before upload
        validateFileBeforeUpload(file);

        // Create unique filename
        String uniqueFileName = createUniqueFileName(file.getOriginalFilename());

        // Parse baseURI to get the actual directory path
        String uploadDirectory = extractDirectoryFromBaseURI(baseURI);

        // Create upload directory if not exists
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("Created upload directory: {}", uploadPath.toAbsolutePath());
        }

        // Save file to the configured directory
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("File saved to: {}", filePath.toAbsolutePath());
        return uniqueFileName;
    }

    /**
     * Validate file before upload
     */
    private void validateFileBeforeUpload(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_FILE_NAME);
        }

        // Check file extension
        String fileExtension = getFileExtension(originalFileName);
        if (!ALLOWED_FILE_TYPES.contains(fileExtension.toLowerCase())) {
            throw new AppException(ErrorCode.FILE_INVALID_EXTENSION);
        }

        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new AppException(ErrorCode.FILE_TOO_LARGE);
        }
    }

    /**
     * Get file extension
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }

    /**
     * Create unique filename to avoid conflicts
     */
    private String createUniqueFileName(String originalFileName) {
        return System.currentTimeMillis() + "-" + originalFileName;
    }

    /**
     * Extract directory path from baseURI config
     */
    private String extractDirectoryFromBaseURI(String baseURI) {
        if (baseURI == null || baseURI.isEmpty()) {
            return "uploads";
        }

        String directory = baseURI;
        if (directory.startsWith("file:")) {
            directory = directory.substring(5);
        }

        if (directory.endsWith("/")) {
            directory = directory.substring(0, directory.length() - 1);
        }

        if (directory.isEmpty()) {
            directory = "uploads";
        }

        log.debug("Extracted upload directory from baseURI '{}': '{}'", baseURI, directory);
        return directory;
    }
}
