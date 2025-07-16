package com.hoangminh.controller.api.admin;

import com.hoangminh.dto.PaymentMethodDTO;
import com.hoangminh.entity.PaymentMethod;
import com.hoangminh.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payment-method")
public class PaymentMethodController {
    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping("")
    public List<PaymentMethodDTO> getAll() {
        return paymentMethodService.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PaymentMethodDTO getById(@PathVariable Integer id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodService.findById(id);
        return paymentMethod.map(this::toDTO).orElse(null);
    }

    @PostMapping("")
    public PaymentMethodDTO create(@RequestBody PaymentMethodDTO dto) {
        PaymentMethod entity = toEntity(dto);
        return toDTO(paymentMethodService.save(entity));
    }

    @PutMapping("/{id}")
    public PaymentMethodDTO update(@PathVariable Integer id, @RequestBody PaymentMethodDTO dto) {
        dto.setId(id);
        PaymentMethod entity = toEntity(dto);
        return toDTO(paymentMethodService.save(entity));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        paymentMethodService.deleteById(id);
    }

    private PaymentMethodDTO toDTO(PaymentMethod entity) {
        PaymentMethodDTO dto = new PaymentMethodDTO();
        dto.setId(entity.getId());
        dto.setTenPhuongThuc(entity.getTenPhuongThuc());
        return dto;
    }

    private PaymentMethod toEntity(PaymentMethodDTO dto) {
        PaymentMethod entity = new PaymentMethod();
        entity.setId(dto.getId());
        entity.setTenPhuongThuc(dto.getTenPhuongThuc());
        return entity;
    }
}