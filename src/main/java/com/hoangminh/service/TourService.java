package com.hoangminh.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.hoangminh.dto.TourDTO;
import com.hoangminh.entity.Tour;

import java.math.BigDecimal;

public interface TourService {

	Page<TourDTO> findAllTour(String ten_tour, BigDecimal gia_tour_from, BigDecimal gia_tour_to, Long tour_type_id,
			Pageable pageable);

	List<TourDTO> findBySeason(Long seasonId);

	List<TourDTO> findByMonth(int month);

	Page<TourDTO> findAllTourAdmin(String ten_tour, BigDecimal gia_tour_from, BigDecimal gia_tour_to, Long tour_type_id,
			Pageable pageable);

	TourDTO findTourById(Long id);

	boolean saveTour(Tour tour);

	Tour findFirstByOrderByIdDesc();

	Tour addTour(TourDTO tourDTO);

	Tour updateTour(TourDTO newTour, Long id);

	boolean deleteTour(Long id);

	List<TourDTO> findAllTourWithStartDate();
}
