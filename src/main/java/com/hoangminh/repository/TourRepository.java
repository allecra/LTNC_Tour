package com.hoangminh.repository;

import org.springframework.data.domain.Pageable;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hoangminh.dto.TourDTO;
import com.hoangminh.entity.Tour;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long>,JpaSpecificationExecutor<Tour> {
	
	@Query(value= "SELECT new com.hoangminh.dto.TourDTO(t.id,t.ten_tour,t.gioi_thieu_tour,t.so_ngay,t.noi_dung_tour,t.ngay_ket_thuc,t.ngay_khoi_hanh,t.diem_den,t.loai_tour,t.anh_tour,t.diem_khoi_hanh,t.trang_thai,t.gia_tour) FROM Tour t "
			+ " WHERE (:ten_tour IS NULL OR :ten_tour='' OR t.ten_tour LIKE CONCAT('%', :ten_tour, '%'))"
			+ " AND ( :loai_tour IS NULL OR t.loai_tour = :loai_tour )"
			+ " AND ( :ngay_khoi_hanh IS NULL OR t.ngay_khoi_hanh = :ngay_khoi_hanh )"
			+ " AND ( :gia_tour_from IS NULL OR  :gia_tour_to IS NULL OR (t.gia_tour BETWEEN :gia_tour_from AND :gia_tour_to)) AND t.trang_thai=1 "
			+ " ORDER BY t.id ")
	Page<TourDTO> findAll(
			@Param("ten_tour") String ten_tour,
			@Param("gia_tour_from") Long gia_tour_from,
			@Param("gia_tour_to") Long gia_tour_to,
			@Param("ngay_khoi_hanh") Date ngay_khoi_hanh,
			@Param("loai_tour") Integer loai_tour,
			Pageable pageable
			);


	@Query(value= "SELECT new com.hoangminh.dto.TourDTO(t.id,t.ten_tour,t.gioi_thieu_tour,t.so_ngay,t.noi_dung_tour,t.ngay_ket_thuc,t.ngay_khoi_hanh,t.diem_den,t.loai_tour,t.anh_tour,t.diem_khoi_hanh,t.trang_thai,t.gia_tour) FROM Tour t "
			+ " WHERE (:ten_tour IS NULL OR :ten_tour='' OR t.ten_tour LIKE CONCAT('%', :ten_tour, '%'))"
			+ " AND ( :loai_tour IS NULL OR t.loai_tour = :loai_tour )"
			+ " AND ( :ngay_khoi_hanh IS NULL OR t.ngay_khoi_hanh = :ngay_khoi_hanh )"
			+ " AND ( :gia_tour_from IS NULL OR  :gia_tour_to IS NULL OR (t.gia_tour BETWEEN :gia_tour_from AND :gia_tour_to))  "
			+ " ORDER BY t.id ")
	Page<TourDTO> findAllAdmin(
			@Param("ten_tour") String ten_tour,
			@Param("gia_tour_from") Long gia_tour_from,
			@Param("gia_tour_to") Long gia_tour_to,
			@Param("ngay_khoi_hanh") Date ngay_khoi_hanh,
			@Param("loai_tour") Integer loai_tour,
			Pageable pageable
	);
	
	@Query(value = "SELECT new com.hoangminh.dto.TourDTO(t.id,t.ten_tour,t.gioi_thieu_tour,t.so_ngay,t.noi_dung_tour,t.ngay_ket_thuc,t.ngay_khoi_hanh,t.diem_den,t.loai_tour,t.anh_tour,t.diem_khoi_hanh,t.trang_thai,t.gia_tour) FROM Tour t "
			+ " WHERE t.id = :id")
	TourDTO findTourById(Long id);
	
	@Query(value ="SELECT new com.hoangminh.dto.TourDTO(t.id,t.ten_tour,t.gioi_thieu_tour,t.so_ngay,t.noi_dung_tour,t.ngay_ket_thuc,t.ngay_khoi_hanh,t.diem_den,t.loai_tour,t.anh_tour,t.diem_khoi_hanh,t.trang_thai,t.gia_tour) FROM Tour t "
			+ " JOIN Booking b ON t.id = b.tour_id WHERE b.tour_id = :booking_id" )
	TourDTO findTourByBookingId(@Param("booking_id") Long booking_id);

	@Query("SELECT COUNT(*) > 0 FROM Booking b WHERE b.tour_id = :tourId")
	boolean existsBookingByTourId(@Param("tourId") Long tourId);

	Tour findFirstByOrderByIdDesc();
}
