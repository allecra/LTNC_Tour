package com.hoangminh.service;

import com.hoangminh.dto.VoucherDTO;
import java.util.List;

public interface UserVoucherService {
    
    // Lấy tất cả voucher của user
    List<VoucherDTO> getUserVouchers(Long userId);
    
    // Lấy voucher có thể sử dụng của user
    List<VoucherDTO> getAvailableUserVouchers(Long userId);
    
    // Thêm voucher cho user
    boolean addVoucherToUser(Long userId, Long voucherId);
    
    // Sử dụng voucher
    boolean useVoucher(Long userVoucherId);
    
    // Kiểm tra voucher có hợp lệ không
    boolean isValidVoucher(Long userId, String voucherCode);
    
    // Lấy thông tin voucher theo mã
    VoucherDTO getVoucherByCode(String voucherCode);
} 