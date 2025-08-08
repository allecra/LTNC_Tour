-- Thêm voucher mẫu cho test
INSERT INTO `voucher` (`ma_giam_gia`, `gia_tri`, `ngay_het_han`, `dieu_kien_ap_dung`) VALUES
('EXPIRED2023', 100000.00, '2023-12-31', 'Voucher đã hết hạn'),
('TEST2024', 200000.00, NULL, 'Voucher không có ngày hết hạn'),
('FUTURE2025', 300000.00, '2025-12-31', 'Voucher còn thời gian dài'),
('SOON2024', 400000.00, DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY), 'Voucher sắp hết hạn');

-- Thêm user_voucher mẫu
INSERT INTO `user_voucher` (`user_id`, `voucher_id`, `is_used`, `received_at`) VALUES
(2, (SELECT id FROM voucher WHERE ma_giam_gia = 'EXPIRED2023'), 0, '2023-01-01'),
(2, (SELECT id FROM voucher WHERE ma_giam_gia = 'TEST2024'), 0, CURRENT_DATE),
(2, (SELECT id FROM voucher WHERE ma_giam_gia = 'FUTURE2025'), 0, CURRENT_DATE),
(2, (SELECT id FROM voucher WHERE ma_giam_gia = 'SOON2024'), 1, CURRENT_DATE);
