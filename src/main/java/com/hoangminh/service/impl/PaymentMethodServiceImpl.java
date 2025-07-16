package com.hoangminh.service.impl;

import com.hoangminh.entity.PaymentMethod;
import com.hoangminh.repository.PaymentMethodRepository;
import com.hoangminh.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public List<PaymentMethod> findAll() {
        return paymentMethodRepository.findAll();
    }

    @Override
    public Optional<PaymentMethod> findById(Integer id) {
        return paymentMethodRepository.findById(id);
    }

    @Override
    public PaymentMethod save(PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public void deleteById(Integer id) {
        paymentMethodRepository.deleteById(id);
    }
}