# Dự án Website Du lịch Spring Boot

## Cập nhật mới nhất

- **[2025-07-21]**
  - Thêm quan hệ `@OneToMany` giữa entity `Tour` và `TourStart` (field mới: `tour_starts` trong `Tour`).
  - Sửa các truy vấn JPQL trong `TourRepository` để dùng đúng tên trường `tour_starts`.
  - Đảm bảo các API lấy tour theo tháng/mùa hoạt động đúng.
  - Hướng dẫn cập nhật lại file SQL để đảm bảo ràng buộc khóa ngoại giữa `tour` và `tour_start`.

## 1. Tổng quan dự án

Đây là một dự án website du lịch được xây dựng bằng Spring Boot, Maven và Thymeleaf. Dự án đã trải qua một quá trình tái cấu trúc (refactor) toàn diện để chuyển sang một cấu trúc cơ sở dữ liệu mới, tối ưu hơn và chuẩn hóa hơn.

Kiến trúc dự án theo mô hình 3 lớp (Controller - Service - Repository) và cung cấp cả giao diện web cho người dùng và quản trị viên (sử dụng Thymeleaf) lẫn một hệ thống REST API đầy đủ cho các tính năng phía người dùng.

## 2. Công nghệ sử dụng

- **Backend**: Spring Boot 3, Spring Data JPA, Spring Web
- **Frontend**: Thymeleaf, HTML, CSS, JavaScript
- **Cơ sở dữ liệu**: MySQL
- **Build Tool**: Maven
- **Khác**: Lombok

## 3. Hướng dẫn cài đặt Cơ sở dữ liệu

Dự án sử dụng 2 schema (database) riêng biệt:

- `jsb_dulich`: Chứa bảng `user`.
- `jsb_tour`: Chứa tất cả các bảng còn lại liên quan đến tour, booking, tin tức, v.v.

**Các bước cài đặt:**

1.  **Tạo Schema**: Mở công cụ quản lý MySQL của bạn (MySQL Workbench, DBeaver, etc.) và tạo 2 schema rỗng có tên là `jsb_dulich` và `jsb_tour`.
2.  **Tạo bảng**: Thực thi file `script_db_tour_new.sql` để tạo cấu trúc bảng cho cả hai schema.
3.  **Chèn dữ liệu mẫu**: Thực thi file `data.sql` để chèn dữ liệu mẫu vào các bảng. File này đã được thiết kế để chèn dữ liệu vào đúng schema tương ứng.
4.  **Cấu hình kết nối**: Mở file `src/main/resources/application.properties` và cập nhật lại thông tin `spring.datasource.username` và `spring.datasource.password` cho phù hợp với cấu hình MySQL của bạn.

## 4. Các tính năng chính (Đã tái cấu trúc)

- **Quản lý Tour**: Thêm, sửa, xóa, tìm kiếm tour cho admin. Hiển thị danh sách tour trong nước, ngoài nước cho người dùng.
- **Quản lý Booking**: Người dùng đặt tour, xem lịch sử đặt tour. Admin duyệt và quản lý các đơn đặt tour.
- **Quản lý Người dùng**: Đăng ký, đăng nhập, đổi mật khẩu, cập nhật thông tin tài khoản. Phân quyền Admin và Customer.
- **Quản lý Tin tức**: Admin tạo và quản lý các bài viết tin tức.
- **Quản lý Hình ảnh**: Upload và quản lý các hình ảnh chi tiết cho mỗi tour.

## 5. Danh sách REST API cho người dùng

Tất cả các API đều có đường dẫn gốc là `/api`.

### 5.1. API Đánh giá (Review)

- **`POST /api/user/reviews`**: Thêm một đánh giá mới cho tour.
  - **Request Body**: `ReviewDTO` (`{ "tourId": 1, "userId": 2, "rating": 5, "comment": "Tuyệt vời!" }`)
- **`GET /api/user/{userId}/reviews`**: Lấy danh sách tất cả các đánh giá của một người dùng.

### 5.2. API Yêu thích (Wishlist)

