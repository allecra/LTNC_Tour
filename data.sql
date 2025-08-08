-- Sử dụng schema jsb_tour cho các bảng chính của ứng dụng
USE `jsb_tour`;

-- Dữ liệu mẫu cho bảng 'role'
INSERT INTO `role` (`id`, `ten_role`, `mo_ta`) VALUES
(1, 'ADMIN', 'Quản trị viên hệ thống, có toàn quyền truy cập'),
(2, 'CUSTOMER', 'Khách hàng, người dùng cuối sử dụng dịch vụ'),
(3, 'STAFF', 'Nhân viên, có quyền truy cập hạn chế để quản lý tour và booking');

-- Dữ liệu mẫu cho bảng 'user'
-- Lưu ý: Để id tự động tăng (AUTO_INCREMENT), không gán id thủ công
-- Mật khẩu cho tất cả người dùng mẫu là: 123456
INSERT INTO `user` (`username`, `pass`, `ho_ten`, `email`, `sdt`, `gioi_tinh`, `dia_chi`, `role_id`) VALUES
('admin', '$2a$10$N9qo8uLOickgx2Z5P7a3.OjgIV/vC3T2vLgB1i.5lT.sMxCEsB2yO', 'Quản Trị Viên', 'admin@tour.com', '0123456789', 'Khác', '123 Admin Street', 1),
('customer1', '$2a$10$N9qo8uLOickgx2Z5P7a3.OjgIV/vC3T2vLgB1i.5lT.sMxCEsB2yO', 'Nguyễn Văn A', 'customer1@example.com', '0987654321', 'Nam', '456 Customer Avenue', 2),
('staff1', '$2a$10$N9qo8uLOickgx2Z5P7a3.OjgIV/vC3T2vLgB1i.5lT.sMxCEsB2yO', 'Trần Thị B', 'staff1@tour.com', '0123123123', 'Nữ', '789 Staff Road', 3);


-- Dữ liệu mẫu cho bảng 'tour_type'
INSERT INTO `tour_type` (`id`, `ten_loai`, `mo_ta`) VALUES
(1, 'Trong nước', 'Các tour du lịch diễn ra trong lãnh thổ Việt Nam'),
(2, 'Ngoài nước', 'Các tour du lịch đến các quốc gia khác');

-- Dữ liệu mẫu cho bảng 'destination'
INSERT INTO `destination` (`id`, `name`, `country`, `description`, `is_domestic`) VALUES
(1, 'Vịnh Hạ Long', 'Việt Nam', 'Di sản thiên nhiên thế giới được UNESCO công nhận.', 1),
(2, 'Phố cổ Hội An', 'Việt Nam', 'Thành phố cổ kính với những ngôi nhà đèn lồng đặc trưng.', 1),
(3, 'Paris', 'Pháp', 'Kinh đô ánh sáng với tháp Eiffel và bảo tàng Louvre.', 0),
(4, 'Tokyo', 'Nhật Bản', 'Thành phố hiện đại và năng động với văn hóa độc đáo.', 0);

-- Dữ liệu mẫu cho bảng 'season'
INSERT INTO `season` (`id`, `ten_mua`, `thang_bat_dau`, `thang_ket_thuc`) VALUES
(1, 'Xuan', 3, 5),
(2, 'Ha', 6, 8),
(3, 'Thu', 9, 11),
(4, 'Dong', 12, 2);

