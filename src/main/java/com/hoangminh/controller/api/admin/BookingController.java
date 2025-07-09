package com.hoangminh.controller.api.admin;

import com.hoangminh.dto.BookingDTO;
import com.hoangminh.dto.ResponseDTO;
import com.hoangminh.service.BookingService;
import com.hoangminh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @GetMapping("/getAllBooking")
    public ResponseDTO getAllBooking(@RequestParam(value="trang_thai",required = false) Integer trang_thai,
                                     @RequestParam(value = "trang_thai",required = false) String ten_tour,
                                     @RequestParam(value="pageSize",defaultValue = "10") Integer pageSize,
                                     @RequestParam("pageIndex") Integer pageIndex){


        if(!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập",null);
        }

        Page<BookingDTO> page = this.bookingService.findAllBooking(trang_thai,ten_tour, PageRequest.of(pageIndex,pageSize));

        return new ResponseDTO("Thành công",page.getContent());

    }

    @GetMapping("/{id}")
    public ResponseDTO getOneBooking(@PathVariable("id") Long id) {
        if(!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập",null);
        }
        return new ResponseDTO("Thành công",this.bookingService.getBookingById(id));
    }
    @GetMapping("/detail/{id}")
    public ResponseDTO getOneDetailBooking(@PathVariable("id") Long id) {
        if(!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập",null);
        }
        return new ResponseDTO("Thành công",this.bookingService.getBookingDetailById(id));
    }

    @PutMapping("/approve/{id}")
    public ResponseDTO changeStatus(@PathVariable("id") Long id,@RequestParam("trang_thai") Integer trang_thai) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập",null);
        }

        if(this.bookingService.approveBooking(id,trang_thai)) {
            return new ResponseDTO("Cập nhật thành công",null);
        }
        return new ResponseDTO("Cập nhật thất bại",null);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseDTO deleteBooking(@PathVariable("id") Long id) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập",null);
        }

        if(this.bookingService.deleteBooking(id)) {
            return new ResponseDTO("Xóa thành công",null);
        }

        return new ResponseDTO("Chỉ có thể xóa tour đã hoàn thành và đã hủy",null);
    }

}
