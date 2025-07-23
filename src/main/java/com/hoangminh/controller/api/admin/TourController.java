package com.hoangminh.controller.api.admin;

import com.hoangminh.dto.ResponseDTO;
import com.hoangminh.dto.TourDTO;
import com.hoangminh.dto.TourStartDTO;
import com.hoangminh.dto.ToutStartAddDTO;
import com.hoangminh.entity.Image;
import com.hoangminh.entity.Tour;
import com.hoangminh.entity.TourStart;
import com.hoangminh.repository.TourStartRepository;
import com.hoangminh.service.ImageService;
import com.hoangminh.service.TourService;
import com.hoangminh.service.UserService;
import com.hoangminh.utilities.DateUtils;
import com.hoangminh.utilities.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/tour")
public class TourController {

    @Autowired
    private TourService tourService;

    @Autowired
    private TourStartRepository tourStartRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @GetMapping("/getAllTour")
    public ResponseDTO getAllTour(@RequestParam(value = "ten_tour", required = false) String ten_tour,
            @RequestParam(value = "gia_tour_from", required = false) BigDecimal gia_tour_from,
            @RequestParam(value = "gia_tour_to", required = false) BigDecimal gia_tour_to,
            @RequestParam(value = "tour_type_id", required = false) Long tour_type_id,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageIndex") Integer pageIndex) {
        if (!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }

        Page<TourDTO> page = this.tourService.findAllTourAdmin(ten_tour, gia_tour_from, gia_tour_to, tour_type_id,
                PageRequest.of(pageIndex, pageSize));

        return new ResponseDTO("Thành công", page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseDTO getOneTour(@PathVariable("id") Long id) {

        if (!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }

        TourDTO tour = this.tourService.findTourById(id);

        if (tour != null) {
            return new ResponseDTO("Thành công", tour);
        }
        return new ResponseDTO("Thất bại", null);
    }

    @PostMapping("/test-up-anh")
    public ResponseDTO testUpAnh(@RequestParam("image") MultipartFile image) {

        if (!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }

        String uploadDir = "src/main/resources/static/public/img";

        try {
            // Lưu ảnh vào thư mục "upload"
            String fileName = UUID.randomUUID().toString() + image.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, image);

            return new ResponseDTO("Thành công", fileName);
        } catch (IOException e) {
            // Xử lý exception
            log.info("Lỗi upload file: {}", e.getMessage());
        }
        return new ResponseDTO("Thêm thất bại", null);
    }

    @PostMapping("/add/image")
    public ResponseDTO createTourImage(@RequestParam("image") MultipartFile image) {

        if (!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }

        String uploadDir = "src/main/resources/static/public/img";

        Tour tour = tourService.findFirstByOrderByIdDesc();

        try {
            // Lưu ảnh vào thư mục "upload"
            String fileName = UUID.randomUUID().toString() + image.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, image);

            // Lưu thông tin của tour vào cơ sở dữ liệu
            tour.setAnh_dai_dien(fileName);
            return new ResponseDTO("Thành công", this.tourService.saveTour(tour));
        } catch (IOException e) {
            // Xử lý exception
            log.info("Lỗi upload file: {}", e.getMessage());
        }
        return new ResponseDTO("Thêm thất bại", null);
    }

    @PostMapping("/add")
    public ResponseDTO createTour(@RequestBody TourDTO tourDTO) {

        if (!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }

        Tour tour = this.tourService.addTour(tourDTO);
        if (tour != null) {
            return new ResponseDTO("Thành công", tour);
        }
        return new ResponseDTO("Thêm thất bại", null);

    }

    @PutMapping("/update/image/{id}")
    public ResponseDTO updateTourImage(@PathVariable("id") Long id, @RequestParam("image") MultipartFile image) {

        if (!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }

        String uploadDir = "src/main/resources/static/public/img";

        TourDTO tourDTO = this.tourService.findTourById(id);
        try {
            // Lưu ảnh vào thư mục "upload"
            String fileName = UUID.randomUUID().toString() + image.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, image);

            // Lưu thông tin của tour vào cơ sở dữ liệu
            tourDTO.setAnh_dai_dien(fileName);
            Tour updateTour = this.tourService.updateTour(tourDTO, id);
            if (updateTour != null) {
                return new ResponseDTO("Thành công", updateTour);
            }

        } catch (IOException e) {
            // Xử lý exception
            log.info("Lỗi upload file: {}", e.getMessage());
        }
        return new ResponseDTO("Update thất bại", null);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO updateTour(@PathVariable("id") Long id, @RequestBody TourDTO tourDTO) {

        if (!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truyabek co quyen truy cap", null);
        }

        Tour updateTour = this.tourService.updateTour(tourDTO, id);
        if (updateTour != null) {
            return new ResponseDTO("Thành công", updateTour);
        }

        return new ResponseDTO("Update thất bại", null);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO deleteTour(@PathVariable("id") Long id) {

        if (!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }

        if (this.tourService.findTourById(id) != null) {
            if (this.tourService.deleteTour(id)) {
                return new ResponseDTO("Xóa thành công", null);
            }

        }
        return new ResponseDTO("Xóa thất bại", null);
    }

    @GetMapping("/getAllImageOfTour/{id}")
    public ResponseDTO getAllImageOfTour(@PathVariable("id") Long id) {

        if (!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }

        return new ResponseDTO("Thành công", this.imageService.findByTourId(id));
    }

    @PostMapping("/add-image/{id}")
    public ResponseDTO addImage(@PathVariable("id") Long id, @RequestParam("image") MultipartFile image) {

        if (!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }

        String uploadDir = "src/main/resources/static/public/img";

        try {
            // Lưu ảnh vào thư mục "upload"
            String fileName = UUID.randomUUID() + image.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, image);

            if (this.tourService.findTourById(id) != null) {

                return new ResponseDTO("Thêm thành công", this.imageService.addToTour(id, fileName));
            }
        } catch (IOException e) {
            // Xử lý exception
            log.info("Lỗi upload file: {}", e.getMessage());
        }

        return new ResponseDTO("Lỗi khi thêm", null);

    }

    @DeleteMapping("/TourImage/delete/{id}")
    public ResponseDTO deleteTourImage(@PathVariable("id") Long id) {

        if (!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }

        this.imageService.deleteById(id);

        return new ResponseDTO("Xóa ảnh thành công", null);
    }

}
