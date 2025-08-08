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

	@Query(value = "SELECT new com.hoangminh.dto.BookingDTO(b.id, b.user.id, b.tourStart.tour.id, b.tourStart.tour.ten_tour, b.so_luong_nguoi, b.tourStart.ngay_khoi_hanh, b.tong_tien, b.trang_thai, b.payment_method, b.ghi_chu, b.booking_at) "
			+ " FROM Booking b JOIN b.user u JOIN b.tourStart ts JOIN ts.tour t "
			+ " WHERE ( :trang_thai IS NULL OR b.trang_thai = :trang_thai ) "
			+ " AND ( :ten_tour IS NULL OR :ten_tour = '' OR t.ten_tour LIKE %:ten_tour% ) "
			+ " ORDER BY b.id ")
	public Page<BookingDTO> findAllBooking(@Param("trang_thai") String trang_thai, @Param("ten_tour") String ten_tour,
			Pageable pageable);

	@Query(value = "SELECT new com.hoangminh.dto.BookingDTO(b.id, b.user.id, b.tourStart.tour.id, b.tourStart.tour.ten_tour, b.so_luong_nguoi, b.tourStart.ngay_khoi_hanh, b.tong_tien, b.trang_thai, b.payment_method, b.ghi_chu, b.booking_at) "
			+ " FROM Booking b WHERE b.user.id = :userId")
	List<BookingDTO> findBookingByUserId(@Param("userId") Long userId);
	
	@Query(value = "SELECT new com.hoangminh.dto.BookingDTO(b.id, b.user.id, b.tourStart.tour.id, b.tourStart.tour.ten_tour, b.so_luong_nguoi, b.tourStart.ngay_khoi_hanh, b.tong_tien, b.trang_thai, b.payment_method, b.ghi_chu, b.booking_at) "
			+ " FROM Booking b WHERE b.user.id = :userId AND b.trang_thai NOT IN ('huy', 'hoan_tat')")
	List<BookingDTO> findActiveBookingByUserId(@Param("userId") Long userId);

	@Query(value = "SELECT new com.hoangminh.dto.BookingDTO(b.id, b.user.id, b.tourStart.tour.id, b.tourStart.tour.ten_tour, b.so_luong_nguoi, b.tourStart.ngay_khoi_hanh, b.tong_tien, b.trang_thai, b.payment_method, b.ghi_chu, b.booking_at) "
			+ " FROM Booking b WHERE b.id = :id")
	BookingDTO findBookingById(@Param("id") Long id);

	@Query(value = "SELECT new com.hoangminh.dto.BookingDetailDTO(b.id, u.id, u.ho_ten, u.sdt, t.id, t.ten_tour, b.so_luong_nguoi, ts.ngay_khoi_hanh, b.tong_tien, b.trang_thai, b.payment_method, b.ghi_chu, b.booking_at) "
			+ " FROM Booking b JOIN b.user u JOIN b.tourStart ts JOIN ts.tour t WHERE b.id = :id")
	BookingDetailDTO findBookingDetailById(@Param("id") Long id);

    @Query("SELECT SUM(b.tong_tien) FROM Booking b WHERE b.payment_status = 'da_thanh_toan'")
    Double sumTotalRevenue();
    
    // Native query để lấy bảng giao dịch với mã giao dịch
    @Query(value = "SELECT " +
            "ROW_NUMBER() OVER (ORDER BY b.booking_at DESC) AS stt, " +
            "CONCAT('GD', LPAD(b.id, 5, '0')) AS ma_giao_dich, " +
            "u.ho_ten AS khach_hang, " +
            "b.tong_tien AS so_tien, " +
            "b.payment_method AS phuong_thuc, " +
            "b.booking_at AS thoi_gian, " +
            "b.payment_status AS trang_thai " +
            "FROM booking b " +
            "JOIN user u ON b.user_id = u.id " +
            "ORDER BY b.booking_at DESC", nativeQuery = true)
    List<Object[]> findTransactionTable();
    
    // Native query để lấy bảng giao dịch với filter
    @Query(value = "SELECT " +
            "ROW_NUMBER() OVER (ORDER BY b.booking_at DESC) AS stt, " +
            "CONCAT('GD', LPAD(b.id, 5, '0')) AS ma_giao_dich, " +
            "u.ho_ten AS khach_hang, " +
            "b.tong_tien AS so_tien, " +
            "b.payment_method AS phuong_thuc, " +
            "b.booking_at AS thoi_gian, " +
            "b.payment_status AS trang_thai " +
            "FROM booking b " +
            "JOIN user u ON b.user_id = u.id " +
            "WHERE (:khachHang IS NULL OR u.ho_ten LIKE %:khachHang%) " +
            "AND (:trangThai IS NULL OR b.payment_status = :trangThai) " +
            "AND (:phuongThuc IS NULL OR b.payment_method = :phuongThuc) " +
            "ORDER BY b.booking_at DESC", nativeQuery = true)
    List<Object[]> findTransactionTableWithFilter(
            @Param("khachHang") String khachHang,
            @Param("trangThai") String trangThai,
            @Param("phuongThuc") String phuongThuc);
    
    // Native query để lấy bảng booking chi tiết
    @Query(value = "SELECT " +
            "b.id AS stt, " +
            "CONCAT('GD', LPAD(b.id, 5, '0')) AS ma_giao_dich, " +
            "u.ho_ten AS nguoi_dung, " +
            "t.ten_tour AS tour, " +
            "b.so_luong_nguoi, " +
            "b.tong_tien, " +
            "b.payment_method AS phuong_thuc_thanh_toan, " +
            "ts.ngay_khoi_hanh, " +
            "b.ghi_chu, " +
            "b.booking_at AS ngay_dat, " +
            "b.trang_thai " +
            "FROM booking b " +
            "JOIN user u ON b.user_id = u.id " +
            "JOIN tour_start ts ON b.tour_start_id = ts.id " +
            "JOIN tour t ON ts.tour_id = t.id " +
            "ORDER BY b.booking_at DESC", nativeQuery = true)
    List<Object[]> findBookingDetailTable();
    
    // Native query để lấy bảng booking chi tiết với filter
    @Query(value = "SELECT " +
            "b.id AS stt, " +
            "CONCAT('GD', LPAD(b.id, 5, '0')) AS ma_giao_dich, " +
            "u.ho_ten AS nguoi_dung, " +
            "t.ten_tour AS tour, " +
            "b.so_luong_nguoi, " +
            "b.tong_tien, " +
            "b.payment_method AS phuong_thuc_thanh_toan, " +
            "ts.ngay_khoi_hanh, " +
            "b.ghi_chu, " +
            "b.booking_at AS ngay_dat, " +
            "b.trang_thai " +
            "FROM booking b " +
            "JOIN user u ON b.user_id = u.id " +
            "JOIN tour_start ts ON b.tour_start_id = ts.id " +
            "JOIN tour t ON ts.tour_id = t.id " +
            "WHERE (:nguoiDung IS NULL OR u.ho_ten LIKE %:nguoiDung%) " +
            "AND (:tour IS NULL OR t.ten_tour LIKE %:tour%) " +
            "AND (:trangThai IS NULL OR b.trang_thai = :trangThai) " +
            "AND (:phuongThuc IS NULL OR b.payment_method = :phuongThuc) " +
            "ORDER BY b.booking_at DESC", nativeQuery = true)
    List<Object[]> findBookingDetailTableWithFilter(
            @Param("nguoiDung") String nguoiDung,
            @Param("tour") String tour,
            @Param("trangThai") String trangThai,
            @Param("phuongThuc") String phuongThuc);
}