- **`POST /api/user/{userId}/wishlist/{tourId}`**: Thêm một tour vào danh sách yêu thích của người dùng.
- **`DELETE /api/user/wishlist/{favoriteId}`**: Xóa một mục khỏi danh sách yêu thích.
- **`GET /api/user/{userId}/wishlist`**: Lấy danh sách các tour yêu thích của người dùng (bao gồm thông tin chi tiết của tour).

### 5.3. API Thông báo (Notification)

- **`GET /api/user/{userId}/notifications`**: Lấy danh sách thông báo của người dùng, sắp xếp theo thời gian mới nhất.
- **`PATCH /api/user/notifications/{notificationId}/read`**: Đánh dấu một thông báo là đã đọc.

### 5.4. API Người dùng & Booking

- **`GET /api/user/{userId}`**: Lấy thông tin chi tiết của một người dùng.
- **`GET /api/user/{userId}/bookings`**: Lấy lịch sử đặt tour của một người dùng.

### 5.5. API Phân nhóm & Thống kê Tour

- **`GET /api/tours/season/{seasonId}`**: Lấy danh sách các tour theo mùa (1: Xuân, 2: Hạ, 3: Thu, 4: Đông).
- **`GET /api/tours/month/{month}`**: Lấy danh sách các tour có ngày khởi hành trong một tháng cụ thể.

## 6. Hướng dẫn chạy dự án

1.  Hoàn thành các bước trong phần "Hướng dẫn cài đặt Cơ sở dữ liệu".
2.  Mở dự án bằng IDE của bạn (IntelliJ, Eclipse, VSCode).
3.  Chạy lệnh `mvn clean install` để tải các dependency.
4.  Chạy dự án bằng cách thực thi file `HoangMinhWebApplication.java` hoặc dùng lệnh `mvn spring-boot:run`.
5.  Truy cập ứng dụng tại `http://localhost:8085`.

## 6. Danh sách API đầy đủ (ví dụ với http://localhost:8085)

### 6.1. API Người dùng (User)

- `GET http://localhost:8085/api/admin/user/getAll?pageIndex=0` : Lấy tất cả user (admin, có thể thêm pageSize, sdt, email, ho_ten)
- `GET http://localhost:8085/api/user/{userId}`: Lấy thông tin người dùng
- `GET http://localhost:8085/api/user/{userId}/bookings`: Lấy lịch sử đặt tour của người dùng
- `POST http://localhost:8085/api/user/reviews`: Thêm đánh giá tour
- `GET http://localhost:8085/api/user/{userId}/reviews`: Lấy danh sách đánh giá của người dùng
- `POST http://localhost:8085/api/user/{userId}/wishlist/{tourId}`: Thêm tour vào wishlist
- `DELETE http://localhost:8085/api/user/wishlist/{favoriteId}`: Xóa khỏi wishlist
- `GET http://localhost:8085/api/user/{userId}/wishlist`: Lấy danh sách wishlist
- `GET http://localhost:8085/api/user/{userId}/notifications`: Lấy danh sách thông báo
- `PATCH http://localhost:8085/api/user/notifications/{notificationId}/read`: Đánh dấu thông báo đã đọc

### 6.2. API Tour (User)

- `GET http://localhost:8085/api/tours/season/{seasonId}`: Lấy tour theo mùa
- `GET http://localhost:8085/api/tours/month/{month}`: Lấy tour theo tháng
- `GET http://localhost:8085/api/tour/getAllTour?pageIndex=0` : Lấy tất cả tour (admin, có thể thêm pageSize, ten_tour, gia_tour_from, gia_tour_to, tour_type_id)

### 6.3. API Admin - Quản lý User

- `GET http://localhost:8085/api/admin/user/getAll`: Lấy danh sách user (admin)
- `GET http://localhost:8085/api/admin/user/{id}`: Lấy thông tin user (admin)
- `PUT http://localhost:8085/api/admin/user/update/{id}`: Cập nhật user (admin)
- `DELETE http://localhost:8085/api/admin/user/delete/{id}`: Xóa user (admin)
- `PUT http://localhost:8085/api/admin/user/update/resetPass/{id}`: Reset mật khẩu user (admin)

### 6.4. API Admin - Quản lý Tour

