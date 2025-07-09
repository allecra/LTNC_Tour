package com.hoangminh.service.impl;

import java.util.List;
import java.util.Optional;

import com.hoangminh.dto.BookingDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hoangminh.dto.BookingDTO;
import com.hoangminh.dto.TourDTO;
import com.hoangminh.entity.Booking;
import com.hoangminh.repository.BookingRepository;
import com.hoangminh.repository.TourRepository;
import com.hoangminh.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService{

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private TourRepository tourRepository;

	@Override
	public Page<BookingDTO> findAllBooking(Integer trang_thai,String ten_tour,Pageable pageable) {
		return this.bookingRepository.findAllBooking(trang_thai,ten_tour, pageable);
	}

	@Override
	public List<BookingDTO> findBookingByUserId(Long userId) {
		return this.bookingRepository.findBookingByUserId(userId);
	}

	@Override
	public Page<BookingDTO> findBookingByTourId(Long tour_Id,Pageable pageable) {
		return this.bookingRepository.findBookingByTourId(tour_Id, pageable);
	}

	@Override
	public boolean addNewBooking(BookingDTO newBooking) {


		List<BookingDTO> checkBooking  = this.bookingRepository.checkBookingByUserId(newBooking.getUser_id());
		if(checkBooking.size()>0) {
			return false;
		}

		TourDTO tourDTO = this.tourRepository.findTourById(newBooking.getTour_id());

		Booking booking = new Booking();
		booking.setSo_luong_nguoi(newBooking.getSo_luong_nguoi());
		booking.setNgay_khoi_hanh(newBooking.getNgay_khoi_hanh());
		booking.setTong_tien(tourDTO.getGia_tour()*newBooking.getSo_luong_nguoi());
		booking.setTour_id(newBooking.getTour_id());
		booking.setUser_id(newBooking.getUser_id());
		booking.setSo_luong_nguoi(newBooking.getSo_luong_nguoi());
		booking.setGhi_chu(newBooking.getGhi_chu());
		booking.setPt_thanh_toan(newBooking.getPt_thanh_toan());
		booking.setTrang_thai(0);

		this.bookingRepository.save(booking);

		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean approveBooking(Long bookingId,Integer trang_thai) {
		Optional<Booking> booking  = this.bookingRepository.findById(bookingId);

		if(booking.isPresent()) {
			Booking bookingUpdated = booking.get();
			bookingUpdated.setTrang_thai(trang_thai);
			this.bookingRepository.save(bookingUpdated);
			return true;
		}

		return false;
	}

	@Override
	public boolean deleteBooking(Long id) {

		BookingDTO booking = this.getBookingById(id);

		if(booking.getTrang_thai()==3) {
			this.bookingRepository.deleteById(id);
			return true;
		}

		return false;
	}

	@Override
	public BookingDTO getBookingById(Long id) {
		return this.bookingRepository.findBookingById(id);
	}

	@Override
	public BookingDetailDTO getBookingDetailById(Long id) {
		return this.bookingRepository.findBookingDetailById(id);
	}

}
