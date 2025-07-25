package com.hoangminh.controller.api.admin;

import com.hoangminh.dto.ResponseDTO;
import com.hoangminh.service.PaymentMethodService;
import com.hoangminh.service.UserService;
import com.hoangminh.dto.PaymentMethodDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment-method")
public class PaymentMethodController {
    @Autowired
    private PaymentMethodService service;
    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public ResponseDTO getAll() {
        if (!userService.checkAdminLogin()) return new ResponseDTO("Không có quyền truy cập", null);
        return new ResponseDTO("Thành công", service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseDTO update(@PathVariable("id") Long id, @RequestBody PaymentMethodDTO dto) {
        if (!userService.checkAdminLogin()) return new ResponseDTO("Không có quyền truy cập", null);
        return new ResponseDTO("Thành công", service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseDTO delete(@PathVariable("id") Long id) {
        if (!userService.checkAdminLogin()) return new ResponseDTO("Không có quyền truy cập", null);
        return new ResponseDTO("Thành công", service.delete(id));
    }
} 