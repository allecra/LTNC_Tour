package com.hoangminh.controller.api.admin;

import com.hoangminh.dto.ResponseDTO;
import com.hoangminh.service.PaymentService;
import com.hoangminh.service.BookingService;
import com.hoangminh.service.UserService;
import com.hoangminh.service.TourService;
import com.hoangminh.service.TourStartService;
import com.hoangminh.entity.User;
import com.hoangminh.entity.Tour;
import com.hoangminh.entity.TourStart;
import com.hoangminh.entity.Booking;
import com.hoangminh.repository.UserRepository;
import com.hoangminh.repository.TourRepository;
import com.hoangminh.repository.TourStartRepository;
import com.hoangminh.repository.BookingRepository;
import com.hoangminh.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/transaction")
public class TransactionHistoryController {
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private TourService tourService;

    @Autowired
    private TourStartService tourStartService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private TourStartRepository tourStartRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoleRepository roleRepository;

    // API cũ - lấy thông tin payment
    @GetMapping("/payment/getAll")
    public ResponseDTO getAllPayments() {
        if (!userService.checkAdminLogin()) return new ResponseDTO("Không có quyền truy cập", null);
        return new ResponseDTO("Thành công", paymentService.getAll());
    }
    
    // API lấy thống kê giao dịch
    @GetMapping("/statistics")
    public ResponseDTO getTransactionStatistics() {
        if (!userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }
        
        // Lấy tất cả giao dịch
        List<Object[]> allTransactions = bookingService.getTransactionTable();
        
        // Tính toán thống kê
        long totalTransactions = allTransactions.size();
        long completedTransactions = allTransactions.stream()
                .filter(t -> "da_thanh_toan".equals(t[6])) // payment_status ở index 6
                .count();
        long pendingTransactions = allTransactions.stream()
                .filter(t -> "chua_thanh_toan".equals(t[6]))
                .count();
        
        // Tổng doanh thu từ các giao dịch đã hoàn thành
        double totalRevenue = allTransactions.stream()
                .filter(t -> "da_thanh_toan".equals(t[6]))
                .mapToDouble(t -> ((Number) t[3]).doubleValue()) // tong_tien ở index 3
                .sum();
        
        // Tạo object thống kê
        TransactionStatistics stats = new TransactionStatistics();
        stats.setTotalTransactions(totalTransactions);
        stats.setCompletedTransactions(completedTransactions);
        stats.setPendingTransactions(pendingTransactions);
        stats.setCancelledTransactions(0); // Không có trạng thái hủy trong payment_status
        stats.setTotalRevenue(totalRevenue);
        
        return new ResponseDTO("Lấy thống kê giao dịch thành công", stats);
    }
    
    // API lấy bảng giao dịch
    @GetMapping("/table")
    public ResponseDTO getTransactionTable(
            @RequestParam(value = "khachHang", required = false) String khachHang,
            @RequestParam(value = "trangThai", required = false) String trangThai,
            @RequestParam(value = "phuongThuc", required = false) String phuongThuc) {
        
        if (!userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }
        
        List<Object[]> transactions;
        if (khachHang != null || trangThai != null || phuongThuc != null) {
            transactions = bookingService.getTransactionTableWithFilter(khachHang, trangThai, phuongThuc);
        } else {
            transactions = bookingService.getTransactionTable();
        }
        
        return new ResponseDTO("Lấy bảng giao dịch thành công", transactions);
    }
    
    // API lấy bảng booking chi tiết
    @GetMapping("/booking-table")
    public ResponseDTO getBookingDetailTable(
            @RequestParam(value = "nguoiDung", required = false) String nguoiDung,
            @RequestParam(value = "tour", required = false) String tour,
            @RequestParam(value = "trangThai", required = false) String trangThai,
            @RequestParam(value = "phuongThuc", required = false) String phuongThuc) {
        
        if (!userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }
        
        List<Object[]> bookings;
        if (nguoiDung != null || tour != null || trangThai != null || phuongThuc != null) {
            bookings = bookingService.getBookingDetailTableWithFilter(nguoiDung, tour, trangThai, phuongThuc);
        } else {
            bookings = bookingService.getBookingDetailTable();
        }
        
        return new ResponseDTO("Lấy bảng booking chi tiết thành công", bookings);
    }
    
