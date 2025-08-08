package com.hoangminh.service;

import java.util.List;

import com.hoangminh.dto.BookingDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hoangminh.dto.BookingDTO;
import com.hoangminh.entity.Booking;

public interface BookingService {

	Page<BookingDTO> findAllBooking(String trang_thai, String ten_tour, Pageable pageable);

	List<BookingDTO> findBookingByUserId(Long id);

	Page<BookingDTO> findBookingByTourId(Long tour_Id, Pageable pageable);

	boolean addNewBooking(BookingDTO newBooking);

	boolean approveBooking(Long id, String trang_thai);

	boolean deleteBooking(Long id);

	BookingDTO getBookingById(Long id);

	BookingDetailDTO getBookingDetailById(Long id);

	Boolean saveBooking(Booking booking);

	List<BookingDTO> checkBookingByUserId(Long id);
	
	List<BookingDTO> checkActiveBookingByUserId(Long id);

	boolean updateBooking(Long id, BookingDTO bookingDTO);
	
	// Bảng giao dịch
	List<Object[]> getTransactionTable();
	
	List<Object[]> getTransactionTableWithFilter(String khachHang, String trangThai, String phuongThuc);
	
	// Bảng booking chi tiết
	List<Object[]> getBookingDetailTable();
	
	List<Object[]> getBookingDetailTableWithFilter(String nguoiDung, String tour, String trangThai, String phuongThuc);
}
