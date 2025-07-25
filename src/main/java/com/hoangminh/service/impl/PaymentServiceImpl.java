package com.hoangminh.service.impl;

import com.hoangminh.dto.PaymentDTO;
import com.hoangminh.entity.Payment;
import com.hoangminh.repository.PaymentRepository;
import com.hoangminh.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository repo;

    @Override
    public List<PaymentDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    private PaymentDTO toDTO(Payment p) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(p.getId());
        dto.setHoTenNguoiNhan(p.getHoTenNguoiNhan());
        dto.setSoTaiKhoan(p.getSoTaiKhoan());
        dto.setTenNganHang(p.getTenNganHang());
        dto.setChiNhanh(p.getChiNhanh());
        dto.setEmail(p.getEmail());
        dto.setSdt(p.getSdt());
        dto.setPaymentMethod(p.getPaymentMethod() != null ? p.getPaymentMethod().getTen_phuong_thuc() : "");
        dto.setBookingId(p.getBooking() != null ? p.getBooking().getId() : null);
        return dto;
    }
} 