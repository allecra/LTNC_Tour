package com.hoangminh.service;

import com.hoangminh.dto.VoucherDTO;
import java.util.List;

public interface VoucherService {
    List<VoucherDTO> getAll();
    boolean add(VoucherDTO dto);
    boolean update(Long id, VoucherDTO dto);
    boolean delete(Long id);
} 