    // API lấy thống kê booking
    @GetMapping("/booking-statistics")
    public ResponseDTO getBookingStatistics() {
        if (!userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }
        
        // Lấy tất cả booking
        List<Object[]> allBookings = bookingService.getBookingDetailTable();
        
        // Tính toán thống kê
        long totalBookings = allBookings.size();
        long confirmedBookings = allBookings.stream()
                .filter(b -> "da_xac_nhan".equals(b[10])) // trang_thai ở index 10
                .count();
        long pendingBookings = allBookings.stream()
                .filter(b -> "cho_xac_nhan".equals(b[10]))
                .count();
        long cancelledBookings = allBookings.stream()
                .filter(b -> "huy".equals(b[10]))
                .count();
        
        // Tổng doanh thu từ các booking đã xác nhận
        double totalRevenue = allBookings.stream()
                .filter(b -> "da_xac_nhan".equals(b[10]))
                .mapToDouble(b -> ((Number) b[5]).doubleValue()) // tong_tien ở index 5
                .sum();
        
        // Tạo object thống kê
        BookingStatistics stats = new BookingStatistics();
        stats.setTotalBookings(totalBookings);
        stats.setConfirmedBookings(confirmedBookings);
        stats.setPendingBookings(pendingBookings);
        stats.setCancelledBookings(cancelledBookings);
        stats.setTotalRevenue(totalRevenue);
        
        return new ResponseDTO("Lấy thống kê booking thành công", stats);
    }
    
    // Inner class để chứa thống kê
    public static class TransactionStatistics {
        private long totalTransactions;
        private long completedTransactions;
        private long pendingTransactions;
        private long cancelledTransactions;
        private double totalRevenue;
        
        // Getters and Setters
        public long getTotalTransactions() { return totalTransactions; }
        public void setTotalTransactions(long totalTransactions) { this.totalTransactions = totalTransactions; }
        
        public long getCompletedTransactions() { return completedTransactions; }
        public void setCompletedTransactions(long completedTransactions) { this.completedTransactions = completedTransactions; }
        
        public long getPendingTransactions() { return pendingTransactions; }
        public void setPendingTransactions(long pendingTransactions) { this.pendingTransactions = pendingTransactions; }
        
        public long getCancelledTransactions() { return cancelledTransactions; }
        public void setCancelledTransactions(long cancelledTransactions) { this.cancelledTransactions = cancelledTransactions; }
        
        public double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
    }
    
    // Inner class để chứa thống kê booking
    public static class BookingStatistics {
        private long totalBookings;
        private long confirmedBookings;
        private long pendingBookings;
        private long cancelledBookings;
        private double totalRevenue;
        
        // Getters and Setters
        public long getTotalBookings() { return totalBookings; }
        public void setTotalBookings(long totalBookings) { this.totalBookings = totalBookings; }
        
        public long getConfirmedBookings() { return confirmedBookings; }
        public void setConfirmedBookings(long confirmedBookings) { this.confirmedBookings = confirmedBookings; }
        
        public long getPendingBookings() { return pendingBookings; }
        public void setPendingBookings(long pendingBookings) { this.pendingBookings = pendingBookings; }
        
        public long getCancelledBookings() { return cancelledBookings; }
        public void setCancelledBookings(long cancelledBookings) { this.cancelledBookings = cancelledBookings; }
        
        public double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
    }

    // API lấy danh sách users cho dropdown
    @GetMapping("/users")
    public ResponseDTO getUsers() {
        if (!userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }
        return new ResponseDTO("Lấy danh sách users thành công", userService.getAllUsers());
    }

