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
    public ResponseDTO getAllPage(@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                  @RequestParam(value = "pageIndex") Integer pageIndex) {
        Page<TinTuc> page = this.tinTucService.getAllPage(PageRequest.of(pageSize,pageIndex));

        return new ResponseDTO("Thành Công",page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseDTO getOnePage(@PathVariable("id") Long id) {

        return new ResponseDTO("Thành công",this.tinTucService.findOnePage(id));

    }

    @PostMapping("/add")
    public ResponseDTO addNewTintuc(@RequestBody TinTuc tinTuc, @RequestParam("image")MultipartFile image) {

        String uploadDir = "/upload";
        try {
            // Lưu ảnh vào thư mục "upload"
            String fileName = image.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, image);

            // Lưu thông tin của tour vào cơ sở dữ liệu
            tinTuc.setHinh_anh(fileName);

            return new ResponseDTO("Thành công",this.tinTucService.createOnePage(tinTuc));

        } catch (IOException e) {
            // Xử lý exception
            log.info("Lỗi upload file: {}",e.getMessage());
        }

        return new ResponseDTO("Thất bại",null);

    }

    @PutMapping(value = "/update/{id}")
    public ResponseDTO updateOneTinTuc(@PathVariable("id") Long id,
                                       @RequestBody TinTuc tinTuc,
                                       @RequestParam("image") MultipartFile image) {

        String uploadDir = "/upload";
        try {
            // Lưu ảnh vào thư mục "upload"
            String fileName = image.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, image);

            // Lưu thông tin của tour vào cơ sở dữ liệu
            tinTuc.setHinh_anh(fileName);

            return new ResponseDTO("Thành công",this.tinTucService.updateTinTuc(tinTuc,id));

        } catch (IOException e) {
            // Xử lý exception
            log.info("Lỗi upload file: {}",e.getMessage());
        }

        return new ResponseDTO("Cập nhật hất bại",null);

    }


}
