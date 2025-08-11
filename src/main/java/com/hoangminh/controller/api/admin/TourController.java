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
import java.util.List;
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
    public ResponseDTO getAllTour() {
        // Tạm thời bỏ xác thực để test
        // if (!this.userService.checkAdminLogin()) {
        //     return new ResponseDTO("Không có quyền truy cập", null);
        // }
        return new ResponseDTO("Thành công", this.tourService.findAllTourWithStartDate());
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
    public ResponseDTO createTour(
            @RequestParam("ten_tour") String tenTour,
            @RequestParam("so_ngay") Integer soNgay,
            @RequestParam("diem_den") String diemDen,
            @RequestParam("loai_tour") String loaiTour,
            @RequestParam("diem_khoi_hanh") String diemKhoiHanh,
            @RequestParam("gia_tour") BigDecimal giaTour,
            @RequestParam("trang_thai") String trangThai,
            @RequestParam("ngay_khoi_hanh") String ngayKhoiHanh,
            @RequestParam("ngay_ket_thuc") String ngayKetThuc,
            @RequestParam(value = "gioi_thieu_tour", required = false) String gioiThieuTour,
            @RequestParam(value = "noi_dung_tour", required = false) String noiDungTour,
            @RequestParam("anh_dai_dien") MultipartFile anhDaiDien) {
        
        try {
            log.info("Received tour data - ten_tour: {}, so_ngay: {}, diem_den: {}, loai_tour: {}, diem_khoi_hanh: {}, gia_tour: {}, trang_thai: {}, ngay_khoi_hanh: {}, ngay_ket_thuc: {}, gioi_thieu_tour: {}, noi_dung_tour: {}, anh_dai_dien: {}", 
                    tenTour, soNgay, diemDen, loaiTour, diemKhoiHanh, giaTour, trangThai, ngayKhoiHanh, ngayKetThuc, gioiThieuTour, noiDungTour, anhDaiDien.getOriginalFilename());
            
            if (!this.userService.checkAdminLogin()) {
                return new ResponseDTO("Không có quyền truy cập", null);
            }

            // Upload ảnh đại diện
            String uploadDir = "src/main/resources/static/public/img";
            String fileName = UUID.randomUUID().toString() + "_" + anhDaiDien.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, anhDaiDien);
            log.info("Image uploaded successfully: {}", fileName);

            // Tạo TourDTO từ các tham số
            TourDTO tourDTO = new TourDTO();
            tourDTO.setTen_tour(tenTour);
            tourDTO.setSo_ngay(soNgay);
            tourDTO.setDiem_den(diemDen);
            tourDTO.setLoai_tour(loaiTour);
            tourDTO.setDiem_khoi_hanh(diemKhoiHanh);
            tourDTO.setGia_tour(giaTour);
            tourDTO.setTrang_thai(trangThai);
            tourDTO.setNgay_khoi_hanh(ngayKhoiHanh);
            tourDTO.setNgay_ket_thuc(ngayKetThuc);
            tourDTO.setGioi_thieu_tour(gioiThieuTour);
            tourDTO.setNoi_dung_tour(noiDungTour);
            tourDTO.setAnh_dai_dien(fileName);

            Tour tour = this.tourService.addTour(tourDTO);
            if (tour != null) {
                log.info("Tour created successfully with ID: {}", tour.getId());
                return new ResponseDTO("Thêm tour thành công", tour);
            }
            return new ResponseDTO("Thêm tour thất bại", null);
        } catch (Exception e) {
            log.error("Error creating tour: {}", e.getMessage(), e);
            return new ResponseDTO("Lỗi khi thêm tour: " + e.getMessage(), null);
        }
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
    public ResponseDTO updateTour(
            @PathVariable("id") Long id,
            @RequestParam("ten_tour") String tenTour,
            @RequestParam("so_ngay") Integer soNgay,
            @RequestParam("diem_den") String diemDen,
            @RequestParam("loai_tour") String loaiTour,
            @RequestParam("diem_khoi_hanh") String diemKhoiHanh,
            @RequestParam("gia_tour") BigDecimal giaTour,
            @RequestParam("trang_thai") String trangThai,
            @RequestParam("ngay_khoi_hanh") String ngayKhoiHanh,
            @RequestParam("ngay_ket_thuc") String ngayKetThuc,
            @RequestParam(value = "gioi_thieu_tour", required = false) String gioiThieuTour,
            @RequestParam(value = "noi_dung_tour", required = false) String noiDungTour,
            @RequestParam(value = "anh_dai_dien", required = false) MultipartFile anhDaiDien) {
        
        try {
            log.info("Updating tour ID: {} - ten_tour: {}, so_ngay: {}, diem_den: {}, loai_tour: {}, diem_khoi_hanh: {}, gia_tour: {}, trang_thai: {}, ngay_khoi_hanh: {}, ngay_ket_thuc: {}, gioi_thieu_tour: {}, noi_dung_tour: {}, has_new_image: {}", 
                    id, tenTour, soNgay, diemDen, loaiTour, diemKhoiHanh, giaTour, trangThai, ngayKhoiHanh, ngayKetThuc, gioiThieuTour, noiDungTour, (anhDaiDien != null));
            
            if (!this.userService.checkAdminLogin()) {
                return new ResponseDTO("Không có quyền truy cập", null);
            }

            // Tạo TourDTO từ các tham số
            TourDTO tourDTO = new TourDTO();
            tourDTO.setTen_tour(tenTour);
            tourDTO.setSo_ngay(soNgay);
            tourDTO.setDiem_den(diemDen);
            tourDTO.setLoai_tour(loaiTour);
            tourDTO.setDiem_khoi_hanh(diemKhoiHanh);
            tourDTO.setGia_tour(giaTour);
            tourDTO.setTrang_thai(trangThai);
            tourDTO.setNgay_khoi_hanh(ngayKhoiHanh);
            tourDTO.setNgay_ket_thuc(ngayKetThuc);
            tourDTO.setGioi_thieu_tour(gioiThieuTour);
            tourDTO.setNoi_dung_tour(noiDungTour);

            // Xử lý ảnh: nếu có ảnh mới thì upload, nếu không thì giữ ảnh cũ
            if (anhDaiDien != null && !anhDaiDien.isEmpty()) {
                String uploadDir = "src/main/resources/static/public/img";
                String fileName = UUID.randomUUID().toString() + "_" + anhDaiDien.getOriginalFilename();
                FileUploadUtil.saveFile(uploadDir, fileName, anhDaiDien);
                tourDTO.setAnh_dai_dien(fileName);
                log.info("New image uploaded successfully: {}", fileName);
            }

            Tour updateTour = this.tourService.updateTour(tourDTO, id);
            if (updateTour != null) {
                return new ResponseDTO("Cập nhật tour thành công", null);
            }
            return new ResponseDTO("Cập nhật tour thất bại", null);
        } catch (Exception e) {
            log.error("Error updating tour: {}", e.getMessage(), e);
            return new ResponseDTO("Lỗi khi cập nhật tour: " + e.getMessage(), null);
        }
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

    @PutMapping("/updateStatus/{id}")
    public ResponseDTO updateTourStatus(@PathVariable("id") Long id, @RequestBody TourDTO tourDTO) {
        try {
            log.info("Bắt đầu cập nhật trạng thái tour ID: {}, trạng thái mới: {}", id, tourDTO.getTrang_thai());
            
            if (!this.userService.checkAdminLogin()) {
                log.warn("Không có quyền truy cập - admin chưa đăng nhập");
                return new ResponseDTO("Không có quyền truy cập", null);
            }
            
            log.info("Admin đã đăng nhập, tiến hành cập nhật trạng thái");
            Tour tour = this.tourService.updateTourStatus(id, tourDTO.getTrang_thai());
            
            if (tour != null) {
                log.info("Cập nhật trạng thái thành công cho tour ID: {}", id);
                return new ResponseDTO("Cập nhật trạng thái thành công", tour);
            } else {
                log.warn("Không tìm thấy tour với ID: {}", id);
                return new ResponseDTO("Cập nhật trạng thái thất bại - không tìm thấy tour", null);
            }
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật trạng thái tour ID {}: {}", id, e.getMessage(), e);
            return new ResponseDTO("Lỗi: " + e.getMessage(), null);
        }
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
