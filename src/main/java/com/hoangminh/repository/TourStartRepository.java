package com.hoangminh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hoangminh.dto.TourStartDTO;
import com.hoangminh.entity.TourStart;

@Repository
public interface TourStartRepository extends JpaRepository<TourStart, Long> {

	List<TourStart> findByTourId(Long tourId);

	@Query("SELECT DISTINCT t.ngay_khoi_hanh FROM TourStart t WHERE t.ngay_khoi_hanh >= CURRENT_DATE ORDER BY t.ngay_khoi_hanh")
	List<java.util.Date> findAllAvailableDates();
}