    // API lấy danh sách tours cho dropdown
    @GetMapping("/tours")
    public ResponseDTO getTours() {
        if (!userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }
        
        try {
            List<Tour> tours = tourService.getAllTours();
            
            // Chỉ trả về thông tin cần thiết cho dropdown
            List<Map<String, Object>> tourList = tours.stream()
                .map(tour -> {
                    Map<String, Object> tourMap = new HashMap<>();
                    tourMap.put("id", tour.getId());
                    tourMap.put("ten_tour", tour.getTen_tour());
                    tourMap.put("gia_tour", tour.getGia_tour());
                    return tourMap;
                })
                .collect(Collectors.toList());
            
            return new ResponseDTO("Lấy danh sách tours thành công", tourList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDTO("Lỗi khi lấy danh sách tours: " + e.getMessage(), null);
        }
    }

    // API lấy danh sách tour starts theo tour
    @GetMapping("/tour-starts/{tourId}")
    public ResponseDTO getTourStartsByTour(@PathVariable Long tourId) {
        if (!userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }
        return new ResponseDTO("Lấy danh sách tour starts thành công", tourStartService.getTourStartsByTourId(tourId));
    }

    // API thêm booking với thông tin tự nhập
    @PostMapping(value = "/add-booking", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseDTO addBookingWithCustomInfo(@RequestBody BookingCustomDTO bookingDTO) {
        if (!userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }
        
        try {
            // Kiểm tra dữ liệu đầu vào
            if (bookingDTO.getNguoiDung() == null || bookingDTO.getNguoiDung().trim().isEmpty()) {
                return new ResponseDTO("Tên người dùng không được để trống", null);
            }
            if (bookingDTO.getTenTour() == null || bookingDTO.getTenTour().trim().isEmpty()) {
                return new ResponseDTO("Tên tour không được để trống", null);
            }
            if (bookingDTO.getNgayKhoiHanh() == null || bookingDTO.getNgayKhoiHanh().trim().isEmpty()) {
                return new ResponseDTO("Ngày khởi hành không được để trống", null);
            }
            if (bookingDTO.getSoLuongNguoi() == null || bookingDTO.getSoLuongNguoi() <= 0) {
                return new ResponseDTO("Số lượng người phải lớn hơn 0", null);
            }
            if (bookingDTO.getGiaTour() == null || bookingDTO.getGiaTour().compareTo(BigDecimal.ZERO) <= 0) {
                return new ResponseDTO("Giá tour phải lớn hơn 0", null);
            }
            
            // Tạo user mới nếu chưa tồn tại
            User user = userService.findUserByUsername(bookingDTO.getNguoiDung());
            if (user == null) {
                user = new User();
                user.setUsername(bookingDTO.getNguoiDung());
                user.setHo_ten(bookingDTO.getNguoiDung());
                user.setEmail(bookingDTO.getNguoiDung() + "@example.com");
                user.setSdt("0123456789"); // Số điện thoại mặc định
                user.setDia_chi("Chưa cập nhật"); // Địa chỉ mặc định
                user.setGioi_tinh("Nam"); // Giới tính mặc định
                // Mã hóa mật khẩu trước khi lưu
                user.setPassword(userService.encodePassword("123456")); // Mật khẩu mặc định đã mã hóa
                user.setRole(roleRepository.findById(2L).orElse(null)); // Role khách hàng
                if (user.getRole() == null) {
                    return new ResponseDTO("Không tìm thấy role khách hàng", null);
                }
                user = userRepository.save(user);
            }
            
            // Tìm tour theo tên
            Tour tour = tourService.findByTenTour(bookingDTO.getTenTour());
            if (tour == null) {
                return new ResponseDTO("Không tìm thấy tour: " + bookingDTO.getTenTour(), null);
            }
            
            // Tạo tour start
            TourStart tourStart = new TourStart();
            tourStart.setTour(tour);
            tourStart.setNgay_khoi_hanh(java.sql.Date.valueOf(bookingDTO.getNgayKhoiHanh()));
            tourStart.setGia_rieng(bookingDTO.getGiaTour());
            tourStart.setSo_cho_con_lai(20);
            tourStart = tourStartRepository.save(tourStart);
            
            // Tạo booking
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setTourStart(tourStart);
            booking.setSo_luong_nguoi(bookingDTO.getSoLuongNguoi());
            booking.setTong_tien(bookingDTO.getTongTien());
            booking.setPayment_method("Chuyển khoản"); // Luôn cố định là "Chuyển khoản"
            booking.setPayment_status(bookingDTO.getPaymentStatus() != null ? bookingDTO.getPaymentStatus() : "chua_thanh_toan");
            booking.setTrang_thai(bookingDTO.getTrangThai() != null ? bookingDTO.getTrangThai() : "cho_xac_nhan");
            booking.setGhi_chu(bookingDTO.getGhiChu() != null ? bookingDTO.getGhiChu() : "");
            booking.setBooking_at(new java.util.Date());
            
            booking = bookingRepository.save(booking);
            
            // Trả về response đơn giản thay vì toàn bộ object booking
            return new ResponseDTO("Thêm booking thành công", booking.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDTO("Lỗi khi thêm booking: " + e.getMessage(), null);
        }
    }
    
    // Inner class để chứa dữ liệu booking tự nhập
    public static class BookingCustomDTO {
        private String nguoiDung;
        private String tenTour;
        private String ngayKhoiHanh;
        private Integer soLuongNguoi;
        private BigDecimal giaTour;
        private BigDecimal tongTien;
        private String paymentMethod;
        private String paymentStatus;
        private String trangThai;
        private String ghiChu;
        
        // Getters and Setters
        public String getNguoiDung() { return nguoiDung; }
        public void setNguoiDung(String nguoiDung) { this.nguoiDung = nguoiDung; }
        
        public String getTenTour() { return tenTour; }
        public void setTenTour(String tenTour) { this.tenTour = tenTour; }
        
        public String getNgayKhoiHanh() { return ngayKhoiHanh; }
        public void setNgayKhoiHanh(String ngayKhoiHanh) { this.ngayKhoiHanh = ngayKhoiHanh; }
        
        public Integer getSoLuongNguoi() { return soLuongNguoi; }
        public void setSoLuongNguoi(Integer soLuongNguoi) { this.soLuongNguoi = soLuongNguoi; }
        
        public BigDecimal getGiaTour() { return giaTour; }
        public void setGiaTour(BigDecimal giaTour) { this.giaTour = giaTour; }
        
        public BigDecimal getTongTien() { return tongTien; }
        public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }
        
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
        
        public String getPaymentStatus() { return paymentStatus; }
        public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
        
        public String getTrangThai() { return trangThai; }
        public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
        
        public String getGhiChu() { return ghiChu; }
        public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    }
} 