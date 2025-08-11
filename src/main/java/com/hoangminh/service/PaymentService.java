package com.hoangminh.service;

import com.hoangminh.dto.PaymentDTO;
import com.hoangminh.entity.Payment;
import java.util.List;

public interface PaymentService {
    List<PaymentDTO> getAll();
    Payment update(Long id, Payment payment);
    boolean delete(Long id);
} 