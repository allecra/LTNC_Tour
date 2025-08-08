package com.hoangminh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
public class BookingDetailTableDTO {
    
    private Long stt;
    private String nguoiDung;
    private String tour;
    private Integer soLuongNguoi;
    private BigDecimal tongTien;
    private String phuongThucThanhToan;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private Date ngayKhoiHanh;
    
    private String ghiChu;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private Date ngayDat;
    
    private String trangThai;
    private String hanhDong;
    
    // Constructor
    public BookingDetailTableDTO(Long stt, String nguoiDung, String tour, 
                                Integer soLuongNguoi, BigDecimal tongTien, 
                                String phuongThucThanhToan, Date ngayKhoiHanh, 
                                String ghiChu, Date ngayDat, String trangThai) {
        this.stt = stt;
        this.nguoiDung = nguoiDung;
        this.tour = tour;
        this.soLuongNguoi = soLuongNguoi;
        this.tongTien = tongTien;
        this.phuongThucThanhToan = phuongThucThanhToan;
        this.ngayKhoiHanh = ngayKhoiHanh;
        this.ghiChu = ghiChu;
        this.ngayDat = ngayDat;
        this.trangThai = trangThai;
        this.hanhDong = "..."; // Placeholder cho nút hành động
    }
}
