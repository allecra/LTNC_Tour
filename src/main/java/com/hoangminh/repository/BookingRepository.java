package com.hoangminh.repository;

import com.hoangminh.dto.BookingDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hoangminh.dto.BookingDTO;
import com.hoangminh.entity.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	@Query(value = "SELECT new com.hoangminh.dto.BookingDTO(b.id, b.user.id, b.tourStart.tour.id, b.tourStart.tour.ten_tour, b.so_luong_nguoi, b.tourStart.ngay_khoi_hanh, b.tong_tien, b.trang_thai, b.payment_method, b.ghi_chu, b.booking_at, COALESCE(b.voucher.maGiamGia, ''), COALESCE(b.voucher.giaTri, 0), b.tong_tien) "
			+ " FROM Booking b JOIN b.user u JOIN b.tourStart ts JOIN ts.tour t "
			+ " WHERE ( :trang_thai IS NULL OR b.trang_thai = :trang_thai ) "
			+ " AND ( :ten_tour IS NULL OR :ten_tour = '' OR t.ten_tour LIKE %:ten_tour% ) "
			+ " ORDER BY b.id ")
	public Page<BookingDTO> findAllBooking(@Param("trang_thai") String trang_thai, @Param("ten_tour") String ten_tour,
			Pageable pageable);

	@Query(value = "SELECT new com.hoangminh.dto.BookingDTO(b.id, b.user.id, b.tourStart.tour.id, b.tourStart.tour.ten_tour, b.so_luong_nguoi, b.tourStart.ngay_khoi_hanh, b.tong_tien, b.trang_thai, b.payment_method, b.ghi_chu, b.booking_at, COALESCE(b.voucher.maGiamGia, ''), COALESCE(b.voucher.giaTri, 0), b.tong_tien) "
			+ " FROM Booking b WHERE b.user.id = :userId")
	List<BookingDTO> findBookingByUserId(@Param("userId") Long userId);
	
	@Query(value = "SELECT new com.hoangminh.dto.BookingDTO(b.id, b.user.id, b.tourStart.tour.id, b.tourStart.tour.ten_tour, b.so_luong_nguoi, b.tourStart.ngay_khoi_hanh, b.tong_tien, b.trang_thai, b.payment_method, b.ghi_chu, b.booking_at, COALESCE(b.voucher.maGiamGia, ''), COALESCE(b.voucher.giaTri, 0), b.tong_tien) "
			+ " FROM Booking b WHERE b.user.id = :userId AND b.trang_thai NOT IN ('huy', 'hoan_tat')")
	List<BookingDTO> findActiveBookingByUserId(@Param("userId") Long userId);

	@Query(value = "SELECT new com.hoangminh.dto.BookingDTO(b.id, b.user.id, b.tourStart.tour.id, b.tourStart.tour.ten_tour, b.so_luong_nguoi, b.tourStart.ngay_khoi_hanh, b.tong_tien, b.trang_thai, b.payment_method, b.ghi_chu, b.booking_at, COALESCE(b.voucher.maGiamGia, ''), COALESCE(b.voucher.giaTri, 0), b.tong_tien) "
			+ " FROM Booking b WHERE b.id = :id")
	BookingDTO findBookingById(@Param("id") Long id);

	@Query(value = "SELECT new com.hoangminh.dto.BookingDetailDTO(b.id, u.id, u.ho_ten, u.sdt, t.id, t.ten_tour, b.so_luong_nguoi, ts.ngay_khoi_hanh, b.tong_tien, b.trang_thai, b.payment_method, b.ghi_chu, b.booking_at) "
			+ " FROM Booking b JOIN b.user u JOIN b.tourStart ts JOIN ts.tour t WHERE b.id = :id")
	BookingDetailDTO findBookingDetailById(@Param("id") Long id);

    @Query("SELECT SUM(b.tong_tien) FROM Booking b WHERE b.payment_status = 'da_thanh_toan'")
    Double sumTotalRevenue();
    
    // Native query để lấy bảng giao dịch với mã nội dung chuyển khoản
    @Query(value = "SELECT " +
            "ROW_NUMBER() OVER (ORDER BY b.booking_at DESC) AS stt, " +
            "CONCAT('GD', LPAD(b.id, 5, '0'), ' - ', u.ho_ten) AS ma_noi_dung_chuyen_khoan, " +
            "u.ho_ten AS khach_hang, " +
            "b.tong_tien AS so_tien, " +
            "b.booking_at AS thoi_gian, " +
            "CASE " +
            "    WHEN b.trang_thai = 'huy' THEN 'Đã hủy' " +
            "    WHEN b.payment_status = 'da_thanh_toan' THEN 'Đã thanh toán' " +
            "    WHEN b.payment_status = 'chua_thanh_toan' THEN 'Chưa thanh toán' " +
            "    ELSE b.payment_status " +
            "END AS trang_thai " +
            "FROM booking b " +
            "JOIN user u ON b.user_id = u.id " +
            "ORDER BY b.booking_at DESC", nativeQuery = true)
    List<Object[]> findTransactionTable();
    
    // Native query để lấy bảng giao dịch với filter
    @Query(value = "SELECT " +
            "ROW_NUMBER() OVER (ORDER BY b.booking_at DESC) AS stt, " +
            "CONCAT('GD', LPAD(b.id, 5, '0'), ' - ', u.ho_ten) AS ma_noi_dung_chuyen_khoan, " +
            "u.ho_ten AS khach_hang, " +
            "b.tong_tien AS so_tien, " +
            "b.booking_at AS thoi_gian, " +
            "CASE " +
            "    WHEN b.trang_thai = 'huy' THEN 'Đã hủy' " +
            "    WHEN b.payment_status = 'da_thanh_toan' THEN 'Đã thanh toán' " +
            "    WHEN b.payment_status = 'chua_thanh_toan' THEN 'Chưa thanh toán' " +
            "    ELSE b.payment_status " +
            "END AS trang_thai " +
            "FROM booking b " +
            "JOIN user u ON b.user_id = u.id " +
            "WHERE (:khachHang IS NULL OR u.ho_ten LIKE %:khachHang%) " +
            "AND (:trangThai IS NULL OR " +
            "    CASE " +
            "        WHEN b.trang_thai = 'huy' THEN 'Đã hủy' " +
            "        WHEN b.payment_status = 'da_thanh_toan' THEN 'Đã thanh toán' " +
            "        WHEN b.payment_status = 'chua_thanh_toan' THEN 'Chưa thanh toán' " +
            "        ELSE b.payment_status " +
            "    END = :trangThai) " +
            "AND (:maNoiDung IS NULL OR CONCAT('GD', LPAD(b.id, 5, '0'), ' - ', u.ho_ten) LIKE %:maNoiDung%) " +
            "ORDER BY b.booking_at DESC", nativeQuery = true)
    List<Object[]> findTransactionTableWithFilter(
            @Param("khachHang") String khachHang,
            @Param("trangThai") String trangThai,
            @Param("maNoiDung") String maNoiDung);
    
    // Native query để lấy bảng booking chi tiết
    @Query(value = "SELECT " +
            "b.id AS stt, " +
            "CONCAT('GD', LPAD(b.id, 5, '0'), ' - ', u.ho_ten) AS ma_noi_dung_chuyen_khoan, " +
            "u.ho_ten AS nguoi_dung, " +
            "t.ten_tour AS tour, " +
            "b.so_luong_nguoi, " +
            "b.tong_tien AS tong_tien_ban_dau, " +
            "COALESCE(v.ma_giam_gia, '') AS voucher, " +
            "CASE " +
            "  WHEN v.ma_giam_gia IS NOT NULL AND v.gia_tri <= 100 THEN b.tong_tien * (1 - v.gia_tri / 100) " +
            "  WHEN v.ma_giam_gia IS NOT NULL AND v.gia_tri > 100 THEN GREATEST(b.tong_tien - v.gia_tri, 0) " +
            "  ELSE b.tong_tien " +
            "END AS thanh_tien, " +
            "ts.ngay_khoi_hanh, " +
            "b.ghi_chu, " +
            "b.booking_at AS ngay_dat, " +
            "b.trang_thai " +
            "FROM booking b " +
            "JOIN user u ON b.user_id = u.id " +
            "JOIN tour_start ts ON b.tour_start_id = ts.id " +
            "JOIN tour t ON ts.tour_id = t.id " +
            "LEFT JOIN voucher v ON b.voucher_id = v.id " +
            "ORDER BY b.booking_at DESC", nativeQuery = true)
    List<Object[]> findBookingDetailTable();
    
    // Native query để lấy bảng booking chi tiết với filter
    @Query(value = "SELECT " +
            "b.id AS stt, " +
            "CONCAT('GD', LPAD(b.id, 5, '0'), ' - ', u.ho_ten) AS ma_noi_dung_chuyen_khoan, " +
            "u.ho_ten AS nguoi_dung, " +
            "t.ten_tour AS tour, " +
            "b.so_luong_nguoi, " +
            "b.tong_tien AS tong_tien_ban_dau, " +
            "COALESCE(v.ma_giam_gia, '') AS voucher, " +
            "CASE " +
            "  WHEN v.ma_giam_gia IS NOT NULL AND v.gia_tri <= 100 THEN b.tong_tien * (1 - v.gia_tri / 100) " +
            "  WHEN v.ma_giam_gia IS NOT NULL AND v.gia_tri > 100 THEN GREATEST(b.tong_tien - v.gia_tri, 0) " +
            "  ELSE b.tong_tien " +
            "END AS thanh_tien, " +
            "ts.ngay_khoi_hanh, " +
            "b.ghi_chu, " +
            "b.booking_at AS ngay_dat, " +
            "b.trang_thai " +
            "FROM booking b " +
            "JOIN user u ON b.user_id = u.id " +
            "JOIN tour_start ts ON b.tour_start_id = ts.id " +
            "JOIN tour t ON ts.tour_id = t.id " +
            "LEFT JOIN voucher v ON b.voucher_id = v.id " +
            "WHERE (:nguoiDung IS NULL OR u.ho_ten LIKE %:nguoiDung%) " +
            "AND (:tour IS NULL OR t.ten_tour LIKE %:tour%) " +
            "AND (:trangThai IS NULL OR b.trang_thai = :trangThai) " +
            "AND (:maNoiDung IS NULL OR CONCAT('GD', LPAD(b.id, 5, '0'), ' - ', u.ho_ten) LIKE %:maNoiDung%) " +
            "ORDER BY b.booking_at DESC", nativeQuery = true)
    List<Object[]> findBookingDetailTableWithFilter(
            @Param("nguoiDung") String nguoiDung,
            @Param("tour") String tour,
            @Param("trangThai") String trangThai,
            @Param("maNoiDung") String maNoiDung);

    // Query để lấy thống kê booking đã hoàn tất theo tháng
    @Query(value = "SELECT " +
            "MONTH(ts.ngay_khoi_hanh) as month, " +
            "COUNT(b.id) as count " +
            "FROM booking b " +
            "JOIN tour_start ts ON b.tour_start_id = ts.id " +
            "WHERE b.trang_thai = 'hoan_tat' " +
            "GROUP BY MONTH(ts.ngay_khoi_hanh) " +
            "ORDER BY month", nativeQuery = true)
    List<Object[]> findCompletedBookingsByMonth();
    
    // Query để lấy thống kê booking đã hoàn tất theo mùa
    @Query(value = "SELECT " +
            "s.ten_mua AS seasonName, " +
            "COUNT(b.id) AS totalBookings " +
            "FROM booking b " +
            "JOIN tour_start ts ON b.tour_start_id = ts.id " +
            "JOIN season s ON ts.season_id = s.id " +
            "WHERE b.trang_thai = 'hoan_tat' " +
            "GROUP BY s.id, s.ten_mua " +
            "ORDER BY s.id", nativeQuery = true)
    List<Object[]> findCompletedBookingsBySeason();
}
