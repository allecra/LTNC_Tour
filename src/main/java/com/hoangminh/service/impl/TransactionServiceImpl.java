package com.hoangminh.service.impl;

import com.hoangminh.dto.BookingDTOWithTransaction;
import com.hoangminh.repository.TransactionRepository;
import com.hoangminh.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<BookingDTOWithTransaction> findBookingWithTransactionByUserId(Long userId) {
        return transactionRepository.findBookingWithTransactionByUserId(userId);
    }

    @Override
    public List<BookingDTOWithTransaction> findActiveBookingWithTransactionByUserId(Long userId) {
        return transactionRepository.findActiveBookingWithTransactionByUserId(userId);
    }

    @Override
    public BookingDTOWithTransaction findBookingWithTransactionById(Long id) {
        return transactionRepository.findBookingWithTransactionById(id);
    }

    @Override
    public List<BookingDTOWithTransaction> findAllBookingWithTransaction(String trang_thai, String ten_tour) {
        return transactionRepository.findAllBookingWithTransaction(trang_thai, ten_tour);
    }
}
