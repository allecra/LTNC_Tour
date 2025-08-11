package com.hoangminh.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
public class BookingDTO {

    private Long id;

    private Long user_id;

    private Long tour_id;

    private String ten_tour;

    private Integer so_luong_nguoi;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private Date ngay_khoi_hanh;

    private BigDecimal tong_tien;

    private String trang_thai;

    private String payment_method;

    private String ghi_chu;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private Date booking_at;

    private String payment_status;

    // Thêm thông tin voucher
    private String voucherCode;
    private BigDecimal giaTriVoucher;
    private BigDecimal tongTienSauVoucher;

    public String getPayment_status() {
        return payment_status;
    }
    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public BookingDTO(Long id, Long user_id, Long tour_id, String ten_tour, Integer so_luong_nguoi, Date ngay_khoi_hanh,
            BigDecimal tong_tien, String trang_thai, String payment_method, String ghi_chu, Date booking_at) {
        this.id = id;
        this.user_id = user_id;
        this.tour_id = tour_id;
        this.ten_tour = ten_tour;
        this.so_luong_nguoi = so_luong_nguoi;
        this.ngay_khoi_hanh = ngay_khoi_hanh;
        this.tong_tien = tong_tien;
        this.trang_thai = trang_thai;
        this.payment_method = payment_method;
        this.ghi_chu = ghi_chu;
        this.booking_at = booking_at;
    }

    // Constructor mới với thông tin voucher
    public BookingDTO(Long id, Long user_id, Long tour_id, String ten_tour, Integer so_luong_nguoi, Date ngay_khoi_hanh,
            BigDecimal tong_tien, String trang_thai, String payment_method, String ghi_chu, Date booking_at,
            String voucherCode, BigDecimal giaTriVoucher, BigDecimal tongTienSauVoucher) {
        this.id = id;
        this.user_id = user_id;
        this.tour_id = tour_id;
        this.ten_tour = ten_tour;
        this.so_luong_nguoi = so_luong_nguoi;
        this.ngay_khoi_hanh = ngay_khoi_hanh;
        this.tong_tien = tong_tien;
        this.trang_thai = trang_thai;
        this.payment_method = payment_method;
        this.ghi_chu = ghi_chu;
        this.booking_at = booking_at;
        this.voucherCode = voucherCode;
        this.giaTriVoucher = giaTriVoucher;
        this.tongTienSauVoucher = tongTienSauVoucher;
    }
}
