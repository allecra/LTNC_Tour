package com.hoangminh.controller.api.admin;

import com.hoangminh.dto.ResponseDTO;
import com.hoangminh.entity.TinTuc;
import com.hoangminh.entity.Tour;
import com.hoangminh.service.TinTucService;
import com.hoangminh.utilities.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/tintuc")
public class TinTucController {

    @Autowired
    private TinTucService tinTucService;

    @GetMapping("/getAllPage")
    public ResponseDTO getAllPage(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageIndex") Integer pageIndex) {
        Page<TinTuc> page = this.tinTucService.getAllPage(PageRequest.of(pageIndex, pageSize));

        return new ResponseDTO("Thành Công", page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseDTO getOnePage(@PathVariable("id") Long id) {

        return new ResponseDTO("Thành công", this.tinTucService.findOnePage(id));

    }

    @PostMapping("/add")
    public ResponseDTO addNewTintuc(@RequestBody TinTuc tinTuc) {
        if (this.tinTucService.createOnePage(tinTuc) != null) {
            return new ResponseDTO("Thành công", tinTuc);
        }
        return new ResponseDTO("Thất bại", null);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseDTO updateOneTinTuc(@PathVariable("id") Long id,
            @RequestBody TinTuc tinTuc) {
        if (this.tinTucService.updateTinTuc(tinTuc, id) != null) {
            return new ResponseDTO("Thành công", tinTuc);
        }
        return new ResponseDTO("Cập nhật thất bại", null);
    }

}
