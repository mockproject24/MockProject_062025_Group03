package com.group3.MockProject.service.impl;

import com.group3.MockProject.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileSystemStorageService implements FileStorageService {

    // Đường dẫn vật lý nơi các tệp sẽ được lưu trữ.
    // Lấy giá trị từ cấu hình application.yml, mặc định là "./uploads".
    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    @Override
    public String store(MultipartFile file) {
        // Kiểm tra nếu tệp rỗng, ném ngoại lệ RuntimeException
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }

        try {
            // Chuẩn hóa đường dẫn upload và tạo thư mục nếu nó chưa tồn tại
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            // Tạo tên file duy nhất để tránh xung đột tên tệp
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
            Path targetLocation = uploadPath.resolve(fileName);

            // Sao chép tệp từ luồng đầu vào vào vị trí đích
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName; // Trả về tên file duy nhất đã được lưu
        } catch (IOException ex) {
            // Ném RuntimeException nếu có lỗi trong quá trình lưu tệp
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename() + ". " + ex.getMessage(), ex);
        }
    }
}