package com.hoangminh.controller.api.admin;

import com.hoangminh.dto.ResponseDTO;
import com.hoangminh.dto.TinTucDTO;
import com.hoangminh.entity.TinTuc;
import com.hoangminh.entity.User;
import com.hoangminh.service.TinTucService;
import com.hoangminh.service.UserService;
import com.hoangminh.utilities.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/tintuc")
public class TinTucController {

    @Autowired
    private TinTucService tinTucService;
    @Autowired
    private UserService userService;

    @GetMapping("/getAllPage")
    public ResponseDTO getAllPage(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageIndex") Integer pageIndex) {
        Page<TinTuc> page = this.tinTucService.getAllPage(PageRequest.of(pageSize, pageIndex));
        List<TinTucDTO> dtoList = page.getContent().stream().map(this::toDTO).collect(Collectors.toList());
        return new ResponseDTO("Thành Công", dtoList);
    }

    @GetMapping("/{id}")
    public ResponseDTO getOnePage(@PathVariable("id") Long id) {
        TinTuc tinTuc = this.tinTucService.findOnePage(id);
        return new ResponseDTO("Thành công", toDTO(tinTuc));
    }

    @PostMapping("/add")
    public ResponseDTO addNewTintuc(@RequestBody TinTucDTO tinTucDTO, @RequestParam("image") MultipartFile image) {
        String uploadDir = "/upload";
        try {
            String fileName = image.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, image);
            tinTucDTO.setHinhAnh(fileName);
            TinTuc tinTuc = toEntity(tinTucDTO);
            return new ResponseDTO("Thành công", toDTO(this.tinTucService.createOnePage(tinTuc)));
        } catch (IOException e) {
            log.info("Lỗi upload file: {}", e.getMessage());
        }
        return new ResponseDTO("Thất bại", null);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseDTO updateOneTinTuc(@PathVariable("id") Long id,
            @RequestBody TinTucDTO tinTucDTO,
            @RequestParam("image") MultipartFile image) {
        String uploadDir = "/upload";
        try {
            String fileName = image.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, image);
            tinTucDTO.setHinhAnh(fileName);
            TinTuc tinTuc = toEntity(tinTucDTO);
            return new ResponseDTO("Thành công", toDTO(this.tinTucService.updateTinTuc(tinTuc, id)));
        } catch (IOException e) {
            log.info("Lỗi upload file: {}", e.getMessage());
        }
        return new ResponseDTO("Cập nhật thất bại", null);
    }

    private TinTucDTO toDTO(TinTuc tinTuc) {
        if (tinTuc == null)
            return null;
        TinTucDTO dto = new TinTucDTO();
        dto.setId(tinTuc.getId());
        dto.setTieuDe(tinTuc.getTieu_de());
        dto.setTomTat(tinTuc.getTom_tat());
        dto.setNoiDung(tinTuc.getNoi_dung());
        dto.setHinhAnh(tinTuc.getHinh_anh());
        dto.setNgayDang(tinTuc.getNgay_dang());
        dto.setTrangThai(tinTuc.getTrangThai());
        dto.setAuthorId(tinTuc.getAuthor() != null ? tinTuc.getAuthor().getId() : null);
        return dto;
    }

    private TinTuc toEntity(TinTucDTO dto) {
        TinTuc tinTuc = new TinTuc();
        tinTuc.setId(dto.getId());
        tinTuc.setTieu_de(dto.getTieuDe());
        tinTuc.setTom_tat(dto.getTomTat());
        tinTuc.setNoi_dung(dto.getNoiDung());
        tinTuc.setHinh_anh(dto.getHinhAnh());
        tinTuc.setNgay_dang(dto.getNgayDang());
        tinTuc.setTrangThai(dto.getTrangThai());
        if (dto.getAuthorId() != null) {
            User author = userService.findUserById(dto.getAuthorId());
            tinTuc.setAuthor(author);
        }
        return tinTuc;
    }
}
