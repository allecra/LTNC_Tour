package com.hoangminh.service.impl;

import com.hoangminh.dto.VoucherDTO;
import com.hoangminh.entity.Voucher;
import com.hoangminh.repository.VoucherRepository;
import com.hoangminh.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoucherServiceImpl implements VoucherService {
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
            if (v == null) return false;
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
    public boolean isValidVoucher(String code) {
        try {
            Optional<Voucher> voucherOpt = voucherRepository.findByMaGiamGia(code);
            if (voucherOpt.isPresent()) {
                Voucher voucher = voucherOpt.get();
                // Kiểm tra voucher còn hạn sử dụng không
                if (voucher.getNgayHetHan() != null && voucher.getNgayHetHan().before(new Date())) {
                    return false;
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public VoucherDTO getByCode(String code) {
        try {
            Optional<Voucher> voucherOpt = voucherRepository.findByMaGiamGia(code);
            if (voucherOpt.isPresent()) {
                return toDTO(voucherOpt.get());
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
} 