-- Dữ liệu mẫu cho bảng 'tour'
INSERT INTO `tour` (`id`, `ten_tour`, `destination_id`, `diem_khoi_hanh`, `gioi_thieu_tour`, `noi_dung_tour`, `anh_dai_dien`, `gia_tour`, `sale_price`, `so_ngay`, `tour_type_id`, `trang_thai`) VALUES
(1, 'Khám phá Vịnh Hạ Long 3 ngày 2 đêm', 1, 'Hà Nội', 'Tour du thuyền cao cấp khám phá vẻ đẹp kỳ vĩ của Vịnh Hạ Long.', 'Chi tiết lịch trình tham quan hang Sửng Sốt, chèo kayak, tắm biển Titop...', 'halong.jpg', 5000000.00, 4500000.00, 3, 1, 'dang_mo_ban'),
(2, 'Hành trình di sản miền Trung: Đà Nẵng - Hội An - Huế', 2, 'Đà Nẵng', 'Khám phá những di sản văn hóa đặc sắc của miền Trung Việt Nam.', 'Tham quan Cố đô Huế, Phố cổ Hội An, Bà Nà Hills...', 'hoian.jpg', 7000000.00, NULL, 5, 1, 'dang_mo_ban'),
(3, 'Lãng mạn Paris 5 ngày 4 đêm', 3, 'TP. Hồ Chí Minh', 'Trải nghiệm không khí lãng mạn tại kinh đô ánh sáng Paris.', 'Check-in tháp Eiffel, du thuyền sông Seine, tham quan bảo tàng Louvre...', 'paris.jpg', 30000000.00, 28500000.00, 5, 2, 'dang_mo_ban'),
(4, 'Khám phá Tokyo và núi Phú Sĩ', 4, 'Hà Nội', 'Tour trải nghiệm văn hóa Nhật Bản, ngắm hoa anh đào và chinh phục núi Phú Sĩ.', 'Tham quan đền Asakusa, khu phố Shibuya, làng cổ Oshino Hakkai...', 'tokyo.jpg', 25000000.00, 24000000.00, 5, 2, 'da_het_cho'),
(5, 'Phú Quốc - Hòn ngọc Viễn Đông', 1, 'TP. Hồ Chí Minh', 'Nghỉ dưỡng tại resort 5 sao, lặn ngắm san hô và khám phá các hòn đảo hoang sơ.', 'Tham quan nhà tù Phú Quốc, Dinh Cậu, Bãi Sao...', 'phuquoc.jpg', 6500000.00, NULL, 4, 1, 'dang_mo_ban'),
(6, 'Sapa - Nóc nhà Đông Dương', 1, 'Hà Nội', 'Chinh phục đỉnh Fansipan và khám phá văn hóa các dân tộc thiểu số.', 'Đi cáp treo Fansipan, thăm bản Cát Cát, chinh phục đèo Ô Quy Hồ...', 'sapa.jpg', 4800000.00, 4500000.00, 3, 1, 'dang_mo_ban');


-- Dữ liệu mẫu cho bảng 'tour_start'
INSERT INTO `tour_start` (`id`, `tour_id`, `ngay_khoi_hanh`, `ngay_ket_thuc`, `so_cho_con_lai`, `season_id`, `gia_rieng`, `month`, `year`) VALUES
(1, 1, '2024-08-15', '2024-08-17', 20, 2, 4800000.00, 8, 2024),
(2, 1, '2024-09-05', '2024-09-07', 15, 3, 4700000.00, 9, 2024),
(3, 2, '2024-08-20', '2024-08-24', 25, 2, 7000000.00, 8, 2024),
(4, 3, '2024-10-10', '2024-10-14', 10, 3, 28500000.00, 10, 2024),
(5, 4, '2024-04-10', '2024-04-14', 0, 1, 25000000.00, 4, 2024),
(6, 5, '2024-09-15', '2024-09-18', 30, 3, 6500000.00, 9, 2024),
(7, 6, '2024-11-20', '2024-11-22', 18, 3, 4500000.00, 11, 2024),
(8, 6, '2024-12-25', '2024-12-27', 22, 4, 4500000.00, 12, 2024),
-- Bổ sung thêm dữ liệu mới
(9, 2, '2024-06-10', '2024-06-14', 20, 2, 7000000.00, 6, 2024),
(10, 3, '2024-07-15', '2024-07-19', 12, 2, 29000000.00, 7, 2024),
(11, 4, '2024-03-20', '2024-03-24', 8, 1, 25500000.00, 3, 2024),
(12, 5, '2024-05-05', '2024-05-08', 25, 1, 6500000.00, 5, 2024),
(13, 1, '2024-10-01', '2024-10-03', 10, 3, 4600000.00, 10, 2024),
(14, 6, '2024-08-10', '2024-08-12', 15, 2, 4550000.00, 8, 2024);

-- Dữ liệu mẫu cho bảng 'image'
INSERT INTO `image` (`id`, `url`, `object_type`, `object_id`, `caption`, `tour_id`, `user_id`) VALUES
(1, 'halong_1.jpg', 'tour', 1, 'Du thuyền trên vịnh', 1, 1),
(2, 'halong_2.jpg', 'tour', 1, 'Hang Sửng Sốt', 1, 1),
(3, 'hoian_1.jpg', 'tour', 2, 'Phố cổ về đêm', 2, 1),
(4, 'paris_1.jpg', 'tour', 3, 'Tháp Eiffel lung linh', 3, 1),
(5, 'paris_2.jpg', 'tour', 3, 'Bảo tàng Louvre', 3, 1),
(6, 'tokyo_1.jpg', 'tour', 4, 'Hoa anh đào nở rộ', 4, 1),
(7, 'sapa_1.jpg', 'tour', 6, 'Ruộng bậc thang mùa lúa chín', 6, 1);

