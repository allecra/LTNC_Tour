package com.hoangminh.controller.api.admin;

import com.hoangminh.dto.ResponseDTO;
import com.hoangminh.service.ReviewService;
import com.hoangminh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public ResponseDTO getAll() {
        if (!userService.checkAdminLogin()) return new ResponseDTO("Không có quyền truy cập", null);
        return new ResponseDTO("Thành công", reviewService.getAllAdmin());
    }

    @PutMapping("/approve/{id}")
    public ResponseDTO approve(@PathVariable("id") Long id) {
        if (!userService.checkAdminLogin()) return new ResponseDTO("Không có quyền truy cập", null);
        boolean ok = reviewService.approveReview(id);
        return new ResponseDTO(ok ? "Duyệt thành công" : "Duyệt thất bại", null);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO delete(@PathVariable("id") Long id) {
        if (!userService.checkAdminLogin()) return new ResponseDTO("Không có quyền truy cập", null);
        boolean ok = reviewService.deleteReview(id);
        return new ResponseDTO(ok ? "Xóa thành công" : "Xóa thất bại", null);
    }
} 