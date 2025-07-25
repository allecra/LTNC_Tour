package com.hoangminh.dto;
import lombok.Data;

@Data
public class PaymentDTO {
    private Long id;
    private String hoTenNguoiNhan;
    private String soTaiKhoan;
    private String tenNganHang;
    private String chiNhanh;
    private String email;
    private String sdt;
    private String paymentMethod;
    private Long bookingId;
} 