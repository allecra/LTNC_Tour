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
import com.hoangminh.repository.DestinationRepository;
import com.hoangminh.repository.TourRepository;
import com.hoangminh.repository.TourTypeRepository;
import com.hoangminh.service.TourService;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;

@Slf4j
@Service
public class TourServiceImpl implements TourService {

	@Autowired
	private TourRepository tourRepository;

	@Autowired
	private DestinationRepository destinationRepository;

	@Autowired
	private TourTypeRepository tourTypeRepository;

	@Override
	public Page<TourDTO> findAllTour(String ten_tour, BigDecimal gia_tour_from, BigDecimal gia_tour_to,
			Long tour_type_id, Pageable pageable) {
		Page<TourDTO> page = this.tourRepository.findAll(ten_tour, gia_tour_from, gia_tour_to, tour_type_id, pageable);
		return page;
	}

	@Override
	public Page<TourDTO> findAllTourAdmin(String ten_tour, BigDecimal gia_tour_from, BigDecimal gia_tour_to,
			Long tour_type_id, Pageable pageable) {
		Page<TourDTO> page = this.tourRepository.findAllAdmin(ten_tour, gia_tour_from, gia_tour_to, tour_type_id,
				pageable);
		return page;
	}

	@Override
	public TourDTO findTourById(Long id) {
		TourDTO tourDTO = this.tourRepository.findTourById(id);

		if (tourDTO != null) {
			return tourDTO;
		}
		return null;
	}

	@Override
	public boolean saveTour(Tour tour) {
		if (this.tourRepository.save(tour) != null) {
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
		tour.setAnh_dai_dien(tourDTO.getAnh_dai_dien());
		tour.setTourType(this.tourTypeRepository.findById(Long.parseLong(tourDTO.getLoai_tour())).get());
		tour.setGia_tour(tourDTO.getGia_tour());
		tour.setGioi_thieu_tour(tourDTO.getGioi_thieu_tour());
		tour.setDestination(this.destinationRepository.findById(Long.parseLong(tourDTO.getDiem_den())).get());
		tour.setNoi_dung_tour(tourDTO.getNoi_dung_tour());
		tour.setDiem_khoi_hanh(tourDTO.getDiem_khoi_hanh());
		tour.setSo_ngay(tourDTO.getSo_ngay());
		tour.setTrang_thai(tourDTO.getTrang_thai());
		tour.setSale_price(tourDTO.getSale_price());

		return this.tourRepository.save(tour);
	}

	@Override
	public Tour updateTour(TourDTO newTour, Long id) {
		Optional<Tour> tour = this.tourRepository.findById(id);
		log.info("new tour lấy đươc : {}", newTour);
		if (tour.isPresent()) {
			Tour updatedTour = tour.get();

			updatedTour.setTen_tour(newTour.getTen_tour());
			updatedTour.setTourType(this.tourTypeRepository.findById(Long.parseLong(newTour.getLoai_tour())).get());
			updatedTour.setGia_tour(newTour.getGia_tour());
			updatedTour.setGioi_thieu_tour(newTour.getGioi_thieu_tour());
			if (newTour.getAnh_dai_dien() != null) {
				updatedTour.setAnh_dai_dien(newTour.getAnh_dai_dien());
			}
			updatedTour
					.setDestination(this.destinationRepository.findById(Long.parseLong(newTour.getDiem_den())).get());
			updatedTour.setNoi_dung_tour(newTour.getNoi_dung_tour());
			updatedTour.setDiem_khoi_hanh(newTour.getDiem_khoi_hanh());
			updatedTour.setSo_ngay(newTour.getSo_ngay());
			updatedTour.setTrang_thai(newTour.getTrang_thai());
			updatedTour.setSale_price(newTour.getSale_price());

			return this.tourRepository.save(updatedTour);
		}
		return null;

	}

	@Override
	public boolean deleteTour(Long id) {
		Optional<Tour> tour = this.tourRepository.findById(id);
		if (tour.isPresent()) {
			this.tourRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public List<TourDTO> findBySeason(Long seasonId) {
		return tourRepository.findBySeason(seasonId);
	}

	@Override
	public List<TourDTO> findByMonth(int month) {
		return tourRepository.findByMonth(month);
	}

    public List<TourDTO> findAllTourWithStartDate() {
        List<Object[]> rawList = tourRepository.findAllTourWithStartDate();
        List<TourDTO> result = new ArrayList<>();
        for (Object[] row : rawList) {
            TourDTO dto = new TourDTO();
            dto.setId(((Number) row[0]).longValue());
            dto.setTen_tour((String) row[1]);
            dto.setGioi_thieu_tour((String) row[2]);
            dto.setSo_ngay(row[3] != null ? ((Number) row[3]).intValue() : null);
            dto.setNoi_dung_tour((String) row[4]);
            dto.setDiem_den((String) row[5]);
            dto.setLoai_tour((String) row[6]);
            dto.setAnh_dai_dien((String) row[7]);
            dto.setDiem_khoi_hanh((String) row[8]);
            dto.setTrang_thai((String) row[9]);
            dto.setGia_tour(row[10] != null ? new java.math.BigDecimal(row[10].toString()) : null);
            dto.setSale_price(row[11] != null ? new java.math.BigDecimal(row[11].toString()) : null);
            dto.setNgay_khoi_hanh(row[12] != null ? row[12].toString() : "");
            dto.setNgay_ket_thuc(row[13] != null ? row[13].toString() : "");
            result.add(dto);
        }
        return result;
    }
}
