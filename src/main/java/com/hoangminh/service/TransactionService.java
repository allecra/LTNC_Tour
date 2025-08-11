package com.hoangminh.service;

import com.hoangminh.dto.BookingDTOWithTransaction;
import java.util.List;

public interface TransactionService {
    
    // Lấy danh sách booking với mã nội dung chuyển khoản theo user ID
    List<BookingDTOWithTransaction> findBookingWithTransactionByUserId(Long userId);
    
    // Lấy danh sách booking active với mã nội dung chuyển khoản theo user ID
    List<BookingDTOWithTransaction> findActiveBookingWithTransactionByUserId(Long userId);
    
    // Lấy booking với mã nội dung chuyển khoản theo ID
    BookingDTOWithTransaction findBookingWithTransactionById(Long id);
    
    // Lấy tất cả booking với mã nội dung chuyển khoản (có filter)
    List<BookingDTOWithTransaction> findAllBookingWithTransaction(String trang_thai, String ten_tour);
}
