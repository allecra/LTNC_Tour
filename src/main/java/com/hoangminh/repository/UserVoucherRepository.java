package com.hoangminh.repository;

import com.hoangminh.entity.UserVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserVoucherRepository extends JpaRepository<UserVoucher, Long> {
    
    List<UserVoucher> findByUserId(Long userId);
    
    List<UserVoucher> findByUserIdAndIsUsedFalse(Long userId);
    
    @Query("SELECT uv FROM UserVoucher uv WHERE uv.user.id = :userId AND uv.voucher.ngayHetHan > CURRENT_DATE")
    List<UserVoucher> findActiveVouchersByUserId(@Param("userId") Long userId);
    
    @Query("SELECT uv FROM UserVoucher uv WHERE uv.user.id = :userId AND uv.isUsed = false AND uv.voucher.ngayHetHan > CURRENT_DATE")
    List<UserVoucher> findAvailableVouchersByUserId(@Param("userId") Long userId);
} 