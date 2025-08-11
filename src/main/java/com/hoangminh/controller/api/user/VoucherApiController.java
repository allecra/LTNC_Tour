package com.hoangminh.controller.api.user;

import com.hoangminh.dto.ResponseDTO;
import com.hoangminh.dto.VoucherDTO;
import com.hoangminh.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/voucher")
public class VoucherApiController {
    
    @Autowired
    private VoucherService voucherService;
    
    @GetMapping("/validate")
    public ResponseDTO validateVoucher(@RequestParam("code") String code) {
        try {
            if (code == null || code.trim().isEmpty()) {
                return new ResponseDTO("Mã voucher không được để trống", null);
            }
            
            boolean isValid = voucherService.isValidVoucher(code.trim());
            if (isValid) {
                VoucherDTO voucher = voucherService.getByCode(code.trim());
                return new ResponseDTO("Voucher hợp lệ", voucher);
            } else {
                return new ResponseDTO("Voucher không hợp lệ hoặc đã hết hạn", null);
            }
        } catch (Exception e) {
            return new ResponseDTO("Có lỗi xảy ra khi kiểm tra voucher", null);
        }
    }
}
