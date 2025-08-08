package com.hoangminh.service;

import com.hoangminh.dto.VoucherDTO;
import java.util.List;

public interface VoucherService {
    List<VoucherDTO> getAll();

    boolean add(VoucherDTO dto);

    boolean update(Long id, VoucherDTO dto);

    boolean delete(Long id);

    // Thêm method để lấy voucher còn hiệu lực
    List<VoucherDTO> getActiveVouchers();

    // Kiểm tra voucher có hợp lệ không
    VoucherDTO checkVoucher(String code);

    // Sử dụng voucher
    boolean useVoucher(String code, Long userId);
}