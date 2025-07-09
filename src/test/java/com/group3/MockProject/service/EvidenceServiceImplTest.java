package com.group3.MockProject.service;

import com.group3.MockProject.constant.EvidenceType; // Giả định enum này tồn tại
import com.group3.MockProject.dto.request.CreateEvidenceRequest;
import com.group3.MockProject.dto.response.EvidenceResponse;
import com.group3.MockProject.entity.Case; // Giả định Case entity tồn tại
import com.group3.MockProject.entity.Evidence;
import com.group3.MockProject.exception.ResourceNotFoundException;
import com.group3.MockProject.exception.StorageException;
import com.group3.MockProject.mapper.EvidenceMapper;
import com.group3.MockProject.repository.CaseRepository;
import com.group3.MockProject.repository.EvidenceRepository;
import com.group3.MockProject.service.impl.EvidenceServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvidenceServiceImplTest {

    @Mock
    private EvidenceRepository evidenceRepository;

    @Mock
    private CaseRepository caseRepository;

    @Mock
    private EvidenceMapper evidenceMapper;

    @InjectMocks
    private EvidenceServiceImpl evidenceService;

    private Case testCase;
    private CreateEvidenceRequest testRequest;
    private MockMultipartFile testFile;
    private Evidence testEvidence; // Sẽ được khởi tạo trong các test method cụ thể

    private Path tempUploadDir; // Đường dẫn thư mục tạm thời để lưu file thật trong quá trình test

    @BeforeEach
    void setUp() throws Exception {
        testCase = new Case();
        testCase.setCaseId("case123");

        testRequest = CreateEvidenceRequest.builder()
                .description("Test Description")
                .currentLocation("Test Location")
                .collectedAt(LocalDateTime.of(2025, 7, 9, 10, 0))
                .evidenceType(EvidenceType.PHYSICAL_EVIDENCE)
                .build();

        testFile = new MockMultipartFile(
                "file",
                "test-evidence.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "some-image-content".getBytes()
        );

        // --- Cấu hình @Value fields cho EvidenceServiceImpl để test ---
        tempUploadDir = Files.createTempDirectory("test_uploads_");

        // Dùng Reflection để inject giá trị cho các trường @Value trong service
        // Đặt uploadDir của service trỏ đến thư mục tạm thời này
        Field uploadDirField = EvidenceServiceImpl.class.getDeclaredField("uploadDir");
        uploadDirField.setAccessible(true);
        uploadDirField.set(evidenceService, tempUploadDir.toString());

        // Đặt baseURI của service
        Field baseUriField = EvidenceServiceImpl.class.getDeclaredField("baseURI");
        baseUriField.setAccessible(true);
        baseUriField.set(evidenceService, "/uploads/"); // Sử dụng base URI đơn giản cho test
    }

    @AfterEach
    void tearDown() throws IOException {
        // Dọn dẹp thư mục tạm thời sau mỗi test
        if (tempUploadDir != null && Files.exists(tempUploadDir)) {
            Files.walk(tempUploadDir)
                    .sorted(java.util.Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
            Files.deleteIfExists(tempUploadDir);
        }
    }

    @Test
    void testCreateEvidence_Success() throws IOException {
        when(caseRepository.findById("case123")).thenReturn(Optional.of(testCase));

        // Mock mapper.toEvidenceEntity để nó trả về một Evidence entity
        when(evidenceMapper.toEvidenceEntity(eq(testRequest), anyString(), eq(testCase)))
                .thenAnswer(invocation -> {
                    String fileUrl = invocation.getArgument(1); // Lấy fileUrl từ tham số
                    return Evidence.builder()
                            .evidenceId("evi001") // Id giả định cho evidence mới
                            .description(testRequest.getDescription())
                            .currentLocation(testRequest.getCurrentLocation())
                            .collectedAt(testRequest.getCollectedAt())
                            .evidenceType(testRequest.getEvidenceType())
                            .attachFile(fileUrl) // Gán fileUrl được tạo ra
                            .caseEntity(testCase)
                            .build();
                });

        // Mock evidenceRepository.save để trả về evidence đã được lưu
        // Lưu ý: testEvidence ở đây sẽ là đối tượng đã được gán fileUrl từ mapper
        when(evidenceRepository.save(any(Evidence.class))).thenAnswer(invocation -> {
            Evidence savedEvidence = invocation.getArgument(0);
            testEvidence = savedEvidence; // Gán vào testEvidence để dùng cho mock tiếp theo
            return savedEvidence;
        });

        // Mock mapper.toEvidenceResponse
        // Sử dụng thenAnswer để xây dựng EvidenceResponse dựa trên các đối số thực sự được truyền vào
        when(evidenceMapper.toEvidenceResponse(any(Evidence.class), anyString())) // Thay đổi từ eq(testEvidence) thành any(Evidence.class)
                .thenAnswer(invocation -> {
                    Evidence evidenceArg = invocation.getArgument(0); // Lấy Evidence từ tham số
                    String fileUrlArg = invocation.getArgument(1); // Lấy fileUrl từ tham số
                    return EvidenceResponse.builder()
                            .evidenceId(evidenceArg.getEvidenceId())
                            .description(evidenceArg.getDescription())
                            .currentLocation(evidenceArg.getCurrentLocation())
                            .collectedAt(evidenceArg.getCollectedAt())
                            .evidenceType(evidenceArg.getEvidenceType())
                            .attachFile(fileUrlArg)
                            .caseId(evidenceArg.getCaseEntity().getCaseId())
                            .build();
                });


        // Thực thi phương thức cần kiểm thử
        EvidenceResponse response = evidenceService.createEvidence("case123", testRequest, testFile);

        // Xác minh kết quả
        assertNotNull(response);
        assertEquals("case123", response.getCaseId());
        assertEquals("Test Description", response.getDescription());
        assertTrue(response.getAttachFile().startsWith("/uploads/")); // Kiểm tra tiền tố URL
        assertTrue(response.getAttachFile().contains("_test-evidence.jpg")); // Kiểm tra tên file gốc

        // Xác minh rằng file đã được tạo vật lý trong thư mục tạm thời
        String storedFileName = response.getAttachFile().replace("/uploads/", "");
        Path storedFilePath = tempUploadDir.resolve(storedFileName);
        assertTrue(Files.exists(storedFilePath), "File should exist in temporary upload directory");
        assertTrue(Files.size(storedFilePath) > 0, "Stored file should not be empty");

        // Xác minh các tương tác với mock
        verify(caseRepository, times(1)).findById("case123");
        verify(evidenceMapper, times(1)).toEvidenceEntity(eq(testRequest), anyString(), eq(testCase));
        verify(evidenceRepository, times(1)).save(any(Evidence.class));
        verify(evidenceMapper, times(1)).toEvidenceResponse(any(Evidence.class), anyString());
    }

    @Test
    void testGetEvidence_Success() {
        // Khởi tạo testEvidence cho riêng test này
        testEvidence = Evidence.builder()
                .evidenceId("evi001")
                .description("Existing Description")
                .currentLocation("Existing Location")
                .collectedAt(LocalDateTime.now())
                .evidenceType(EvidenceType.DIGITAL_EVIDENCE)
                .attachFile("/uploads/existing_file.pdf") // Giả định URL đầy đủ
                .caseEntity(testCase)
                .build();

        // Khởi tạo testResponse cho riêng test này
        EvidenceResponse expectedResponse = EvidenceResponse.builder()
                .evidenceId("evi001")
                .description("Existing Description")
                .currentLocation("Existing Location")
                .collectedAt(LocalDateTime.now())
                .evidenceType(EvidenceType.DIGITAL_EVIDENCE)
                .attachFile("/uploads/existing_file.pdf")
                .caseId("case123")
                .build();

        when(caseRepository.findById("case123")).thenReturn(Optional.of(testCase));
        when(evidenceRepository.findByCaseEntity_CaseIdAndEvidenceId("case123", "evi001"))
                .thenReturn(Optional.of(testEvidence));
        when(evidenceMapper.toEvidenceResponse(eq(testEvidence), eq(testEvidence.getAttachFile())))
                .thenReturn(expectedResponse);

        EvidenceResponse actualResponse = evidenceService.getEvidence("case123", "evi001");

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getEvidenceId(), actualResponse.getEvidenceId()); // So sánh từng trường
        assertEquals(expectedResponse.getDescription(), actualResponse.getDescription());
        assertEquals(expectedResponse.getAttachFile(), actualResponse.getAttachFile());

        verify(caseRepository, times(1)).findById("case123");
        verify(evidenceRepository, times(1)).findByCaseEntity_CaseIdAndEvidenceId("case123", "evi001");
        verify(evidenceMapper, times(1)).toEvidenceResponse(any(Evidence.class), anyString());
    }

    @Test
    void testGetEvidence_EvidenceNotFound() {
        when(caseRepository.findById("case123")).thenReturn(Optional.of(testCase));
        when(evidenceRepository.findByCaseEntity_CaseIdAndEvidenceId("case123", "nonExistentEvidence"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                evidenceService.getEvidence("case123", "nonExistentEvidence")
        );

        assertTrue(exception.getMessage().contains("Evidence not found with ID: nonExistentEvidence for case ID: case123"));
        verify(caseRepository, times(1)).findById("case123");
        verify(evidenceRepository, times(1)).findByCaseEntity_CaseIdAndEvidenceId("case123", "nonExistentEvidence");
        verify(evidenceMapper, never()).toEvidenceResponse(any(), anyString());
    }

    @Test
    void testCreateEvidence_CaseNotFound() {
        when(caseRepository.findById("nonExistentCase")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                evidenceService.createEvidence("nonExistentCase", testRequest, testFile)
        );

        assertTrue(exception.getMessage().contains("Case not found with ID: nonExistentCase"));
        verify(caseRepository, times(1)).findById("nonExistentCase");
        verify(evidenceMapper, never()).toEvidenceEntity(any(), anyString(), any());
        verify(evidenceRepository, never()).save(any());
    }

    @Test
    void testCreateEvidence_EmptyFile() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]); // File rỗng

        // Case vẫn được tìm thấy trước khi kiểm tra file
        when(caseRepository.findById("case123")).thenReturn(Optional.of(testCase)); // Vẫn mock findById vì nó được gọi trước exception

        StorageException exception = assertThrows(StorageException.class, () ->
                evidenceService.createEvidence("case123", testRequest, emptyFile)
        );

        assertTrue(exception.getMessage().contains("Attach file cannot be null or empty."));
        verify(caseRepository, times(1)).findById("case123"); // Verify nó được gọi vì nó nằm sau kiểm tra file rỗng/null
        verify(evidenceMapper, never()).toEvidenceEntity(any(), anyString(), any());
        verify(evidenceRepository, never()).save(any());
    }

    @Test
    void testCreateEvidence_NullFile() {
        // Case vẫn được tìm thấy trước khi kiểm tra file
        when(caseRepository.findById("case123")).thenReturn(Optional.of(testCase)); // Vẫn mock findById vì nó được gọi trước exception

        StorageException exception = assertThrows(StorageException.class, () ->
                evidenceService.createEvidence("case123", testRequest, null) // Truyền null file
        );

        assertTrue(exception.getMessage().contains("Attach file cannot be null or empty."));
        verify(caseRepository, times(1)).findById("case123"); // Verify nó được gọi vì nó nằm sau kiểm tra file rỗng/null
        verify(evidenceMapper, never()).toEvidenceEntity(any(), anyString(), any());
        verify(evidenceRepository, never()).save(any());
    }


    @Test
    void testGetEvidence_CaseNotFound() {
        when(caseRepository.findById("nonExistentCase")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            evidenceService.getEvidence("nonExistentCase", "someEvidenceId");
        });
        verify(caseRepository, times(1)).findById("nonExistentCase");
        // Xác minh rằng evidenceRepository KHÔNG được tương tác vì case không tìm thấy
        verify(evidenceRepository, never()).findByCaseEntity_CaseIdAndEvidenceId(anyString(), anyString());
        verify(evidenceMapper, never()).toEvidenceResponse(any(), anyString());
    }

    @Test
    void testCreateEvidence_InvalidFileExtension() {
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file",
                "malicious.exe", // Phần mở rộng không hợp lệ
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "malicious-content".getBytes()
        );

        // Case vẫn được tìm thấy trước khi kiểm tra file extension
        when(caseRepository.findById("case123")).thenReturn(Optional.of(testCase));

        StorageException exception = assertThrows(StorageException.class, () ->
                evidenceService.createEvidence("case123", testRequest, invalidFile)
        );

        assertTrue(exception.getMessage().contains("Invalid file extension: exe. Allowed extensions: [jpeg, png, jpg, gif, mp4, pdf, doc, docx, ppt, pptx]"));
        verify(caseRepository, times(1)).findById("case123"); // Verify nó được gọi
        verify(evidenceMapper, never()).toEvidenceEntity(any(), anyString(), any());
        verify(evidenceRepository, never()).save(any());
    }
}