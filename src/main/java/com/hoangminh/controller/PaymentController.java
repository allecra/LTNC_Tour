package com.hoangminh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hoangminh.service.VoucherService;
import com.hoangminh.service.UserService;
import com.hoangminh.service.BookingService;
import com.hoangminh.dto.VoucherDTO;
import com.hoangminh.dto.BookingDTO;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    private Long getUserIdFromSession() {
        return userService.getCurrentUserId();
    }

    @PostMapping("/user/check-voucher")
    public ResponseEntity<?> checkVoucher(@RequestBody Map<String, String> request) {
        String voucherCode = request.get("voucherCode");
        try {
            VoucherDTO voucher = voucherService.checkVoucher(voucherCode);
            if (voucher != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("discount", voucher.getDiscount());
                response.put("dieuKien", voucher.getDieuKien());
                response.put("id", voucher.getId());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.ok(Map.of("success", false, "message", "Voucher không hợp lệ hoặc đã sử dụng"));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("success", false, "message", "Có lỗi khi kiểm tra voucher"));
        }
    }

    @PostMapping("/user/booking/confirm-payment")
    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, Object> request) {
        Long bookingId = Long.valueOf(request.get("bookingId").toString());
        String paymentMethod = request.get("paymentMethod").toString();
        Boolean paymentConfirmed = Boolean.valueOf(request.get("paymentConfirmed").toString());
        String voucherCode = request.get("voucherCode") != null ? request.get("voucherCode").toString() : null;

        try {
            // Kiểm tra booking có tồn tại không
            BookingDTO booking = bookingService.getBookingById(bookingId);
            if (booking == null) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Không tìm thấy thông tin đặt tour"));
            }

            // Xử lý voucher nếu có
            if (voucherCode != null && !voucherCode.trim().isEmpty()) {
                VoucherDTO voucher = voucherService.checkVoucher(voucherCode);
                if (voucher == null) {
                    return ResponseEntity.ok(Map.of(
                            "success", false,
                            "message", "Voucher không hợp lệ hoặc đã được sử dụng"));
                }

                // Lấy userId từ session
                Long userId = getUserIdFromSession();
                if (userId == null) {
                    return ResponseEntity.ok(Map.of(
                            "success", false,
                            "message", "Vui lòng đăng nhập để sử dụng voucher"));
                }

                if (!voucherService.useVoucher(voucherCode, userId)) {
                    return ResponseEntity.ok(Map.of(
                            "success", false,
                            "message", "Không thể sử dụng voucher này"));
                }

                // Cập nhật giá sau khi áp dụng voucher
                BigDecimal discountAmount = booking.getTong_tien().multiply(BigDecimal.valueOf(voucher.getDiscount()));
                booking.setTong_tien(booking.getTong_tien().subtract(discountAmount));
            }

            // Xử lý xác nhận thanh toán
            if (paymentMethod.equals("Chuyển khoản") && !paymentConfirmed) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Vui lòng xác nhận đã thanh toán"));
            }

            // Cập nhật trạng thái thanh toán
            booking.setPayment_status(paymentMethod.equals("Chuyển khoản") ? "Đã thanh toán" : "Chưa thanh toán");
            booking.setPayment_method(paymentMethod);

            if (!bookingService.updateBooking(bookingId, booking)) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Không thể cập nhật thông tin thanh toán"));
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Thanh toán thành công",
                    "booking", booking));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Map.of("success", false, "message", "Có lỗi khi xác nhận thanh toán: " + e.getMessage()));
        }
    }
}
