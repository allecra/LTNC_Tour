package com.hoangminh.controller.api.admin;

import com.hoangminh.dto.ResponseDTO;
import com.hoangminh.dto.VoucherDTO;
import com.hoangminh.service.UserService;
import com.hoangminh.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/voucher")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public ResponseDTO getAll() {
        if (!userService.checkAdminLogin()) return new ResponseDTO("Không có quyền truy cập", null);
        return new ResponseDTO("Thành công", voucherService.getAll());
    }

    @PostMapping("")
    public ResponseDTO add(@RequestBody VoucherDTO dto) {
        if (!userService.checkAdminLogin()) return new ResponseDTO("Không có quyền truy cập", null);
        boolean ok = voucherService.add(dto);
        return new ResponseDTO(ok ? "Thêm thành công" : "Thêm thất bại", null);
    }

    @PutMapping("/{id}")
    public ResponseDTO update(@PathVariable("id") Long id, @RequestBody VoucherDTO dto) {
        if (!userService.checkAdminLogin()) return new ResponseDTO("Không có quyền truy cập", null);
        boolean ok = voucherService.update(id, dto);
        return new ResponseDTO(ok ? "Cập nhật thành công" : "Cập nhật thất bại", null);
    }

    @DeleteMapping("/{id}")
    public ResponseDTO delete(@PathVariable("id") Long id) {
        if (!userService.checkAdminLogin()) return new ResponseDTO("Không có quyền truy cập", null);
        boolean ok = voucherService.delete(id);
        return new ResponseDTO(ok ? "Xóa thành công" : "Xóa thất bại", null);
    }
} 