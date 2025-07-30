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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long>, JpaSpecificationExecutor<Tour> {

	@Query(value = "SELECT new com.hoangminh.dto.TourDTO(t.id, t.ten_tour, t.gioi_thieu_tour, t.so_ngay, t.noi_dung_tour, t.destination.name, t.tourType.ten_loai, t.anh_dai_dien, t.diem_khoi_hanh, t.trang_thai, t.gia_tour, t.sale_price) FROM Tour t "
			+ " WHERE (:ten_tour IS NULL OR :ten_tour='' OR t.ten_tour LIKE CONCAT('%', :ten_tour, '%'))"
			+ " AND ( :tour_type_id IS NULL OR t.tourType.id = :tour_type_id )"
			+ " AND ( :gia_tour_from IS NULL OR  :gia_tour_to IS NULL OR (t.gia_tour BETWEEN :gia_tour_from AND :gia_tour_to)) AND t.trang_thai='dang_mo_ban' "
			+ " ORDER BY t.id ")
	Page<TourDTO> findAll(
			@Param("ten_tour") String ten_tour,
			@Param("gia_tour_from") BigDecimal gia_tour_from,
			@Param("gia_tour_to") BigDecimal gia_tour_to,
			@Param("tour_type_id") Long tour_type_id,
			Pageable pageable);

	@Query(value = "SELECT new com.hoangminh.dto.TourDTO(t.id, t.ten_tour, t.gioi_thieu_tour, t.so_ngay, t.noi_dung_tour, t.destination.name, t.tourType.ten_loai, t.anh_dai_dien, t.diem_khoi_hanh, t.trang_thai, t.gia_tour, t.sale_price) FROM Tour t "
			+ " WHERE (:ten_tour IS NULL OR :ten_tour='' OR t.ten_tour LIKE CONCAT('%', :ten_tour, '%'))"
			+ " AND ( :tour_type_id IS NULL OR t.tourType.id = :tour_type_id )"
			+ " AND ( :gia_tour_from IS NULL OR  :gia_tour_to IS NULL OR (t.gia_tour BETWEEN :gia_tour_from AND :gia_tour_to))  "
			+ " ORDER BY t.id ")
	Page<TourDTO> findAllAdmin(
			@Param("ten_tour") String ten_tour,
			@Param("gia_tour_from") BigDecimal gia_tour_from,
			@Param("gia_tour_to") BigDecimal gia_tour_to,
			@Param("tour_type_id") Long tour_type_id,
			Pageable pageable);

	@Query(value = "SELECT new com.hoangminh.dto.TourDTO(t.id, t.ten_tour, t.gioi_thieu_tour, t.so_ngay, t.noi_dung_tour, t.destination.name, t.tourType.ten_loai, t.anh_dai_dien, t.diem_khoi_hanh, t.trang_thai, t.gia_tour, t.sale_price) FROM Tour t "
			+ " WHERE t.id = :id")
	TourDTO findTourById(Long id);
	
	@Query(value = "SELECT t.id, t.ten_tour, t.gioi_thieu_tour, t.so_ngay, t.noi_dung_tour, d.name as diem_den, tt.ten_loai, t.anh_dai_dien, t.diem_khoi_hanh, t.trang_thai, t.gia_tour, t.sale_price, MIN(ts.ngay_khoi_hanh) as ngay_khoi_hanh, MIN(ts.ngay_ket_thuc) as ngay_ket_thuc " +
            "FROM tour t " +
            "LEFT JOIN destination d ON t.destination_id = d.id " +
            "LEFT JOIN tour_type tt ON t.tour_type_id = tt.id " +
            "LEFT JOIN tour_start ts ON t.id = ts.tour_id " +
            "WHERE t.id = :id " +
            "GROUP BY t.id, t.ten_tour, t.gioi_thieu_tour, t.so_ngay, t.noi_dung_tour, d.name, tt.ten_loai, t.anh_dai_dien, t.diem_khoi_hanh, t.trang_thai, t.gia_tour, t.sale_price", nativeQuery = true)
    List<Object[]> findTourByIdWithStartDate(@Param("id") Long id);

	Tour findFirstByOrderByIdDesc();

	@Query("SELECT new com.hoangminh.dto.TourDTO(t.id, t.ten_tour, t.gioi_thieu_tour, t.so_ngay, t.noi_dung_tour, d.name, tt.ten_loai, t.anh_dai_dien, t.diem_khoi_hanh, t.trang_thai, t.gia_tour, t.sale_price) "
			+
			"FROM Tour t " +
			"JOIN t.destination d " +
			"JOIN t.tourType tt " +
			"LEFT JOIN t.tour_starts ts " +
			"LEFT JOIN ts.season s " +
			"WHERE s.id = :seasonId")
	List<TourDTO> findBySeason(@Param("seasonId") Long seasonId);

	@Query("SELECT new com.hoangminh.dto.TourDTO(t.id, t.ten_tour, t.gioi_thieu_tour, t.so_ngay, t.noi_dung_tour, d.name, tt.ten_loai, t.anh_dai_dien, t.diem_khoi_hanh, t.trang_thai, t.gia_tour, t.sale_price) "
			+
			"FROM Tour t " +
			"JOIN t.destination d " +
			"JOIN t.tourType tt " +
			"LEFT JOIN t.tour_starts ts " +
			"WHERE FUNCTION('MONTH', ts.ngay_khoi_hanh) = :month")
	List<TourDTO> findByMonth(@Param("month") int month);

    @Query("SELECT COUNT(t) FROM Tour t")
    Long countAllTours();

    @Query("SELECT FUNCTION('MONTH', ts.ngay_khoi_hanh) as month, COUNT(t) as count " +
           "FROM Tour t JOIN t.tour_starts ts " +
           "GROUP BY FUNCTION('MONTH', ts.ngay_khoi_hanh) " +
           "ORDER BY month")
    List<Object[]> countToursByMonth();

    @Query("SELECT s.ten_mua as season, COUNT(t) as count " +
           "FROM Tour t JOIN t.tour_starts ts JOIN ts.season s " +
           "GROUP BY s.ten_mua")
    List<Object[]> countToursBySeason();

    @Query(value = "SELECT t.id, t.ten_tour, t.gioi_thieu_tour, t.so_ngay, t.noi_dung_tour, d.name as diem_den, tt.ten_loai, t.anh_dai_dien, t.diem_khoi_hanh, t.trang_thai, t.gia_tour, t.sale_price, MIN(ts.ngay_khoi_hanh) as ngay_khoi_hanh, MIN(ts.ngay_ket_thuc) as ngay_ket_thuc " +
            "FROM tour t " +
            "LEFT JOIN destination d ON t.destination_id = d.id " +
            "LEFT JOIN tour_type tt ON t.tour_type_id = tt.id " +
            "LEFT JOIN tour_start ts ON t.id = ts.tour_id " +
            "GROUP BY t.id, t.ten_tour, t.gioi_thieu_tour, t.so_ngay, t.noi_dung_tour, d.name, tt.ten_loai, t.anh_dai_dien, t.diem_khoi_hanh, t.trang_thai, t.gia_tour, t.sale_price " +
            "ORDER BY t.id", nativeQuery = true)
    List<Object[]> findAllTourWithStartDate();
}
