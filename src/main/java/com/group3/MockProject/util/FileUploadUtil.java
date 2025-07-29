package com.group3.MockProject.util;

import com.group3.MockProject.exception.AppException;
import com.group3.MockProject.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * FileUploadUtil
 *
 * Utility class for handling file upload operations.
 *
 * Version 1.0
 * Date: 7/28/2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/28/2025      FongFox      Create
 */
@Component
@Slf4j
public class FileUploadUtil {
    @Value("${spring.upload-file.base-uri}")
    private String uploadBasePath;

    // Constants for file upload
    private static final List<String> ALLOWED_FILE_EXTENSIONS = List.of(
            "jpg", "jpeg", "png", "gif", "bmp", "webp", // Images
            "mp3", "wav", "aac", "flac", "mp4", "avi", "mov", "mkv", "webm", // Audio/Video
            "pdf", "doc", "docx", "txt", "rtf", // Documents
            "zip", "rar", "7z" // Archives
    );

    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

    /**
     * Uploads multiple files and returns their original filenames
     *
     * @param files List of files to upload
     * @return List of original filenames of successfully uploaded files
     * @throws AppException if file upload fails
     */
    public List<String> uploadMultipleFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            log.debug("No files provided for upload");
            return List.of();
        }

        log.info("Starting upload of {} files", files.size());

        // Create upload directory if not exists
        createUploadDirectoryIfNotExists();

        List<String> originalFileNames = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                // Upload file and get unique filename
                uploadSingleFile(file);
                // Keep original filename for response
                originalFileNames.add(file.getOriginalFilename());
            }
        }

        return originalFileNames;
    }

    /**
     * Uploads a single file and returns the unique filename
     *
     * @param file The file to upload
     * @return The unique filename that was saved
     * @throws AppException if file upload fails
     */
    public String uploadSingleFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_EMPTY);
        }

        try {
            // Validate file before upload
            validateFile(file);

            // Generate unique filename: UUID + original extension
            String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());

            // Save file to disk
            Path uploadDir = getUploadDirectory();
            Path filePath = uploadDir.resolve(uniqueFileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            log.debug("File uploaded successfully: {} -> {}", file.getOriginalFilename(), uniqueFileName);
            return uniqueFileName;

        } catch (IOException e) {
            log.error("File upload failed for file: {}", file.getOriginalFilename(), e);
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "Failed to upload file: " + e.getMessage());
        }
    }

    /**
     * Validates file before upload
     *
     * @param file The file to validate
     * @throws AppException if validation fails
     */
    private void validateFile(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();

        // Check filename
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_FILE_NAME);
        }

        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            log.error("File too large: {} bytes (max: {} bytes)", file.getSize(), MAX_FILE_SIZE);
            throw new AppException(ErrorCode.FILE_TOO_LARGE);
        }

        // Check file extension
        String extension = getFileExtension(originalFileName);
        if (!isValidFileExtension(extension)) {
            log.error("Invalid file extension: {} for file: {}", extension, originalFileName);
            throw new AppException(ErrorCode.FILE_INVALID_EXTENSION);
        }
    }

    /**
     * Generates a unique filename using UUID and original extension
     *
     * @param originalFileName The original filename
     * @return Unique filename with UUID prefix
     */
    private String generateUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        return UUID.randomUUID().toString() + (extension.isEmpty() ? "" : "." + extension);
    }

    /**
     * Gets file extension from filename
     *
     * @param filename The filename
     * @return File extension without dot, or empty string if no extension
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }

        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        return "";
    }

    /**
     * Checks if file extension is valid
     *
     * @param extension The file extension to check
     * @return true if extension is allowed, false otherwise
     */
    private boolean isValidFileExtension(String extension) {
        return ALLOWED_FILE_EXTENSIONS.contains(extension.toLowerCase());
    }

    /**
     * Creates upload directory if it doesn't exist
     *
     * @throws AppException if directory creation fails
     */
    private void createUploadDirectoryIfNotExists() {
        try {
            Path uploadDir = getUploadDirectory();
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
                log.info("Created upload directory: {}", uploadDir.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("Failed to create upload directory", e);
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "Failed to create upload directory");
        }
    }

    /**
     * Gets the upload directory path
     *
     * @return Path to upload directory
     */
    private Path getUploadDirectory() {
        String cleanPath = uploadBasePath.replace("file:", "");
        return Paths.get(cleanPath);
    }

    /**
     * Deletes a file from upload directory
     *
     * @param filename The filename to delete
     * @return true if file was deleted successfully, false otherwise
     */
    public boolean deleteFile(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return false;
        }

        try {
            Path uploadDir = getUploadDirectory();
            Path filePath = uploadDir.resolve(filename);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.debug("File deleted successfully: {}", filename);
                return true;
            } else {
                log.warn("File not found for deletion: {}", filename);
                return false;
            }
        } catch (IOException e) {
            log.error("Failed to delete file: {}", filename, e);
            return false;
        }
    }

    /**
     * Checks if a file exists in upload directory
     *
     * @param filename The filename to check
     * @return true if file exists, false otherwise
     */
    public boolean fileExists(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return false;
        }

        Path uploadDir = getUploadDirectory();
        Path filePath = uploadDir.resolve(filename);
        return Files.exists(filePath);
    }

    /**
     * Gets the full path to a file in upload directory
     *
     * @param filename The filename
     * @return Full path to the file
     */
    public Path getFilePath(String filename) {
        Path uploadDir = getUploadDirectory();
        return uploadDir.resolve(filename);
    }
}
