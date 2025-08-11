package com.hoangminh.controller.api.user;

import com.hoangminh.dto.BookingDTOWithTransaction;
import com.hoangminh.dto.ResponseDTO;
import com.hoangminh.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // API lấy danh sách booking với mã nội dung chuyển khoản theo user ID
    @GetMapping("/user/{userId}/bookings")
    public ResponseEntity<ResponseDTO> getBookingWithTransactionByUserId(@PathVariable Long userId) {
        List<BookingDTOWithTransaction> bookings = transactionService.findBookingWithTransactionByUserId(userId);
        return ResponseEntity.ok(new ResponseDTO("Lấy danh sách booking với mã nội dung chuyển khoản thành công", bookings));
    }

    // API lấy danh sách booking active với mã nội dung chuyển khoản theo user ID
    @GetMapping("/user/{userId}/active-bookings")
    public ResponseEntity<ResponseDTO> getActiveBookingWithTransactionByUserId(@PathVariable Long userId) {
        List<BookingDTOWithTransaction> bookings = transactionService.findActiveBookingWithTransactionByUserId(userId);
        return ResponseEntity.ok(new ResponseDTO("Lấy danh sách booking active với mã nội dung chuyển khoản thành công", bookings));
    }

    // API lấy booking với mã nội dung chuyển khoản theo ID
    @GetMapping("/booking/{id}")
    public ResponseEntity<ResponseDTO> getBookingWithTransactionById(@PathVariable Long id) {
        BookingDTOWithTransaction booking = transactionService.findBookingWithTransactionById(id);
        if (booking != null) {
            return ResponseEntity.ok(new ResponseDTO("Lấy thông tin booking với mã nội dung chuyển khoản thành công", booking));
        }
        return ResponseEntity.notFound().build();
    }

    // API lấy tất cả booking với mã nội dung chuyển khoản (có filter)
    @GetMapping("/bookings")
    public ResponseEntity<ResponseDTO> getAllBookingWithTransaction(
            @RequestParam(value = "trang_thai", required = false) String trang_thai,
            @RequestParam(value = "ten_tour", required = false) String ten_tour) {
        List<BookingDTOWithTransaction> bookings = transactionService.findAllBookingWithTransaction(trang_thai, ten_tour);
        return ResponseEntity.ok(new ResponseDTO("Lấy danh sách booking với mã nội dung chuyển khoản thành công", bookings));
    }
}