-- Dữ liệu mẫu cho bảng 'tin_tuc'
INSERT INTO `tin_tuc` (`id`, `tieu_de`, `tom_tat`, `noi_dung`, `ngay_dang`, `trang_thai`) VALUES
(1, 'Mở lại đường bay quốc tế, du lịch Việt Nam khởi sắc', 'Sau thời gian dài bị ảnh hưởng bởi dịch bệnh, ngành du lịch đang có những dấu hiệu phục hồi mạnh mẽ.', 'Nội dung chi tiết...', NOW(), 'da_dang'),
(2, 'Top 5 điểm đến không thể bỏ lỡ mùa hè 2024', 'Tổng hợp những bãi biển, hòn đảo và khu nghỉ dưỡng hot nhất cho kỳ nghỉ hè của bạn.', 'Nội dung chi tiết...', NOW(), 'da_dang'),
(3, 'Kinh nghiệm săn vé máy bay giá rẻ cho dân du lịch', 'Bí quyết và mẹo vặt giúp bạn có một chuyến đi tiết kiệm chi phí.', 'Nội dung chi tiết...', NOW(), 'da_dang');

-- Dữ liệu mẫu cho bảng 'booking'
INSERT INTO `booking` (`id`, `tour_start_id`, `user_id`, `so_luong_nguoi`, `tong_tien`, `payment_method`, `payment_status`, `trang_thai`, `ghi_chu`, `booking_at`) VALUES
(1, 1, 2, 2, 9000000.00, 'Chuyển khoản', 'da_thanh_toan', 'da_xac_nhan', 'Yêu cầu phòng có view đẹp.', NOW()),
(2, 3, 2, 4, 28000000.00, 'Thanh toán khi nhận tour', 'chua_thanh_toan', 'cho_xac_nhan', 'Gia đình có trẻ nhỏ', NOW()),
(3, 4, 2, 2, 57000000.00, 'Chuyển khoản', 'da_thanh_toan', 'da_xac_nhan', NULL, NOW());

-- Dữ liệu mẫu cho bảng 'review'
INSERT INTO `review` (`id`, `tour_id`, `user_id`, `rating`, `comment`, `trang_thai`, `created_at`) VALUES
(1, 1, 2, 5, 'Tour rất tuyệt vời, hướng dẫn viên nhiệt tình, cảnh đẹp. Sẽ quay lại!', 'Đã duyệt', NOW()),
(2, 2, 2, 4, 'Dịch vụ tốt, đồ ăn ngon, tuy nhiên lịch trình hơi gấp.', 'Đã duyệt', NOW()),
(3, 3, 2, 5, 'Chuyến đi châu Âu rất đáng nhớ. Cảm ơn công ty!', 'Đã duyệt', NOW());

-- Dữ liệu mẫu cho bảng 'favorite'
INSERT INTO `favorite` (`id`, `user_id`, `tour_id`) VALUES
(1, 2, 3),
(2, 2, 5),
(3, 2, 6);

-- Dữ liệu mẫu cho bảng 'payment_method'
INSERT INTO `payment_method` (`id`, `ten_phuong_thuc`, `booking_id`) VALUES
(1, 'Thanh toán khi nhận tour', 1),
(2, 'Chuyển khoản ngân hàng', 1);

-- Dữ liệu mẫu cho bảng 'voucher'
INSERT INTO `voucher` (`id`, `ma_giam_gia`, `gia_tri`, `ngay_het_han`, `dieu_kien_ap_dung`) VALUES
(1, 'HE2024', 100000.00, '2024-09-30', 'Áp dụng cho các tour trong nước có giá trị từ 5,000,000 VNĐ'),
(2, 'SUMMER10', 500000.00, '2024-08-31', 'Giảm 10% cho các tour nước ngoài');

-- Dữ liệu mẫu cho bảng 'payment'
INSERT INTO `payment` (`id`, `ho_ten_nguoi_nhan`, `so_tai_khoan`, `ten_ngan_hang`, `chi_nhanh`, `email`, `sdt`, `payment_method_id`, `booking_id`) VALUES
(1, 'Hoang Minh Thien', '0329175309', 'MBbank', 'Hà Nội', 'thienkt179@gmail.com', '0329175309', 2, 1); 

-- Dữ liệu mẫu cho bảng 'notification'
INSERT INTO `notification` (`user_id`, `message`, `is_read`, `created_at`) VALUES
(1, 'Chào mừng bạn đến với hệ thống!', 0, NOW()),
(2, 'Bạn vừa đặt tour thành công.', 0, NOW()); 

-- Dữ liệu mẫu cho bảng 'user_voucher'
INSERT INTO `user_voucher` (`id`, `user_id`, `voucher_id`, `is_used`, `received_at`) VALUES
(1, 2, 1, 0, NOW()),
(2, 2, 2, 0, NOW()); 