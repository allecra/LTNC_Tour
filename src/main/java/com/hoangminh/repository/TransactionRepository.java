package com.hoangminh.repository;

import com.hoangminh.dto.BookingDTOWithTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hoangminh.entity.Booking;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Booking, Long> {

    // Query để lấy booking với mã nội dung chuyển khoản cho user
    @Query(value = "SELECT new com.hoangminh.dto.BookingDTOWithTransaction(b.id, CONCAT('GD', LPAD(CAST(b.id AS string), 5, '0'), ' - ', b.user.ho_ten), b.user.id, b.tourStart.tour.id, b.tourStart.tour.ten_tour, b.so_luong_nguoi, b.tourStart.ngay_khoi_hanh, b.tong_tien, b.trang_thai, b.payment_method, b.ghi_chu, b.booking_at) "
            + " FROM Booking b WHERE b.user.id = :userId")
    List<BookingDTOWithTransaction> findBookingWithTransactionByUserId(@Param("userId") Long userId);
    
    // Query để lấy booking với mã nội dung chuyển khoản cho user (active bookings)
    @Query(value = "SELECT new com.hoangminh.dto.BookingDTOWithTransaction(b.id, CONCAT('GD', LPAD(CAST(b.id AS string), 5, '0'), ' - ', b.user.ho_ten), b.user.id, b.tourStart.tour.id, b.tourStart.tour.ten_tour, b.so_luong_nguoi, b.tourStart.ngay_khoi_hanh, b.tong_tien, b.trang_thai, b.payment_method, b.ghi_chu, b.booking_at) "
            + " FROM Booking b WHERE b.user.id = :userId AND b.trang_thai NOT IN ('huy', 'hoan_tat')")
    List<BookingDTOWithTransaction> findActiveBookingWithTransactionByUserId(@Param("userId") Long userId);

    // Query để lấy booking với mã nội dung chuyển khoản theo ID
    @Query(value = "SELECT new com.hoangminh.dto.BookingDTOWithTransaction(b.id, CONCAT('GD', LPAD(CAST(b.id AS string), 5, '0'), ' - ', b.user.ho_ten), b.user.id, b.tourStart.tour.id, b.tourStart.tour.ten_tour, b.so_luong_nguoi, b.tourStart.ngay_khoi_hanh, b.tong_tien, b.trang_thai, b.payment_method, b.ghi_chu, b.booking_at) "
            + " FROM Booking b WHERE b.id = :id")
    BookingDTOWithTransaction findBookingWithTransactionById(@Param("id") Long id);

    // Query để lấy tất cả booking với mã nội dung chuyển khoản (có filter)
    @Query(value = "SELECT new com.hoangminh.dto.BookingDTOWithTransaction(b.id, CONCAT('GD', LPAD(CAST(b.id AS string), 5, '0'), ' - ', b.user.ho_ten), b.user.id, b.tourStart.tour.id, b.tourStart.tour.ten_tour, b.so_luong_nguoi, b.tourStart.ngay_khoi_hanh, b.tong_tien, b.trang_thai, b.payment_method, b.ghi_chu, b.booking_at) "
            + " FROM Booking b JOIN b.user u JOIN b.tourStart ts JOIN ts.tour t "
            + " WHERE ( :trang_thai IS NULL OR b.trang_thai = :trang_thai ) "
            + " AND ( :ten_tour IS NULL OR :ten_tour = '' OR t.ten_tour LIKE %:ten_tour% ) "
            + " ORDER BY b.id ")
    List<BookingDTOWithTransaction> findAllBookingWithTransaction(@Param("trang_thai") String trang_thai, @Param("ten_tour") String ten_tour);
}
