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
			+ " FROM Booking b WHERE b.id = :id")
	BookingDTO findBookingById(@Param("id") Long id);

	@Query(value = "SELECT new com.hoangminh.dto.BookingDetailDTO(b.id, u.id, u.ho_ten, u.sdt, t.id, t.ten_tour, b.so_luong_nguoi, ts.ngay_khoi_hanh, b.tong_tien, b.trang_thai, b.payment_method, b.ghi_chu, b.booking_at) "
			+ " FROM Booking b JOIN b.user u JOIN b.tourStart ts JOIN ts.tour t WHERE b.id = :id")
	BookingDetailDTO findBookingDetailById(@Param("id") Long id);

    @Query("SELECT SUM(b.tong_tien) FROM Booking b WHERE b.trang_thai = 'da_thanh_toan'")
    Double sumTotalRevenue();
}
