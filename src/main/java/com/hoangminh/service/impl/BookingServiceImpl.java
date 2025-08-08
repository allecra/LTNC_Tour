package com.hoangminh.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hoangminh.dto.BookingDTO;
import com.hoangminh.dto.BookingDetailDTO;
import com.hoangminh.entity.Booking;
import com.hoangminh.entity.TourStart;
import com.hoangminh.entity.User;
import com.hoangminh.repository.BookingRepository;
import com.hoangminh.repository.TourRepository;
import com.hoangminh.repository.TourStartRepository;
import com.hoangminh.repository.UserRepository;
import com.hoangminh.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService {

	private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

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
		// Kiểm tra booking đang hoạt động (không bị hủy hoặc hoàn tất)
		List<BookingDTO> checkBooking = this.checkActiveBookingByUserId(newBooking.getUser_id());
		if (checkBooking.size() > 0) {
			logger.warn("User đã có booking đang hoạt động: {}", newBooking.getUser_id());
			return false;
		}

		Optional<TourStart> tourStartOpt = this.tourStartRepository.findById(newBooking.getTour_id());
		Optional<User> userOpt = this.userRepository.findById(newBooking.getUser_id());

		if (tourStartOpt.isEmpty() || userOpt.isEmpty()) {
			logger.error("Không tìm thấy user_id: {} hoặc tour_start_id: {}", newBooking.getUser_id(), newBooking.getTour_id());
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

		boolean result = this.saveBooking(booking);
		if (!result) {
			logger.error("Lưu booking thất bại cho user_id: {}, tour_start_id: {}", newBooking.getUser_id(), newBooking.getTour_id());
		}
		return result;
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
		if (booking != null) {
			// Cho phép xóa booking ở trạng thái hủy, hoàn tất, hoặc cho xác nhận
			if (booking.getTrang_thai().equals("huy") || 
				booking.getTrang_thai().equals("hoan_tat") || 
				booking.getTrang_thai().equals("cho_xac_nhan")) {
				this.bookingRepository.deleteById(id);
				return true;
			} else {
				logger.warn("Không thể xóa booking có trạng thái: {}", booking.getTrang_thai());
			}
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
		return this.bookingRepository.findBookingByUserId(id);
	}
	
	@Override
	public List<BookingDTO> checkActiveBookingByUserId(Long id) {
		return this.bookingRepository.findActiveBookingByUserId(id);
	}

	@Override
	public Page<BookingDTO> findBookingByTourId(Long tour_Id, Pageable pageable) {
		// This method is obsolete and should not be called.
		return Page.empty();
	}
	
	@Override
	public boolean updateBooking(Long id, BookingDTO bookingDTO) {
		Optional<Booking> bookingOpt = this.bookingRepository.findById(id);
		if (bookingOpt.isPresent()) {
			Booking booking = bookingOpt.get();
			if (bookingDTO.getSo_luong_nguoi() != null) booking.setSo_luong_nguoi(bookingDTO.getSo_luong_nguoi());
			if (bookingDTO.getTong_tien() != null) booking.setTong_tien(bookingDTO.getTong_tien());
			booking.setPayment_method("Chuyển khoản"); // Luôn cố định là "Chuyển khoản"
			if (bookingDTO.getGhi_chu() != null) booking.setGhi_chu(bookingDTO.getGhi_chu());
			if (bookingDTO.getTrang_thai() != null) booking.setTrang_thai(bookingDTO.getTrang_thai());
			if (bookingDTO.getPayment_status() != null) booking.setPayment_status(bookingDTO.getPayment_status());
			// Có thể cập nhật thêm các trường khác nếu cần
			this.bookingRepository.save(booking);
			return true;
		}
		return false;
	}
	
	@Override
	public List<Object[]> getTransactionTable() {
		return this.bookingRepository.findTransactionTable();
	}
	
	@Override
	public List<Object[]> getTransactionTableWithFilter(String khachHang, String trangThai, String phuongThuc) {
		return this.bookingRepository.findTransactionTableWithFilter(khachHang, trangThai, phuongThuc);
	}
	
	@Override
	public List<Object[]> getBookingDetailTable() {
		return this.bookingRepository.findBookingDetailTable();
	}
	
	@Override
	public List<Object[]> getBookingDetailTableWithFilter(String nguoiDung, String tour, String trangThai, String phuongThuc) {
		return this.bookingRepository.findBookingDetailTableWithFilter(nguoiDung, tour, trangThai, phuongThuc);
	}
}
