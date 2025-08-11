package com.hoangminh.controller.api.user;

import com.hoangminh.dto.ResponseDTO;
import com.hoangminh.dto.ReviewDTO;
import com.hoangminh.dto.FavoriteDTO;
import com.hoangminh.dto.NotificationDTO;
import com.hoangminh.dto.BookingDTO;
import com.hoangminh.dto.UserDTO;
import com.hoangminh.dto.VoucherValidationRequest;
import com.hoangminh.dto.VoucherCalculationRequest;
import com.hoangminh.dto.VoucherCalculationResponse;
import com.hoangminh.dto.BookingRequestDTO;
import java.math.BigDecimal;
import com.hoangminh.entity.Review;
import com.hoangminh.service.FavoriteService;
import com.hoangminh.service.NotificationService;
import com.hoangminh.service.ReviewService;
import com.hoangminh.service.BookingService;
import com.hoangminh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import com.hoangminh.entity.User;

@RestController
@RequestMapping("/api/user")
public class UserApiController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    // == REVIEW API ==
    @PostMapping("/reviews")
    public ResponseEntity<ResponseDTO> addReview(@RequestBody ReviewDTO reviewDTO) {
        // NOTE: Trong thực tế, bạn nên lấy userId từ thông tin xác thực (ví dụ: JWT)
        // thay vì tin tưởng vào userId trong request body.
        Review newReview = reviewService.addReview(reviewDTO);
        if (newReview != null) {
            return new ResponseEntity<>(new ResponseDTO("Thêm đánh giá thành công", newReview), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ResponseDTO("Thêm đánh giá thất bại", null), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{userId}/reviews")
    public ResponseEntity<ResponseDTO> getReviewsByUserId(@PathVariable Long userId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(new ResponseDTO("Lấy danh sách đánh giá thành công", reviews));
    }

    // == FAVORITE (WISHLIST) API ==
    @PostMapping("/{userId}/wishlist/{tourId}")
    public ResponseEntity<ResponseDTO> addToWishlist(@PathVariable Long userId, @PathVariable Long tourId) {
        FavoriteDTO favoriteDTO = favoriteService.addToWishlist(userId, tourId);
        if (favoriteDTO != null) {
            return new ResponseEntity<>(new ResponseDTO("Đã thêm vào danh sách yêu thích", favoriteDTO),
                    HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ResponseDTO("Tour đã có trong danh sách hoặc có lỗi xảy ra", null),
                HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/wishlist/{favoriteId}")
    public ResponseEntity<ResponseDTO> removeFromWishlist(@PathVariable Long favoriteId) {
        favoriteService.removeFromWishlist(favoriteId);
        return ResponseEntity.ok(new ResponseDTO("Đã xóa khỏi danh sách yêu thích", null));
    }

    @GetMapping("/{userId}/wishlist")
    public ResponseEntity<ResponseDTO> getWishlist(@PathVariable Long userId) {
        List<FavoriteDTO> wishlist = favoriteService.getWishlistByUserId(userId);
        return ResponseEntity.ok(new ResponseDTO("Lấy danh sách yêu thích thành công", wishlist));
    }

    // == NOTIFICATION API ==
    @GetMapping("/{userId}/notifications")
    public ResponseEntity<ResponseDTO> getNotifications(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(new ResponseDTO("Lấy danh sách thông báo thành công", notifications));
    }

    @PatchMapping("/notifications/{notificationId}/read")
    public ResponseEntity<ResponseDTO> markNotificationAsRead(@PathVariable Long notificationId) {
        NotificationDTO notificationDTO = notificationService.markAsRead(notificationId);
        if (notificationDTO != null) {
            return ResponseEntity.ok(new ResponseDTO("Đánh dấu đã đọc thành công", notificationDTO));
        }
        return new ResponseEntity<>(new ResponseDTO("Không tìm thấy thông báo", null), HttpStatus.NOT_FOUND);
    }

    // == USER & BOOKING API ==
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDTO> getUserInfo(@PathVariable Long userId) {
        UserDTO userDTO = userService.findUserDTOById(userId);
        if (userDTO != null) {
            return ResponseEntity.ok(new ResponseDTO("Lấy thông tin người dùng thành công", userDTO));
        }
        return new ResponseEntity<>(new ResponseDTO("Không tìm thấy người dùng", null), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{userId}/bookings")
    public ResponseEntity<ResponseDTO> getBookingHistory(@PathVariable Long userId) {
        List<BookingDTO> bookings = bookingService.findBookingByUserId(userId);
        return ResponseEntity.ok(new ResponseDTO("Lấy lịch sử đặt tour thành công", bookings));
    }
    
    @PostMapping("/{userId}/bookings")
    public ResponseEntity<ResponseDTO> createBooking(@PathVariable Long userId, @RequestBody BookingRequestDTO bookingRequest) {
        try {
            // Set userId từ path variable
            bookingRequest.setUserId(userId);
            
            // Validation
            if (!bookingRequest.isValid()) {
                return new ResponseEntity<>(new ResponseDTO("Dữ liệu booking không hợp lệ", null), HttpStatus.BAD_REQUEST);
            }
            
            // Convert BookingRequestDTO to BookingDTO
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setUser_id(bookingRequest.getUserId());
            bookingDTO.setTour_id(bookingRequest.getTourId());
            bookingDTO.setSo_luong_nguoi(bookingRequest.getSoLuongNguoi());
            bookingDTO.setPayment_method(bookingRequest.getPaymentMethod());
            bookingDTO.setGhi_chu(bookingRequest.getGhiChu());
            bookingDTO.setVoucherCode(bookingRequest.getVoucherCode());
            
            boolean result = bookingService.addNewBooking(bookingDTO);
            if (result) {
                return new ResponseEntity<>(new ResponseDTO("Tạo booking thành công", null), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ResponseDTO("Tạo booking thất bại", null), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO("Lỗi khi tạo booking: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/guides")
    public ResponseEntity<ResponseDTO> getAllGuides() {
        try {
            // Sử dụng repository method để lấy trực tiếp guides (role_id = 3)
            List<User> guides = userService.getAllGuides();
            
            // Convert sang DTO
            List<UserDTO> guideDTOs = guides.stream()
                .map(guide -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(guide.getId());
                    dto.setHo_ten(guide.getHo_ten());
                    dto.setEmail(guide.getEmail());
                    dto.setSdt(guide.getSdt());
                    dto.setGioi_tinh(guide.getGioi_tinh());
                    dto.setDia_chi(guide.getDia_chi());
                    return dto;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(new ResponseDTO("Lấy danh sách hướng dẫn viên thành công", guideDTOs));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO("Lỗi khi lấy danh sách hướng dẫn viên: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // == VOUCHER API ==
    @PostMapping("/voucher/validate")
    public ResponseEntity<ResponseDTO> validateVoucher(@RequestBody VoucherValidationRequest request) {
        try {
            if (request.getVoucherCode() == null || request.getVoucherCode().trim().isEmpty()) {
                return new ResponseEntity<>(new ResponseDTO("Mã voucher không được để trống", null), HttpStatus.BAD_REQUEST);
            }
            
            boolean isValid = bookingService.isValidVoucher(request.getVoucherCode().trim());
            if (isValid) {
                return ResponseEntity.ok(new ResponseDTO("Voucher hợp lệ", true));
            } else {
                return new ResponseEntity<>(new ResponseDTO("Voucher không hợp lệ hoặc đã hết hạn", false), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO("Lỗi khi kiểm tra voucher: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/voucher/calculate")
    public ResponseEntity<ResponseDTO> calculatePriceWithVoucher(@RequestBody VoucherCalculationRequest request) {
        try {
            if (request.getOriginalPrice() == null || request.getVoucherCode() == null || request.getVoucherCode().trim().isEmpty()) {
                return new ResponseEntity<>(new ResponseDTO("Giá gốc và mã voucher không được để trống", null), HttpStatus.BAD_REQUEST);
            }
            
            BigDecimal finalPrice = bookingService.calculatePriceWithVoucher(request.getOriginalPrice(), request.getVoucherCode().trim());
            BigDecimal discountAmount = request.getOriginalPrice().subtract(finalPrice);
            
            VoucherCalculationResponse response = new VoucherCalculationResponse();
            response.setOriginalPrice(request.getOriginalPrice());
            response.setFinalPrice(finalPrice);
            response.setDiscountAmount(discountAmount);
            response.setVoucherCode(request.getVoucherCode());
            
            return ResponseEntity.ok(new ResponseDTO("Tính toán giá thành công", response));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO("Lỗi khi tính toán giá: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}