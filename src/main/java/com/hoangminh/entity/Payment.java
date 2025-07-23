package com.hoangminh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payment", schema = "jsb_tour")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ho_ten_nguoi_nhan", nullable = false)
    private String hoTenNguoiNhan;

    @Column(name = "so_tai_khoan", nullable = false)
    private String soTaiKhoan;

    @Column(name = "ten_ngan_hang", nullable = false)
    private String tenNganHang;

    @Column(name = "chi_nhanh")
    private String chiNhanh;

    private String email;

    private String sdt;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
}