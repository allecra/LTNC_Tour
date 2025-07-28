package com.hoangminh.controller.api.user;

import com.hoangminh.dto.ResponseDTO;
import com.hoangminh.dto.ReviewDTO;
import com.hoangminh.dto.FavoriteDTO;
import com.hoangminh.dto.NotificationDTO;
import com.hoangminh.dto.BookingDTO;
import com.hoangminh.dto.UserDTO;
import com.hoangminh.entity.Review;
import com.hoangminh.service.FavoriteService;
import com.hoangminh.service.NotificationService;
import com.hoangminh.service.ReviewService;
import com.hoangminh.service.BookingService;
import com.hoangminh.service.UserService;
import com.hoangminh.service.UserVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @Autowired
    private UserVoucherService userVoucherService;

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

    // == VOUCHER API ==
    @PostMapping("/check-voucher")
    public ResponseEntity<Map<String, Object>> checkVoucher(@RequestBody Map<String, String> request) {
        String voucherCode = request.get("voucherCode");
        
        // Lấy userId từ session (trong thực tế nên dùng JWT)
        Long userId = 2L; // Tạm thời hardcode, cần lấy từ session
        
        if (userVoucherService.isValidVoucher(userId, voucherCode)) {
            com.hoangminh.dto.VoucherDTO voucher = userVoucherService.getVoucherByCode(voucherCode);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "discount", voucher.getDiscount(),
                "dieuKien", voucher.getDieuKien()
            ));
        } else {
            return ResponseEntity.ok(Map.of("success", false));
        }
    }
}