package com.hoangminh.controller.api.admin;

import com.hoangminh.dto.VoucherDTO;
import com.hoangminh.entity.Voucher;
import com.hoangminh.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/voucher")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @GetMapping("")
    public List<VoucherDTO> getAll() {
        return voucherService.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public VoucherDTO getById(@PathVariable Integer id) {
        Optional<Voucher> voucher = voucherService.findById(id);
        return voucher.map(this::toDTO).orElse(null);
    }

    @PostMapping("")
    public VoucherDTO create(@RequestBody VoucherDTO dto) {
        Voucher entity = toEntity(dto);
        return toDTO(voucherService.save(entity));
    }

    @PutMapping("/{id}")
    public VoucherDTO update(@PathVariable Integer id, @RequestBody VoucherDTO dto) {
        dto.setId(id);
        Voucher entity = toEntity(dto);
        return toDTO(voucherService.save(entity));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        voucherService.deleteById(id);
    }

    private VoucherDTO toDTO(Voucher entity) {
        VoucherDTO dto = new VoucherDTO();
        dto.setId(entity.getId());
        dto.setMaGiamGia(entity.getMaGiamGia());
        dto.setGiaTri(entity.getGiaTri());
        dto.setNgayHetHan(entity.getNgayHetHan());
        dto.setDieuKienApDung(entity.getDieuKienApDung());
        return dto;
    }

    private Voucher toEntity(VoucherDTO dto) {
        Voucher entity = new Voucher();
        entity.setId(dto.getId());
        entity.setMaGiamGia(dto.getMaGiamGia());
        entity.setGiaTri(dto.getGiaTri());
        entity.setNgayHetHan(dto.getNgayHetHan());
        entity.setDieuKienApDung(dto.getDieuKienApDung());
        return entity;
    }
}