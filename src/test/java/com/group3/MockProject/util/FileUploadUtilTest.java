package com.group3.MockProject.util;

import com.group3.MockProject.exception.AppException;
import com.group3.MockProject.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FileUploadUtilTest
 * <p>
 * Unit tests for FileUploadUtil class
 * <p>
 * Version 1.0
 * Date: 7/28/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/28/2025      FongFox      Create
 */
@ExtendWith(MockitoExtension.class)
class FileUploadUtilTest {

    private FileUploadUtil fileUploadUtil;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        fileUploadUtil = new FileUploadUtil();
        // Set upload path to temp directory for testing
        ReflectionTestUtils.setField(fileUploadUtil, "uploadBasePath", "file:" + tempDir.toString() + "/");
    }

    // ================= SINGLE FILE UPLOAD TESTS =================

    @Test
    void uploadSingleFile_ValidFile_ShouldReturnUniqueFileName() {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", "application/pdf", "test content".getBytes());

        // When
        String result = fileUploadUtil.uploadSingleFile(file);

        // Then
        assertNotNull(result);
        assertTrue(result.endsWith(".pdf"));
        assertTrue(result.length() > "test.pdf".length()); // UUID prefix makes it longer

        // Verify file exists
        Path uploadedFile = tempDir.resolve(result);
        assertTrue(Files.exists(uploadedFile));
    }

    @Test
    void uploadSingleFile_ValidImageFile_ShouldSuccess() {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file", "image.jpg", "image/jpeg", "image content".getBytes());

        // When
        String result = fileUploadUtil.uploadSingleFile(file);

        // Then
        assertNotNull(result);
        assertTrue(result.endsWith(".jpg"));

        // Verify file content
        Path uploadedFile = tempDir.resolve(result);
        assertTrue(Files.exists(uploadedFile));

        try {
            String content = Files.readString(uploadedFile);
            assertEquals("image content", content);
        } catch (IOException e) {
            fail("Failed to read uploaded file content");
        }
    }

    @Test
    void uploadSingleFile_EmptyFile_ShouldThrowException() {
        // Given
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.pdf", "application/pdf", new byte[0]);

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> fileUploadUtil.uploadSingleFile(emptyFile));

        assertEquals(ErrorCode.FILE_EMPTY, exception.getErrorCode());
    }

    @Test
    void uploadSingleFile_NullFile_ShouldThrowException() {
        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> fileUploadUtil.uploadSingleFile(null));

        assertEquals(ErrorCode.FILE_EMPTY, exception.getErrorCode());
    }

    @Test
    void uploadSingleFile_InvalidExtension_ShouldThrowException() {
        // Given
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file", "malware.exe", "application/octet-stream", "malicious content".getBytes());

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> fileUploadUtil.uploadSingleFile(invalidFile));

        assertEquals(ErrorCode.FILE_INVALID_EXTENSION, exception.getErrorCode());
    }

    @Test
    void uploadSingleFile_FileTooLarge_ShouldThrowException() {
        // Given
        byte[] largeContent = new byte[51 * 1024 * 1024]; // 51MB
        MockMultipartFile largeFile = new MockMultipartFile(
                "file", "large.pdf", "application/pdf", largeContent);

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> fileUploadUtil.uploadSingleFile(largeFile));

        assertEquals(ErrorCode.FILE_TOO_LARGE, exception.getErrorCode());
    }

    @Test
    void uploadSingleFile_NullFileName_ShouldThrowException() {
        // Given
        MockMultipartFile fileWithNullName = new MockMultipartFile(
                "file", null, "application/pdf", "content".getBytes());

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> fileUploadUtil.uploadSingleFile(fileWithNullName));

        assertEquals(ErrorCode.INVALID_FILE_NAME, exception.getErrorCode());
    }

    @Test
    void uploadSingleFile_EmptyFileName_ShouldThrowException() {
        // Given
        MockMultipartFile fileWithEmptyName = new MockMultipartFile(
                "file", "", "application/pdf", "content".getBytes());

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> fileUploadUtil.uploadSingleFile(fileWithEmptyName));

        assertEquals(ErrorCode.INVALID_FILE_NAME, exception.getErrorCode());
    }

    @Test
    void uploadSingleFile_FileWithoutExtension_ShouldThrowException() {
        // Given
        MockMultipartFile fileWithoutExt = new MockMultipartFile(
                "file", "README", "text/plain", "readme content".getBytes());

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> fileUploadUtil.uploadSingleFile(fileWithoutExt));

        assertEquals(ErrorCode.FILE_INVALID_EXTENSION, exception.getErrorCode());
    }

    // ================= MULTIPLE FILES UPLOAD TESTS =================

    @Test
    void uploadMultipleFiles_ValidFiles_ShouldReturnOriginalNames() {
        // Given
        MockMultipartFile file1 = new MockMultipartFile(
                "file1", "document.pdf", "application/pdf", "pdf content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile(
                "file2", "image.jpg", "image/jpeg", "image content".getBytes());
        List<MultipartFile> files = List.of(file1, file2);

        // When
        List<String> result = fileUploadUtil.uploadMultipleFiles(files);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains("document.pdf"));
        assertTrue(result.contains("image.jpg"));

        // Verify files exist (check that some files with PDF and JPG extensions exist)
        try {
            long pdfCount = Files.list(tempDir)
                    .filter(path -> path.toString().endsWith(".pdf"))
                    .count();
            long jpgCount = Files.list(tempDir)
                    .filter(path -> path.toString().endsWith(".jpg"))
                    .count();

            assertEquals(1, pdfCount);
            assertEquals(1, jpgCount);
        } catch (IOException e) {
            fail("Failed to list uploaded files");
        }
    }

    @Test
    void uploadMultipleFiles_EmptyList_ShouldReturnEmptyList() {
        // When
        List<String> result = fileUploadUtil.uploadMultipleFiles(List.of());

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void uploadMultipleFiles_NullList_ShouldReturnEmptyList() {
        // When
        List<String> result = fileUploadUtil.uploadMultipleFiles(null);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void uploadMultipleFiles_MixedValidAndEmptyFiles_ShouldUploadOnlyValid() {
        // Given
        MockMultipartFile validFile = new MockMultipartFile(
                "file1", "document.pdf", "application/pdf", "content".getBytes());
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file2", "empty.txt", "text/plain", new byte[0]);
        List<MultipartFile> files = List.of(validFile, emptyFile);

        // When
        List<String> result = fileUploadUtil.uploadMultipleFiles(files);

        // Then
        assertEquals(1, result.size());
        assertTrue(result.contains("document.pdf"));
    }

    @Test
    void uploadMultipleFiles_OneInvalidFile_ShouldThrowException() {
        // Given
        MockMultipartFile validFile = new MockMultipartFile(
                "file1", "document.pdf", "application/pdf", "content".getBytes());
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file2", "malware.exe", "application/octet-stream", "malicious".getBytes());
        List<MultipartFile> files = List.of(validFile, invalidFile);

        // When & Then
        AppException exception = assertThrows(AppException.class,
                () -> fileUploadUtil.uploadMultipleFiles(files));

        assertEquals(ErrorCode.FILE_INVALID_EXTENSION, exception.getErrorCode());
    }

    // ================= FILE OPERATIONS TESTS =================

    @Test
    void deleteFile_ExistingFile_ShouldReturnTrue() throws IOException {
        // Given
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "test content".getBytes());
        assertTrue(Files.exists(testFile));

        // When
        boolean result = fileUploadUtil.deleteFile("test.txt");

        // Then
        assertTrue(result);
        assertFalse(Files.exists(testFile));
    }

    @Test
    void deleteFile_NonExistingFile_ShouldReturnFalse() {
        // When
        boolean result = fileUploadUtil.deleteFile("nonexistent.txt");

        // Then
        assertFalse(result);
    }

    @Test
    void deleteFile_NullFileName_ShouldReturnFalse() {
        // When
        boolean result = fileUploadUtil.deleteFile(null);

        // Then
        assertFalse(result);
    }

    @Test
    void deleteFile_EmptyFileName_ShouldReturnFalse() {
        // When
        boolean result = fileUploadUtil.deleteFile("");

        // Then
        assertFalse(result);
    }

    @Test
    void fileExists_ExistingFile_ShouldReturnTrue() throws IOException {
        // Given
        Path testFile = tempDir.resolve("exists.txt");
        Files.write(testFile, "content".getBytes());

        // When
        boolean result = fileUploadUtil.fileExists("exists.txt");

        // Then
        assertTrue(result);
    }

    @Test
    void fileExists_NonExistingFile_ShouldReturnFalse() {
        // When
        boolean result = fileUploadUtil.fileExists("nonexistent.txt");

        // Then
        assertFalse(result);
    }

    @Test
    void fileExists_NullFileName_ShouldReturnFalse() {
        // When
        boolean result = fileUploadUtil.fileExists(null);

        // Then
        assertFalse(result);
    }

    @Test
    void fileExists_EmptyFileName_ShouldReturnFalse() {
        // When
        boolean result = fileUploadUtil.fileExists("");

        // Then
        assertFalse(result);
    }

    @Test
    void getFilePath_ValidFileName_ShouldReturnCorrectPath() {
        // When
        Path result = fileUploadUtil.getFilePath("test.txt");

        // Then
        assertNotNull(result);
        assertTrue(result.toString().endsWith("test.txt"));
        assertEquals(tempDir.resolve("test.txt"), result);
    }

    // ================= EDGE CASES AND INTEGRATION TESTS =================

    @Test
    void uploadSingleFile_VariousValidExtensions_ShouldAllSucceed() {
        // Given
        String[] validExtensions = {"jpg", "jpeg", "png", "gif", "pdf", "doc", "docx", "txt", "mp4", "avi", "zip"};

        for (String ext : validExtensions) {
            // Given
            MockMultipartFile file = new MockMultipartFile(
                    "file", "test." + ext, "application/octet-stream", "content".getBytes());

            // When & Then
            assertDoesNotThrow(() -> {
                String result = fileUploadUtil.uploadSingleFile(file);
                assertTrue(result.endsWith("." + ext), "Failed for extension: " + ext);
            }, "Should succeed for extension: " + ext);
        }
    }

    @Test
    void uploadSingleFile_CaseInsensitiveExtensions_ShouldWork() {
        // Given
        MockMultipartFile upperCaseFile = new MockMultipartFile(
                "file", "test.PDF", "application/pdf", "content".getBytes());
        MockMultipartFile mixedCaseFile = new MockMultipartFile(
                "file", "test.JpG", "image/jpeg", "content".getBytes());

        // When & Then
        assertDoesNotThrow(() -> fileUploadUtil.uploadSingleFile(upperCaseFile));
        assertDoesNotThrow(() -> fileUploadUtil.uploadSingleFile(mixedCaseFile));
    }

    @Test
    void uploadSingleFile_MaxAllowedSize_ShouldSucceed() {
        // Given - exactly 50MB (maximum allowed)
        byte[] maxSizeContent = new byte[50 * 1024 * 1024];
        MockMultipartFile maxSizeFile = new MockMultipartFile(
                "file", "large.pdf", "application/pdf", maxSizeContent);

        // When & Then
        assertDoesNotThrow(() -> fileUploadUtil.uploadSingleFile(maxSizeFile));
    }

    @Test
    void uploadMultipleFiles_LargeNumberOfFiles_ShouldHandleCorrectly() {
        // Given - 10 files
        List<MultipartFile> manyFiles = new java.util.ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MockMultipartFile file = new MockMultipartFile(
                    "file" + i, "test" + i + ".txt", "text/plain", ("content" + i).getBytes());
            manyFiles.add(file);
        }

        // When
        List<String> result = fileUploadUtil.uploadMultipleFiles(manyFiles);

        // Then
        assertEquals(10, result.size());
        for (int i = 0; i < 10; i++) {
            assertTrue(result.contains("test" + i + ".txt"));
        }
    }

    @Test
    void uploadAndDelete_FileLifecycle_ShouldWorkCorrectly() {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file", "lifecycle.txt", "text/plain", "test content".getBytes());

        // When - Upload
        String fileName = fileUploadUtil.uploadSingleFile(file);

        // Then - File should exist
        assertTrue(fileUploadUtil.fileExists(fileName));

        // When - Delete
        boolean deleted = fileUploadUtil.deleteFile(fileName);

        // Then - File should be gone
        assertTrue(deleted);
        assertFalse(fileUploadUtil.fileExists(fileName));
    }

    // ================= ERROR HANDLING TESTS =================

    @Test
    void uploadSingleFile_DirectoryCreationError_ShouldHandleGracefully() {
        // Given - Set invalid upload path that cannot be created
        ReflectionTestUtils.setField(fileUploadUtil, "uploadBasePath", "file:///root/forbidden/path/");

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "content".getBytes());

        // When & Then - Should handle directory creation error
        AppException exception = assertThrows(AppException.class,
                () -> fileUploadUtil.uploadSingleFile(file));

        assertEquals(ErrorCode.FILE_UPLOAD_FAILED, exception.getErrorCode());
    }
}