- `GET http://localhost:8085/api/tour/getAllTour`: Lấy danh sách tour (admin)
- `GET http://localhost:8085/api/tour/{id}`: Lấy thông tin tour (admin)
- `POST http://localhost:8085/api/tour/add`: Thêm tour mới
- `PUT http://localhost:8085/api/tour/update/{id}`: Cập nhật tour
- `DELETE http://localhost:8085/api/tour/delete/{id}`: Xóa tour
- `POST http://localhost:8085/api/tour/add/image`: Thêm ảnh đại diện tour
- `POST http://localhost:8085/api/tour/add-image/{id}`: Thêm ảnh cho tour
- `PUT http://localhost:8085/api/tour/update/image/{id}`: Cập nhật ảnh đại diện tour
- `GET http://localhost:8085/api/tour/getAllImageOfTour/{id}`: Lấy tất cả ảnh của tour
- `DELETE http://localhost:8085/api/tour/TourImage/delete/{id}`: Xóa ảnh tour

### 6.5. API Admin - Quản lý Booking

- `GET http://localhost:8085/api/booking/getAllBooking`: Lấy danh sách booking
- `GET http://localhost:8085/api/booking/{id}`: Lấy thông tin booking
- `GET http://localhost:8085/api/booking/detail/{id}`: Lấy chi tiết booking
- `PUT http://localhost:8085/api/booking/approve/{id}`: Duyệt/cập nhật trạng thái booking
- `DELETE http://localhost:8085/api/booking/delete/{id}`: Xóa booking
- `GET http://localhost:8085/api/booking/getAllBooking?pageIndex=0` : Lấy tất cả booking (admin, có thể thêm pageSize, trang_thai, ten_tour)

### 6.6. API Admin - Quản lý Tin tức

- `GET http://localhost:8085/api/tintuc/getAllPage`: Lấy danh sách tin tức
- `GET http://localhost:8085/api/tintuc/{id}`: Lấy chi tiết tin tức
- `POST http://localhost:8085/api/tintuc/add`: Thêm tin tức
- `PUT http://localhost:8085/api/tintuc/update/{id}`: Cập nhật tin tức
- `GET http://localhost:8085/api/tintuc/getAllPage?pageIndex=0` : Lấy tất cả tin tức (admin, có thể thêm pageSize)

### 6.7. API Auth (Admin)

- `POST http://localhost:8085/api/auth/login`: Đăng nhập admin
- `GET http://localhost:8085/api/auth/logout`: Đăng xuất admin

## 6.8. API lấy tất cả dữ liệu cho mọi nhóm

- **User (admin):**
  - `GET http://localhost:8085/api/admin/user/getAll?pageIndex=0` : Lấy tất cả user (có thể thêm pageSize, sdt, email, ho_ten)
- **Tour (admin):**
  - `GET http://localhost:8085/api/tour/getAllTour?pageIndex=0` : Lấy tất cả tour (có thể thêm pageSize, ten_tour, gia_tour_from, gia_tour_to, tour_type_id)
- **Booking (admin):**
  - `GET http://localhost:8085/api/booking/getAllBooking?pageIndex=0` : Lấy tất cả booking (có thể thêm pageSize, trang_thai, ten_tour)
- **Tin tức (admin):**
  - `GET http://localhost:8085/api/tintuc/getAllPage?pageIndex=0` : Lấy tất cả tin tức (có thể thêm pageSize)
- **Notification:**
  - `GET http://localhost:8085/api/user/{userId}/notifications` : Lấy tất cả notification của 1 user (không có API lấy tất cả notification toàn hệ thống)
- **Favorite:**
  - `GET http://localhost:8085/api/user/{userId}/wishlist` : Lấy tất cả favorite của 1 user (không có API lấy tất cả favorite toàn hệ thống)
- **Review:**
  - `GET http://localhost:8085/api/user/{userId}/reviews` : Lấy tất cả review của 1 user
  - (Không có API lấy tất cả review toàn hệ thống)
- **Tour (user):**
  - `GET http://localhost:8085/api/tours/season/{seasonId}` : Lấy tất cả tour theo mùa
  - `GET http://localhost:8085/api/tours/month/{month}` : Lấy tất cả tour theo tháng
