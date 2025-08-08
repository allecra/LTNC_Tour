package com.hoangminh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
public class TransactionTableDTO {
    
    private Integer stt;
    private String maGiaoDich;
    private String khachHang;
    private BigDecimal soTien;
    private String phuongThuc;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private Date thoiGian;
    
    private String trangThai;
    
    // Constructor
    public TransactionTableDTO(Integer stt, String maGiaoDich, String khachHang, 
                              BigDecimal soTien, String phuongThuc, Date thoiGian, String trangThai) {
        this.stt = stt;
        this.maGiaoDich = maGiaoDich;
        this.khachHang = khachHang;
        this.soTien = soTien;
        this.phuongThuc = phuongThuc;
        this.thoiGian = thoiGian;
        this.trangThai = trangThai;
    }
}
