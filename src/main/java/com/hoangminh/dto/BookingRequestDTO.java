package com.hoangminh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {
    
    private Long userId;
    
    private Long tourId;
    
    private Integer soLuongNguoi;
    
    private String paymentMethod;
    
    private String ghiChu;
    
    private String voucherCode;
    
    private Date ngayKhoiHanh;
    
    // Validation method
    public boolean isValid() {
        return userId != null && 
               tourId != null && 
               soLuongNguoi != null && 
               soLuongNguoi > 0 && 
               paymentMethod != null && 
               !paymentMethod.trim().isEmpty() &&
               isVoucherValid();
    }
    
    // Validation method for voucher
    public boolean isVoucherValid() {
        if (voucherCode == null || voucherCode.trim().isEmpty()) {
            return true; // Không có voucher vẫn hợp lệ
        }
        // Voucher code phải có ít nhất 3 ký tự
        return voucherCode.trim().length() >= 3;
    }
}
