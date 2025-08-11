package com.hoangminh.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoangminh.dto.BookingDTO;
import com.hoangminh.dto.BookingDetailDTO;
import com.hoangminh.entity.Booking;
import com.hoangminh.entity.TourStart;
import com.hoangminh.entity.User;
import com.hoangminh.entity.Voucher;
import com.hoangminh.repository.BookingRepository;
import com.hoangminh.repository.TourRepository;
import com.hoangminh.repository.TourStartRepository;
import com.hoangminh.repository.UserRepository;
import com.hoangminh.repository.VoucherRepository;
import com.hoangminh.service.BookingService;

@Service
@Transactional
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
	
	@Autowired
	private VoucherRepository voucherRepository;

	@Override
	public Page<BookingDTO> findAllBooking(String trang_thai, String ten_tour, Pageable pageable) {
		try {
			return this.bookingRepository.findAllBooking(trang_thai, ten_tour, pageable);
		} catch (Exception e) {
			logger.error("Lỗi khi tìm tất cả booking: {}", e.getMessage());
			throw new RuntimeException("Không thể tải danh sách booking", e);
		}
	}

	@Override
	public List<BookingDTO> findBookingByUserId(Long userId) {
		if (userId == null) {
			throw new IllegalArgumentException("User ID không được null");
		}
		try {
			return this.bookingRepository.findBookingByUserId(userId);
		} catch (Exception e) {
			logger.error("Lỗi khi tìm booking theo user ID {}: {}", userId, e.getMessage());
			throw new RuntimeException("Không thể tải booking của user", e);
		}
	}

	@Override
	public boolean addNewBooking(BookingDTO newBooking) {
		// Validation
		if (newBooking == null) {
			logger.error("BookingDTO không được null");
			return false;
		}
		
		if (newBooking.getUser_id() == null || newBooking.getTour_id() == null) {
			logger.error("User ID hoặc Tour ID không được null");
			return false;
		}
		
		if (newBooking.getSo_luong_nguoi() == null || newBooking.getSo_luong_nguoi() <= 0) {
			logger.error("Số lượng người phải lớn hơn 0");
			return false;
		}

		try {
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

			// Kiểm tra ngày khởi hành
			if (tourStart.getNgay_khoi_hanh().before(new java.util.Date())) {
				logger.error("Không thể đặt tour đã khởi hành");
				return false;
			}

			Booking booking = new Booking();
			booking.setSo_luong_nguoi(newBooking.getSo_luong_nguoi());

			BigDecimal price = tourStart.getGia_rieng();
			if (price == null) {
				price = tourStart.getTour().getSale_price() != null ? tourStart.getTour().getSale_price()
						: tourStart.getTour().getGia_tour();
			}
			
			// Tính tổng tiền ban đầu
			BigDecimal tongTienBanDau = price.multiply(BigDecimal.valueOf(newBooking.getSo_luong_nguoi()));
			booking.setTong_tien(tongTienBanDau);

			// Xử lý voucher nếu có
			Voucher voucher = null;
			BigDecimal tongTienSauVoucher = tongTienBanDau;
			
			if (newBooking.getVoucherCode() != null && !newBooking.getVoucherCode().trim().isEmpty()) {
				try {
					Optional<Voucher> voucherOpt = voucherRepository.findByMaGiamGia(newBooking.getVoucherCode().trim());
					if (voucherOpt.isPresent()) {
						voucher = voucherOpt.get();
						
						// Kiểm tra voucher còn hạn sử dụng không
						if (voucher.getNgayHetHan() != null && voucher.getNgayHetHan().before(new java.util.Date())) {
							logger.warn("Voucher đã hết hạn: {}", newBooking.getVoucherCode());
						} else {
							// Áp dụng voucher
							BigDecimal giaTriGiam = tongTienBanDau.multiply(voucher.getGiaTri())
								.divide(BigDecimal.valueOf(100));
							tongTienSauVoucher = tongTienBanDau.subtract(giaTriGiam);
							
							// Đảm bảo giá không âm
							if (tongTienSauVoucher.compareTo(BigDecimal.ZERO) < 0) {
								tongTienSauVoucher = BigDecimal.ZERO;
							}
							
							// Cập nhật tổng tiền sau voucher
							booking.setTong_tien(tongTienSauVoucher);
							logger.info("Áp dụng voucher {} thành công, giảm giá: {}", newBooking.getVoucherCode(), giaTriGiam);
						}
					} else {
						logger.warn("Không tìm thấy voucher: {}", newBooking.getVoucherCode());
					}
				} catch (Exception e) {
					logger.error("Lỗi khi xử lý voucher {}: {}", newBooking.getVoucherCode(), e.getMessage());
				}
			}

			booking.setTourStart(tourStart);
			booking.setUser(user);
			booking.setGhi_chu(newBooking.getGhi_chu());
			booking.setPayment_method(newBooking.getPayment_method() != null ? newBooking.getPayment_method() : "Chuyển khoản");
			booking.setTrang_thai("cho_xac_nhan");
			booking.setPayment_status("chua_thanh_toan");
			
			// Gán voucher nếu có
			if (voucher != null) {
				booking.setVoucher(voucher);
			}

			boolean result = this.saveBooking(booking);
			if (!result) {
				logger.error("Lưu booking thất bại cho user_id: {}, tour_start_id: {}", newBooking.getUser_id(), newBooking.getTour_id());
			}
			return result;
		} catch (Exception e) {
			logger.error("Lỗi khi thêm booking mới: {}", e.getMessage());
			return false;
		}
	}

	@Override
	public boolean approveBooking(Long bookingId, String trang_thai) {
		if (bookingId == null) {
			logger.error("Booking ID không được null");
			return false;
		}
		
		if (trang_thai == null || trang_thai.trim().isEmpty()) {
			logger.error("Trạng thái không được null hoặc rỗng");
			return false;
		}

		try {
			Optional<Booking> booking = this.bookingRepository.findById(bookingId);

			if (booking.isPresent()) {
				Booking bookingUpdated = booking.get();
				
				// Kiểm tra trạng thái hợp lệ
				if (!isValidStatus(trang_thai)) {
					logger.error("Trạng thái không hợp lệ: {}", trang_thai);
					return false;
				}
				
				bookingUpdated.setTrang_thai(trang_thai);
				
				// Nếu đã xác nhận, cập nhật trạng thái thanh toán
				if ("da_xac_nhan".equals(trang_thai)) {
					bookingUpdated.setPayment_status("da_thanh_toan");
				}
				
				this.bookingRepository.save(bookingUpdated);
				logger.info("Cập nhật trạng thái booking {} thành công: {}", bookingId, trang_thai);
				return true;
			}

			logger.warn("Không tìm thấy booking với ID: {}", bookingId);
			return false;
		} catch (Exception e) {
			logger.error("Lỗi khi cập nhật trạng thái booking {}: {}", bookingId, e.getMessage());
			return false;
		}
	}

	@Override
	public boolean deleteBooking(Long id) {
		if (id == null) {
			logger.error("Booking ID không được null");
			return false;
		}
		
		try {
			BookingDTO booking = this.getBookingById(id);
			if (booking != null) {
				// Cho phép xóa booking ở trạng thái hủy, hoàn tất, hoặc cho xác nhận
				if (booking.getTrang_thai().equals("huy") || 
					booking.getTrang_thai().equals("hoan_tat") || 
					booking.getTrang_thai().equals("cho_xac_nhan")) {
					this.bookingRepository.deleteById(id);
					logger.info("Xóa booking {} thành công", id);
					return true;
				} else {
					logger.warn("Không thể xóa booking có trạng thái: {}", booking.getTrang_thai());
				}
			}
			return false;
		} catch (Exception e) {
			logger.error("Lỗi khi xóa booking {}: {}", id, e.getMessage());
			return false;
		}
	}

	@Override
	public BookingDTO getBookingById(Long id) {
		if (id == null) {
			logger.error("Booking ID không được null");
			return null;
		}
		
		try {
			return this.bookingRepository.findBookingById(id);
		} catch (Exception e) {
			logger.error("Lỗi khi tìm booking theo ID {}: {}", id, e.getMessage());
			return null;
		}
	}

	@Override
	public BookingDetailDTO getBookingDetailById(Long id) {
		if (id == null) {
			logger.error("Booking ID không được null");
			return null;
		}
		
		try {
			return this.bookingRepository.findBookingDetailById(id);
		} catch (Exception e) {
			logger.error("Lỗi khi tìm chi tiết booking theo ID {}: {}", id, e.getMessage());
			return null;
		}
	}

	@Override
	public Boolean saveBooking(Booking booking) {
		if (booking == null) {
			logger.error("Booking entity không được null");
			return false;
		}
		
		try {
			Booking savedBooking = this.bookingRepository.save(booking);
			return savedBooking != null;
		} catch (Exception e) {
			logger.error("Lỗi khi lưu booking: {}", e.getMessage());
			return false;
		}
	}

	@Override
	public List<BookingDTO> checkBookingByUserId(Long id) {
		if (id == null) {
			logger.error("User ID không được null");
			return List.of();
		}
		
		try {
			return this.bookingRepository.findBookingByUserId(id);
		} catch (Exception e) {
			logger.error("Lỗi khi kiểm tra booking theo user ID {}: {}", id, e.getMessage());
			return List.of();
		}
	}
	
	@Override
	public List<BookingDTO> checkActiveBookingByUserId(Long id) {
		if (id == null) {
			logger.error("User ID không được null");
			return List.of();
		}
		
		try {
			return this.bookingRepository.findActiveBookingByUserId(id);
		} catch (Exception e) {
			logger.error("Lỗi khi kiểm tra booking đang hoạt động theo user ID {}: {}", id, e.getMessage());
			return List.of();
		}
	}

	@Override
	public Page<BookingDTO> findBookingByTourId(Long tour_Id, Pageable pageable) {
		// This method is obsolete and should not be called.
		logger.warn("Method findBookingByTourId đã bị deprecated");
		return Page.empty();
	}
	
	@Override
	public boolean updateBooking(Long id, BookingDTO bookingDTO) {
		if (id == null || bookingDTO == null) {
			logger.error("ID hoặc BookingDTO không được null");
			return false;
		}
		
		try {
			Optional<Booking> bookingOpt = this.bookingRepository.findById(id);
			if (bookingOpt.isPresent()) {
				Booking booking = bookingOpt.get();
				
				// Cập nhật số lượng người
				if (bookingDTO.getSo_luong_nguoi() != null && bookingDTO.getSo_luong_nguoi() > 0) {
					booking.setSo_luong_nguoi(bookingDTO.getSo_luong_nguoi());
					
					// Tính lại tổng tiền ban đầu
					BigDecimal giaTour = booking.getTourStart().getGia_rieng();
					if (giaTour == null) {
						giaTour = booking.getTourStart().getTour().getSale_price() != null ? 
							booking.getTourStart().getTour().getSale_price() : 
							booking.getTourStart().getTour().getGia_tour();
					}
					BigDecimal tongTienBanDau = giaTour.multiply(BigDecimal.valueOf(bookingDTO.getSo_luong_nguoi()));
					
					// Xử lý voucher nếu có
					Voucher voucher = null;
					BigDecimal tongTienSauVoucher = tongTienBanDau;
					
					if (bookingDTO.getVoucherCode() != null && !bookingDTO.getVoucherCode().trim().isEmpty()) {
						try {
							Optional<Voucher> voucherOpt = voucherRepository.findByMaGiamGia(bookingDTO.getVoucherCode().trim());
							if (voucherOpt.isPresent()) {
								voucher = voucherOpt.get();
								
								// Kiểm tra voucher còn hạn sử dụng không
								if (voucher.getNgayHetHan() != null && voucher.getNgayHetHan().before(new java.util.Date())) {
									logger.warn("Voucher đã hết hạn: {}", bookingDTO.getVoucherCode());
								} else {
									// Áp dụng voucher
									BigDecimal giaTriGiam = tongTienBanDau.multiply(voucher.getGiaTri())
										.divide(BigDecimal.valueOf(100));
									tongTienSauVoucher = tongTienBanDau.subtract(giaTriGiam);
									
									// Đảm bảo giá không âm
									if (tongTienSauVoucher.compareTo(BigDecimal.ZERO) < 0) {
										tongTienSauVoucher = BigDecimal.ZERO;
									}
									
									// Cập nhật tổng tiền sau voucher
									booking.setTong_tien(tongTienSauVoucher);
									logger.info("Áp dụng voucher {} thành công, giảm giá: {}", bookingDTO.getVoucherCode(), giaTriGiam);
								}
							} else {
								logger.warn("Không tìm thấy voucher: {}", bookingDTO.getVoucherCode());
							}
						} catch (Exception e) {
							logger.error("Lỗi khi xử lý voucher {}: {}", bookingDTO.getVoucherCode(), e.getMessage());
						}
					} else {
						// Nếu không có voucher, sử dụng giá ban đầu
						booking.setTong_tien(tongTienBanDau);
					}
					
					// Gán voucher nếu có
					if (voucher != null) {
						booking.setVoucher(voucher);
					} else {
						booking.setVoucher(null);
					}
				}
				
				// Cập nhật các trường khác
				if (bookingDTO.getGhi_chu() != null) {
					booking.setGhi_chu(bookingDTO.getGhi_chu());
				}
				if (bookingDTO.getTrang_thai() != null && isValidStatus(bookingDTO.getTrang_thai())) {
					booking.setTrang_thai(bookingDTO.getTrang_thai());
				}
				if (bookingDTO.getPayment_status() != null) {
					booking.setPayment_status(bookingDTO.getPayment_status());
				}
				
				// Luôn cố định payment method là "Chuyển khoản"
				booking.setPayment_method("Chuyển khoản");
				
				this.bookingRepository.save(booking);
				logger.info("Cập nhật booking {} thành công", id);
				return true;
			}
			
			logger.warn("Không tìm thấy booking với ID: {}", id);
			return false;
		} catch (Exception e) {
			logger.error("Lỗi khi cập nhật booking {}: {}", id, e.getMessage());
			return false;
		}
	}
	
	@Override
	public List<Object[]> getTransactionTable() {
		try {
			return this.bookingRepository.findTransactionTable();
		} catch (Exception e) {
			logger.error("Lỗi khi lấy bảng giao dịch: {}", e.getMessage());
			return List.of();
		}
	}
	
	@Override
	public List<Object[]> getTransactionTableWithFilter(String khachHang, String trangThai, String maNoiDung) {
		try {
			return this.bookingRepository.findTransactionTableWithFilter(khachHang, trangThai, maNoiDung);
		} catch (Exception e) {
			logger.error("Lỗi khi lấy bảng giao dịch với filter: {}", e.getMessage());
			return List.of();
		}
	}
	
	@Override
	public List<Object[]> getBookingDetailTable() {
		try {
			return this.bookingRepository.findBookingDetailTable();
		} catch (Exception e) {
			logger.error("Lỗi khi lấy bảng booking chi tiết: {}", e.getMessage());
			return List.of();
		}
	}
	
	@Override
	public List<Object[]> getBookingDetailTableWithFilter(String nguoiDung, String tour, String trangThai, String maNoiDung) {
		try {
			return this.bookingRepository.findBookingDetailTableWithFilter(nguoiDung, tour, trangThai, maNoiDung);
		} catch (Exception e) {
			logger.error("Lỗi khi lấy bảng booking chi tiết với filter: {}", e.getMessage());
			return List.of();
		}
	}
	
	// Helper methods
	private boolean isValidStatus(String status) {
		return status != null && (
			"cho_xac_nhan".equals(status) ||
			"da_xac_nhan".equals(status) ||
			"dang_tien_hanh".equals(status) ||
			"hoan_tat".equals(status) ||
			"huy".equals(status)
		);
	}
	
	/**
	 * Tính toán tổng tiền sau khi áp dụng voucher
	 */
	public BigDecimal calculateFinalPrice(BigDecimal originalPrice, String voucherCode) {
		if (voucherCode == null || voucherCode.trim().isEmpty()) {
			return originalPrice;
		}
		
		try {
			Optional<Voucher> voucherOpt = voucherRepository.findByMaGiamGia(voucherCode);
			if (voucherOpt.isPresent()) {
				Voucher voucher = voucherOpt.get();
				BigDecimal discountAmount = originalPrice.multiply(voucher.getGiaTri())
					.divide(BigDecimal.valueOf(100));
				return originalPrice.subtract(discountAmount);
			}
		} catch (Exception e) {
			logger.error("Lỗi khi tính toán giá sau voucher: {}", e.getMessage());
		}
		
		return originalPrice;
	}
	
	/**
	 * Kiểm tra xem user có thể đặt tour hay không
	 */
	public boolean canUserBookTour(Long userId, Long tourStartId) {
		try {
			// Kiểm tra user có booking đang hoạt động không
			List<BookingDTO> activeBookings = checkActiveBookingByUserId(userId);
			if (!activeBookings.isEmpty()) {
				return false;
			}
			
			// Kiểm tra tour start có hợp lệ không
			Optional<TourStart> tourStartOpt = tourStartRepository.findById(tourStartId);
			if (tourStartOpt.isEmpty()) {
				return false;
			}
			
			TourStart tourStart = tourStartOpt.get();
			
			// Kiểm tra ngày khởi hành
			if (tourStart.getNgay_khoi_hanh().before(new java.util.Date())) {
				return false;
			}
			
			return true;
		} catch (Exception e) {
			logger.error("Lỗi khi kiểm tra khả năng đặt tour: {}", e.getMessage());
			return false;
		}
	}
	
	@Override
	public boolean isValidVoucher(String voucherCode) {
		if (voucherCode == null || voucherCode.trim().isEmpty()) {
			return false;
		}
		
		try {
			Optional<Voucher> voucherOpt = voucherRepository.findByMaGiamGia(voucherCode.trim());
			if (voucherOpt.isPresent()) {
				Voucher voucher = voucherOpt.get();
				
				// Kiểm tra voucher còn hạn sử dụng không
				if (voucher.getNgayHetHan() != null && voucher.getNgayHetHan().before(new java.util.Date())) {
					logger.warn("Voucher đã hết hạn: {}", voucherCode);
					return false;
				}
				
				return true;
			}
		} catch (Exception e) {
			logger.error("Lỗi khi kiểm tra voucher {}: {}", voucherCode, e.getMessage());
		}
		
		return false;
	}
	
	@Override
	public BigDecimal calculatePriceWithVoucher(BigDecimal originalPrice, String voucherCode) {
		if (originalPrice == null || voucherCode == null || voucherCode.trim().isEmpty()) {
			return originalPrice;
		}

		try {
			Optional<Voucher> voucherOpt = voucherRepository.findByMaGiamGia(voucherCode.trim());
			if (voucherOpt.isEmpty()) {
				logger.warn("Voucher không tồn tại: {}", voucherCode);
				return originalPrice;
			}

			Voucher voucher = voucherOpt.get();

			// Kiểm tra voucher còn hạn không
			if (voucher.getNgayHetHan() != null && voucher.getNgayHetHan().before(new java.util.Date())) {
				logger.warn("Voucher đã hết hạn: {}", voucherCode);
				return originalPrice;
			}

			BigDecimal discountAmount;
			if (voucher.getGiaTri().compareTo(BigDecimal.valueOf(100)) <= 0) {
				// Giảm giá theo phần trăm
				discountAmount = originalPrice.multiply(voucher.getGiaTri()).divide(BigDecimal.valueOf(100));
			} else {
				// Giảm giá theo số tiền cố định
				discountAmount = voucher.getGiaTri();
			}

			BigDecimal finalPrice = originalPrice.subtract(discountAmount);
			return finalPrice.compareTo(BigDecimal.ZERO) > 0 ? finalPrice : BigDecimal.ZERO;

		} catch (Exception e) {
			logger.error("Lỗi khi tính giá với voucher {}: {}", voucherCode, e.getMessage());
			return originalPrice;
		}
	}

	@Override
	public List<Map<String, Object>> getCompletedBookingsByMonth() {
		try {
			List<Object[]> rawData = bookingRepository.findCompletedBookingsByMonth();
			List<Map<String, Object>> result = new java.util.ArrayList<>();
			
			for (Object[] row : rawData) {
				Map<String, Object> item = new java.util.HashMap<>();
				item.put("month", row[0]);
				item.put("count", row[1]);
				result.add(item);
			}
			
			logger.info("Lấy thống kê booking theo tháng thành công: {} records", result.size());
			return result;
			
		} catch (Exception e) {
			logger.error("Lỗi khi lấy thống kê booking theo tháng: {}", e.getMessage());
			return new java.util.ArrayList<>();
		}
	}

	@Override
	public List<Map<String, Object>> getCompletedBookingsBySeason() {
		try {
			List<Object[]> rawData = bookingRepository.findCompletedBookingsBySeason();
			List<Map<String, Object>> result = new java.util.ArrayList<>();
			
			for (Object[] row : rawData) {
				Map<String, Object> item = new java.util.HashMap<>();
				item.put("seasonName", row[0]);
				item.put("count", row[1]);
				result.add(item);
			}
			
			logger.info("Lấy thống kê booking theo mùa thành công: {} records", result.size());
			return result;
			
		} catch (Exception e) {
			logger.error("Lỗi khi lấy thống kê booking theo mùa: {}", e.getMessage());
			return new java.util.ArrayList<>();
		}
	}
}
