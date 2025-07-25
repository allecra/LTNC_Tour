package com.hoangminh.service;

import com.hoangminh.dto.PaymentMethodDTO;
import java.util.List;

public interface PaymentMethodService {
    List<PaymentMethodDTO> getAll();
    PaymentMethodDTO update(Long id, PaymentMethodDTO dto);
    boolean delete(Long id);
} 