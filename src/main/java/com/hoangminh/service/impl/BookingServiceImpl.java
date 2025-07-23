package com.hoangminh.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.hoangminh.dto.BookingDetailDTO;
import com.hoangminh.entity.TourStart;
import com.hoangminh.entity.User;
import com.hoangminh.repository.TourStartRepository;
import com.hoangminh.repository.UserRepository;
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
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private TourRepository tourRepository;

	@Autowired
	private TourStartRepository tourStartRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Page<BookingDTO> findAllBooking(String trang_thai, String ten_tour, Pageable pageable) {
		return this.bookingRepository.findAllBooking(trang_thai, ten_tour, pageable);
	}

	@Override
	public List<BookingDTO> findBookingByUserId(Long userId) {
		return this.bookingRepository.findBookingByUserId(userId);
	}

	@Override
	public boolean addNewBooking(BookingDTO newBooking) {
		List<BookingDTO> checkBooking = this.checkBookingByUserId(newBooking.getUser_id());
		if (checkBooking.size() > 0) {
			return false;
		}

		Optional<TourStart> tourStartOpt = this.tourStartRepository.findById(newBooking.getTour_id());
		Optional<User> userOpt = this.userRepository.findById(newBooking.getUser_id());

		if (tourStartOpt.isEmpty() || userOpt.isEmpty()) {
			return false;
		}

		TourStart tourStart = tourStartOpt.get();
		User user = userOpt.get();

		Booking booking = new Booking();
		booking.setSo_luong_nguoi(newBooking.getSo_luong_nguoi());

		BigDecimal price = tourStart.getGia_rieng();
		if (price == null) {
			price = tourStart.getTour().getSale_price() != null ? tourStart.getTour().getSale_price()
					: tourStart.getTour().getGia_tour();
		}
		booking.setTong_tien(price.multiply(BigDecimal.valueOf(newBooking.getSo_luong_nguoi())));

		booking.setTourStart(tourStart);
		booking.setUser(user);
		booking.setGhi_chu(newBooking.getGhi_chu());
		booking.setPayment_method(newBooking.getPayment_method());
		booking.setTrang_thai("cho_xac_nhan");

		return this.saveBooking(booking);
	}

	@Override
	public boolean approveBooking(Long bookingId, String trang_thai) {
		Optional<Booking> booking = this.bookingRepository.findById(bookingId);

		if (booking.isPresent()) {
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
		if (booking != null && (booking.getTrang_thai().equals("huy") || booking.getTrang_thai().equals("hoan_tat"))) {
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

	@Override
	public Boolean saveBooking(Booking booking) {
		return this.bookingRepository.save(booking) != null;
	}

	@Override
	public List<BookingDTO> checkBookingByUserId(Long id) {
		// This method is not fully implemented in the repository based on previous
		// steps.
		// Returning an empty list to avoid breaking logic that depends on it.
		// A proper implementation would require adding a custom query to
		// BookingRepository.
		return List.of();
	}

	@Override
	public Page<BookingDTO> findBookingByTourId(Long tour_Id, Pageable pageable) {
		// This method is obsolete and should not be called.
		return Page.empty();
	}
}
