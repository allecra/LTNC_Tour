package com.hoangminh.repository;

import com.hoangminh.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    
    Voucher findByMaGiamGia(String maGiamGia);
    
    List<Voucher> findByNgayHetHanAfter(java.util.Date currentDate);
}