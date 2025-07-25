package com.hoangminh.service.impl;

import com.hoangminh.dto.PaymentMethodDTO;
import com.hoangminh.entity.PaymentMethod;
import com.hoangminh.repository.PaymentMethodRepository;
import com.hoangminh.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
    @Autowired
    private PaymentMethodRepository repo;

    @Override
    public List<PaymentMethodDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public PaymentMethodDTO update(Long id, PaymentMethodDTO dto) {
        PaymentMethod entity = repo.findById(id).orElse(null);
        if (entity == null) return null;
        entity.setTen_phuong_thuc(dto.getName());
        entity.setMoTa(dto.getDescription());
        entity.setTrangThai(dto.getStatus());
        repo.save(entity);
        return toDTO(entity);
    }

    @Override
    public boolean delete(Long id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }

    private PaymentMethodDTO toDTO(PaymentMethod p) {
        PaymentMethodDTO dto = new PaymentMethodDTO();
        dto.setId(p.getId());
        dto.setName(p.getTen_phuong_thuc());
        dto.setDescription(p.getMoTa());
        dto.setStatus(p.getTrangThai());
        return dto;
    }
} 