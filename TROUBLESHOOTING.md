# Hướng dẫn khắc phục lỗi ERR_CONNECTION_REFUSED

## Nguyên nhân có thể:

### 1. **Ứng dụng chưa được khởi động**
- Kiểm tra xem ứng dụng Spring Boot đã chạy chưa
- Port mặc định: 8085 (theo cấu hình trong application.properties)

### 2. **Database chưa được kết nối**
- Đảm bảo MySQL đang chạy
- Kiểm tra thông tin kết nối trong `application.properties`:
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/jsb_tour?createDatabaseIfNotExist=true
  spring.datasource.username=root
  spring.datasource.password=123456
  ```

### 3. **Dependencies chưa được load**
- Chạy lệnh: `mvn clean install`
- Restart IDE nếu cần

## Các bước khắc phục:

### Bước 1: Kiểm tra database
```sql
-- Tạo database nếu chưa có
CREATE DATABASE IF NOT EXISTS jsb_tour;
USE jsb_tour;

-- Chạy file data.sql để tạo dữ liệu mẫu
```

### Bước 2: Build và chạy ứng dụng
```bash
# Clean và build
mvn clean compile

# Chạy ứng dụng
mvn spring-boot:run
```

### Bước 3: Kiểm tra log
- Xem log trong console để tìm lỗi cụ thể
- Các lỗi thường gặp:
  - Database connection failed
  - Port already in use
  - Missing dependencies

### Bước 4: Truy cập ứng dụng
- URL: `http://localhost:8085`
- Nếu port 8085 bị chiếm, thay đổi trong `application.properties`:
  ```properties
  server.port=8086
  ```

## Các tính năng đã thêm:

### 1. **Trang Voucher** (`/voucher`)
- Hiển thị voucher của user
- Hiển thị voucher có sẵn
- Yêu cầu đăng nhập

### 2. **Tích hợp voucher vào booking**
- Form nhập mã voucher
- API kiểm tra voucher: `/api/user/check-voucher`
- JavaScript validation real-time

### 3. **Quản lý voucher**
- Entity `UserVoucher`
- Service `UserVoucherService`
- Repository `UserVoucherRepository`

## Lưu ý:
- Đã tạm thời tắt Spring Security để tránh lỗi
- Cần cấu hình lại bảo mật sau khi ứng dụng chạy ổn định
- Dữ liệu mẫu đã được cập nhật trong `data.sql` 