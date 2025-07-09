package com.hoangminh.service.impl;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hoangminh.dto.TourDTO;
import com.hoangminh.entity.Image;
import com.hoangminh.entity.Tour;
import com.hoangminh.entity.TourStart;
import com.hoangminh.repository.TourRepository;
import com.hoangminh.service.TourService;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
@Slf4j
@Service
public class TourServiceImpl implements TourService {

	@Autowired
	private TourRepository tourRepository;


	@Override
	public Page<TourDTO> findAllTour(String ten_tour,Long gia_tour_from,Long gia_tour_to,Date ngay_khoi_hanh,Integer loai_tour,Pageable pageable) {
		Page<TourDTO> page = this.tourRepository.findAll(ten_tour, gia_tour_from, gia_tour_to,ngay_khoi_hanh, loai_tour, pageable);
		return page;
	}

	@Override
	public Page<TourDTO> findAllTourAdmin(String ten_tour,Long gia_tour_from,Long gia_tour_to,Date ngay_khoi_hanh,Integer loai_tour,Pageable pageable) {
		Page<TourDTO> page = this.tourRepository.findAllAdmin(ten_tour, gia_tour_from, gia_tour_to,ngay_khoi_hanh, loai_tour, pageable);
		return page;
	}

	@Override
	public TourDTO findTourById(Long id) {
		TourDTO tourDTO = this.tourRepository.findTourById(id);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(tourDTO.getNgay_khoi_hanh());
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		tourDTO.setNgay_khoi_hanh(calendar.getTime());


		calendar.setTime(tourDTO.getNgay_ket_thuc());
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		tourDTO.setNgay_ket_thuc(calendar.getTime());

		if(tourDTO!=null) {
			return tourDTO;
		}
		return null;
	}

	@Override
	public boolean saveTour(Tour tour) {
		if(this.tourRepository.save(tour) != null) {
			return true;
		}
		return false;
	}

	@Override
	public Tour findFirstByOrderByIdDesc() {
		return this.tourRepository.findFirstByOrderByIdDesc();
	}

	@Override
	public Tour addTour(TourDTO tourDTO) {

		Tour tour = new Tour();
		tour.setTen_tour(tourDTO.getTen_tour());
		tour.setAnh_tour(tourDTO.getAnh_tour());
		tour.setLoai_tour(tourDTO.getLoai_tour());
		tour.setGia_tour(tourDTO.getGia_tour());
		tour.setGioi_thieu_tour(tourDTO.getGioi_thieu_tour());
		tour.setDiem_den(tourDTO.getDiem_den());
		tour.setNoi_dung_tour(tourDTO.getNoi_dung_tour());
		tour.setDiem_khoi_hanh(tourDTO.getDiem_khoi_hanh());
		tour.setNgay_khoi_hanh(tourDTO.getNgay_khoi_hanh());
		tour.setSo_ngay(tourDTO.getSo_ngay());
		tour.setTrang_thai(tourDTO.getTrang_thai());
		tour.setNgay_ket_thuc(tourDTO.getNgay_ket_thuc());

		return this.tourRepository.save(tour);
	}

	@Override
	public Tour updateTour(TourDTO newTour, Long id) {
		Optional<Tour> tour = this.tourRepository.findById(id);
		log.info("new tour lấy đươc : {}",newTour);
		if(tour.isPresent()) {
			Tour updatedTour = tour.get();

			updatedTour.setTen_tour(newTour.getTen_tour());
			updatedTour.setLoai_tour(newTour.getLoai_tour());
			updatedTour.setGia_tour(newTour.getGia_tour());
			updatedTour.setGioi_thieu_tour(newTour.getGioi_thieu_tour());
			if(newTour.getAnh_tour()!=null) {
				updatedTour.setAnh_tour(newTour.getAnh_tour());
			}
			updatedTour.setDiem_den(newTour.getDiem_den());
			updatedTour.setNoi_dung_tour(newTour.getNoi_dung_tour());
			updatedTour.setDiem_khoi_hanh(newTour.getDiem_khoi_hanh());
			updatedTour.setNgay_khoi_hanh(newTour.getNgay_khoi_hanh());
			updatedTour.setSo_ngay(newTour.getSo_ngay());
			updatedTour.setTrang_thai(newTour.getTrang_thai());
			updatedTour.setNgay_ket_thuc(newTour.getNgay_ket_thuc());

			return this.tourRepository.save(updatedTour);
		}
		return null;

	}

	@Override
	public boolean deleteTour(Long id) {
		Optional<Tour> tour = this.tourRepository.findById(id);
		if(tour.isPresent()) {
			if(this.tourRepository.existsBookingByTourId(id)==false) {
				this.tourRepository.deleteById(id);
				return true;
			}
		}
		return false;
	}
}
