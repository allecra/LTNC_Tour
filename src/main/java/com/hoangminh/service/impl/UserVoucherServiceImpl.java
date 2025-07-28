package com.hoangminh.service.impl;

import com.hoangminh.dto.VoucherDTO;
import com.hoangminh.entity.User;
import com.hoangminh.entity.UserVoucher;
import com.hoangminh.entity.Voucher;
import com.hoangminh.repository.UserRepository;
import com.hoangminh.repository.UserVoucherRepository;
import com.hoangminh.repository.VoucherRepository;
import com.hoangminh.service.UserVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserVoucherServiceImpl implements UserVoucherService {

    @Autowired
    private UserVoucherRepository userVoucherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<VoucherDTO> getUserVouchers(Long userId) {
        return userVoucherRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VoucherDTO> getAvailableUserVouchers(Long userId) {
        return userVoucherRepository.findAvailableVouchersByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean addVoucherToUser(Long userId, Long voucherId) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            Voucher voucher = voucherRepository.findById(voucherId).orElse(null);
            
            if (user != null && voucher != null) {
                // Kiểm tra xem user đã có voucher này chưa
                List<UserVoucher> existingVouchers = userVoucherRepository.findByUserId(userId);
                boolean alreadyHasVoucher = existingVouchers.stream()
                        .anyMatch(uv -> uv.getVoucher().getId().equals(voucherId));
                
                if (!alreadyHasVoucher) {
                    UserVoucher userVoucher = new UserVoucher();
                    userVoucher.setUser(user);
                    userVoucher.setVoucher(voucher);
                    userVoucher.setIsUsed(false);
                    userVoucher.setReceivedAt(new Date());
                    userVoucherRepository.save(userVoucher);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean useVoucher(Long userVoucherId) {
        try {
            UserVoucher userVoucher = userVoucherRepository.findById(userVoucherId).orElse(null);
            if (userVoucher != null && !userVoucher.getIsUsed()) {
                userVoucher.setIsUsed(true);
                userVoucher.setUsedAt(new Date());
                userVoucherRepository.save(userVoucher);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isValidVoucher(Long userId, String voucherCode) {
        List<UserVoucher> userVouchers = userVoucherRepository.findAvailableVouchersByUserId(userId);
        return userVouchers.stream()
                .anyMatch(uv -> uv.getVoucher().getMaGiamGia().equals(voucherCode));
    }

    @Override
    public VoucherDTO getVoucherByCode(String voucherCode) {
        // Tìm voucher trong bảng voucher chung
        Voucher voucher = voucherRepository.findByMaGiamGia(voucherCode);
        if (voucher != null) {
            return convertVoucherToDTO(voucher);
        }
        return null;
    }

    private VoucherDTO convertToDTO(UserVoucher userVoucher) {
        VoucherDTO dto = new VoucherDTO();
        dto.setId(userVoucher.getVoucher().getId());
        dto.setCode(userVoucher.getVoucher().getMaGiamGia());
        dto.setDiscount(userVoucher.getVoucher().getGiaTri() != null ? 
                userVoucher.getVoucher().getGiaTri().doubleValue() : null);
        dto.setExpiry_date(userVoucher.getVoucher().getNgayHetHan() != null ? 
                sdf.format(userVoucher.getVoucher().getNgayHetHan()) : null);
        dto.setDieuKien(userVoucher.getVoucher().getDieuKienApDung());
        dto.setIsUsed(userVoucher.getIsUsed());
        dto.setReceivedAt(userVoucher.getReceivedAt() != null ? 
                sdf.format(userVoucher.getReceivedAt()) : null);
        return dto;
    }

    private VoucherDTO convertVoucherToDTO(Voucher voucher) {
        VoucherDTO dto = new VoucherDTO();
        dto.setId(voucher.getId());
        dto.setCode(voucher.getMaGiamGia());
        dto.setDiscount(voucher.getGiaTri() != null ? voucher.getGiaTri().doubleValue() : null);
        dto.setExpiry_date(voucher.getNgayHetHan() != null ? sdf.format(voucher.getNgayHetHan()) : null);
        dto.setDieuKien(voucher.getDieuKienApDung());
        return dto;
    }
} 