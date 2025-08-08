package com.hoangminh.service.impl;

import com.hoangminh.dto.VoucherDTO;
import com.hoangminh.entity.Voucher;
import com.hoangminh.repository.VoucherRepository;
import com.hoangminh.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VoucherServiceImpl implements VoucherService {
    private static final Logger logger = LoggerFactory.getLogger(VoucherServiceImpl.class);
    @Autowired
    private VoucherRepository voucherRepository;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<VoucherDTO> getAll() {
        return voucherRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public boolean add(VoucherDTO dto) {
        try {
            Voucher v = new Voucher();
            v.setMaGiamGia(dto.getCode());
            v.setGiaTri(dto.getDiscount() != null ? java.math.BigDecimal.valueOf(dto.getDiscount()) : null);
            v.setNgayHetHan(dto.getExpiry_date() != null ? sdf.parse(dto.getExpiry_date()) : null);
            v.setDieuKienApDung(dto.getDieuKien());
            voucherRepository.save(v);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(Long id, VoucherDTO dto) {
        try {
            Voucher v = voucherRepository.findById(id).orElse(null);
            if (v == null)
                return false;
            v.setMaGiamGia(dto.getCode());
            v.setGiaTri(dto.getDiscount() != null ? java.math.BigDecimal.valueOf(dto.getDiscount()) : null);
            v.setNgayHetHan(dto.getExpiry_date() != null ? sdf.parse(dto.getExpiry_date()) : null);
            v.setDieuKienApDung(dto.getDieuKien());
            voucherRepository.save(v);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(Long id) {
        if (voucherRepository.existsById(id)) {
            voucherRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<VoucherDTO> getActiveVouchers() {
        try {
            Date currentDate = new Date();
            logger.info("Fetching active vouchers, current date: {}", currentDate);

            List<Voucher> allVouchers = voucherRepository.findAll();
            logger.info("Total vouchers found: {}", allVouchers.size());

            List<VoucherDTO> activeVouchers = allVouchers.stream()
                    .filter(v -> {
                        // Một voucher được coi là active khi:
                        // 1. Có ngày hết hạn và ngày hết hạn sau ngày hiện tại
                        boolean isValid = v.getNgayHetHan() != null && v.getNgayHetHan().after(currentDate);

                        logger.info("Voucher {} (code: {}): ngayHetHan={}, isValid={}",
                                v.getId(),
                                v.getMaGiamGia(),
                                v.getNgayHetHan() != null ? sdf.format(v.getNgayHetHan()) : "null",
                                isValid);

                        return isValid;
                    })
                    .map(v -> {
                        VoucherDTO dto = toDTO(v);
                        logger.info("Mapped active voucher to DTO: {}", dto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            logger.info("Returning {} active vouchers", activeVouchers.size());
            return activeVouchers;

        } catch (Exception e) {
            logger.error("Error while fetching active vouchers: ", e);
            return new ArrayList<>();
        }
    }

    private VoucherDTO toDTO(Voucher v) {
        VoucherDTO dto = new VoucherDTO();
        dto.setId(v.getId());
        dto.setCode(v.getMaGiamGia());
        dto.setDiscount(v.getGiaTri() != null ? v.getGiaTri().doubleValue() : null);
        dto.setExpiry_date(v.getNgayHetHan() != null ? sdf.format(v.getNgayHetHan()) : null);
        dto.setDieuKien(v.getDieuKienApDung());
        return dto;
    }

    @Override
    public VoucherDTO checkVoucher(String code) {
        try {
            if (code == null || code.trim().isEmpty()) {
                return null;
            }

            Date currentDate = new Date();
            Voucher voucher = voucherRepository.findByMaGiamGia(code);

            if (voucher == null) {
                logger.info("Voucher not found: {}", code);
                return null;
            }

            if (voucher.getNgayHetHan() != null && voucher.getNgayHetHan().before(currentDate)) {
                logger.info("Voucher expired: {}", code);
                return null;
            }

            logger.info("Valid voucher found: {}", code);
            return toDTO(voucher);
        } catch (Exception e) {
            logger.error("Error checking voucher: ", e);
            return null;
        }
    }

    @Override
    public boolean useVoucher(String code, Long userId) {
        try {
            if (code == null || code.trim().isEmpty() || userId == null) {
                return false;
            }

            Voucher voucher = voucherRepository.findByMaGiamGia(code);
            if (voucher == null) {
                logger.info("Voucher not found when trying to use: {}", code);
                return false;
            }

            // TODO: Add logic to mark voucher as used for the specific user
            // This could involve creating a new table to track voucher usage
            logger.info("Voucher {} used by user {}", code, userId);
            return true;
        } catch (Exception e) {
            logger.error("Error using voucher: ", e);
            return false;
        }
    }
}