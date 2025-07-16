package com.hoangminh.service;

import com.hoangminh.entity.PaymentMethod;
import java.util.List;
import java.util.Optional;

public interface PaymentMethodService {
    List<PaymentMethod> findAll();

    Optional<PaymentMethod> findById(Integer id);

    PaymentMethod save(PaymentMethod paymentMethod);

    void deleteById(Integer id);
}