# Mock Project 062025 Group 03

## Cấu hình môi trường

Dự án này sử dụng file `application.yml` để cấu hình. Để thiết lập môi trường local của bạn:

1. Sao chép file mẫu để tạo file cấu hình:
   ```
   cp src/main/resources/application.yml.template src/main/resources/application.yml
   ```

2. Chỉnh sửa file `application.yml` với cài đặt cá nhân của bạn:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/your_db_name
       username: your_username
       password: your_password
   
   jwt:
     secret: your_secure_jwt_secret_key
   ```

Lưu ý: File `application.yml` đã được thêm vào `.gitignore` để tránh xung đột giữa các developer và để bảo mật thông tin nhạy cảm. 