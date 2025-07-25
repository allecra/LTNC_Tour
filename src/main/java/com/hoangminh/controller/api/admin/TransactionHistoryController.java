package com.hoangminh.controller.api.admin;

import com.hoangminh.dto.ResponseDTO;
import com.hoangminh.service.PaymentService;
import com.hoangminh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class TransactionHistoryController {
    @Autowired
    private PaymentService service;
    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public ResponseDTO getAll() {
        if (!userService.checkAdminLogin()) return new ResponseDTO("Không có quyền truy cập", null);
        return new ResponseDTO("Thành công", service.getAll());
    }
} 