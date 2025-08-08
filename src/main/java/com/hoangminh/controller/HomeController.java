package com.hoangminh.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.hoangminh.dto.*;
import com.hoangminh.entity.Destination;
import com.hoangminh.entity.Image;
import com.hoangminh.service.BookingService;
import com.hoangminh.service.DestinationService;
import com.hoangminh.repository.TourStartRepository;
import com.hoangminh.service.ImageService;
import com.hoangminh.service.TourService;
import com.hoangminh.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.hoangminh.utilities.SessionUtilities;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import com.hoangminh.entity.TourStart;
import com.hoangminh.entity.TinTuc;
import com.hoangminh.service.TinTucService;
import com.hoangminh.service.ReviewService;
import com.hoangminh.service.VoucherService;
import com.hoangminh.service.UserVoucherService;

@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {

	@Autowired
	public TourService tourService;

	@Autowired
	private TourStartRepository tourStartRepository;

	@Autowired
	private ImageService imageService;

	@Autowired
	private UserService userService;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private DestinationService destinationService;

	@Autowired
	private TinTucService tinTucService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private VoucherService voucherService;

	@Autowired
	private UserVoucherService userVoucherService;

	@Autowired
	private HttpServletRequest request;

	@GetMapping("")
	ModelAndView index() {
		ModelAndView mdv = new ModelAndView("user/index");

		// Lấy tour nổi bật
		Page<TourDTO> tourPage = this.tourService.findAllTour(null, null, null, null, PageRequest.of(0, 6));
		List<TourDTO> tours = tourPage.getContent();

		// Lấy tour trong nước (type_id = 1) - tất cả tour trong nước
		Page<TourDTO> domesticToursPage = this.tourService.findAllTour(null, null, null, 1L, PageRequest.of(0, 12));
		List<TourDTO> domesticTours = domesticToursPage.getContent();

		// Lấy tour ngoài nước (type_id = 2) - tất cả tour ngoài nước
		Page<TourDTO> internationalToursPage = this.tourService.findAllTour(null, null, null, 2L,
				PageRequest.of(0, 12));
		List<TourDTO> internationalTours = internationalToursPage.getContent();

		// Lấy tour trong nước theo mùa (chia đôi danh sách)
		List<TourDTO> domesticToursBySeason = domesticTours.size() > 6 ? domesticTours.subList(0, 6) : domesticTours;
		List<TourDTO> domesticToursByMonth = domesticTours.size() > 6
				? domesticTours.subList(6, Math.min(12, domesticTours.size()))
				: new ArrayList<>();

		// Lấy tour ngoài nước theo mùa (chia đôi danh sách)
		List<TourDTO> internationalToursBySeason = internationalTours.size() > 6 ? internationalTours.subList(0, 6)
				: internationalTours;
		List<TourDTO> internationalToursByMonth = internationalTours.size() > 6
				? internationalTours.subList(6, Math.min(12, internationalTours.size()))
				: new ArrayList<>();

		List<Destination> destinations = this.destinationService.findAllDestinations();
		List<VoucherDTO> activeVouchers = this.voucherService.getActiveVouchers();
		List<ReviewDTO> featuredReviews = this.reviewService.getAllApprovedReviews().stream()
				.limit(3)
				.collect(Collectors.toList());

		// Lấy user từ session
		try {
			UserDTO user = SessionUtilities.getUser();
			mdv.addObject("user", user);
		} catch (Exception e) {
			mdv.addObject("user", null);
		}

		mdv.addObject("tours", tours);
		mdv.addObject("domesticTours", domesticTours);
		mdv.addObject("internationalTours", internationalTours);
		mdv.addObject("domesticToursBySeason", domesticToursBySeason);
		mdv.addObject("domesticToursByMonth", domesticToursByMonth);
		mdv.addObject("internationalToursBySeason", internationalToursBySeason);
		mdv.addObject("internationalToursByMonth", internationalToursByMonth);
		mdv.addObject("destinations", destinations);
		mdv.addObject("activeVouchers", activeVouchers);
		mdv.addObject("featuredReviews", featuredReviews);
		mdv.addObject("origins", this.tourService.findAllUniqueOrigins());
		mdv.addObject("availableDates", tourStartRepository.findAllAvailableDates());
		mdv.addObject("active", "home");

		return mdv;
	}

	@GetMapping("/search")
	ModelAndView searchTour(
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "destination", required = false) String destination,
			@RequestParam(value = "origin", required = false) String origin,
			@RequestParam(value = "departureDate", required = false) String departureDateStr,
			@RequestParam(value = "price", required = false) BigDecimal price) {

		ModelAndView mdv = new ModelAndView("user/search-results");

		try {
			// Parse ngày khởi hành nếu có
			Date departureDate = null;
			if (departureDateStr != null && !departureDateStr.trim().isEmpty()) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					sdf.setLenient(false); // Strict date parsing
					departureDate = sdf.parse(departureDateStr);
				} catch (Exception e) {
					log.error("Error parsing date: " + departureDateStr, e);
				}
			}

			try {
				// Tìm kiếm tour với các bộ lọc
				Page<TourDTO> searchResults = tourService.findToursByFilters(
						keyword,
						destination,
						origin,
						departureDate,
						price,
						PageRequest.of(0, 12));

				if (searchResults != null) {
					mdv.addObject("searchResults", searchResults.getContent());
				} else {
					mdv.addObject("searchResults", new ArrayList<>());
				}

				// Thêm các tham số tìm kiếm vào model
				mdv.addObject("keyword", keyword);
				mdv.addObject("destination", destination);
				mdv.addObject("origin", origin);
				mdv.addObject("departureDate", departureDateStr);
				mdv.addObject("price", price);

				// Thêm danh sách điểm đến và điểm khởi hành
				mdv.addObject("destinations", destinationService.findAllDestinations());
				mdv.addObject("origins", tourService.findAllUniqueOrigins());
				mdv.addObject("availableDates", tourStartRepository.findAllAvailableDates());
			} catch (Exception e) {
				log.error("Error during search: ", e);
				mdv.addObject("searchResults", new ArrayList<>());
				mdv.addObject("error", "Có lỗi xảy ra trong quá trình tìm kiếm. Vui lòng thử lại.");
			}

			// Lấy danh sách ngày khởi hành có sẵn
			List<Date> availableDates = tourStartRepository.findAllAvailableDates();
			mdv.addObject("availableDates", availableDates);

			// Add user info for dropdown
			try {
				UserDTO user = SessionUtilities.getUser();
				mdv.addObject("user", user);
			} catch (Exception e) {
				mdv.addObject("user", null);
			}

			return mdv;
		} catch (Exception e) {
			// Log lỗi và chuyển hướng về trang chủ với thông báo lỗi
			log.error("Error searching tours: ", e);
			return new ModelAndView("redirect:/#search-error");
		}
	}

	@GetMapping("/tour/trong-nuoc")
	ModelAndView tourTrongNuoc(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageIndex,
			@RequestParam(value = "ten_tour", required = false) String ten_tour,
			@RequestParam(value = "gia_tour", required = false) Long gia_tour) {
		// Redirect về trang chủ với anchor để scroll đến section tour trong nước
		return new ModelAndView("redirect:/#domestic-tours");
	}

	@GetMapping("/tour/ngoai-nuoc")
	ModelAndView tourNgoaiNuoc(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageIndex,
			@RequestParam(value = "ten_tour", required = false) String ten_tour,
			@RequestParam(value = "gia_tour", required = false) Long gia_tour) {
		// Redirect về trang chủ với anchor để scroll đến section tour ngoài nước
		return new ModelAndView("redirect:/#international-tours");
	}

	@GetMapping("/tour/{id}")
	ModelAndView tourDetail(@PathVariable(name = "id", required = true) Long id) {
		ModelAndView mdv = new ModelAndView("user/tour-detail");

		TourDTO tour = this.tourService.findTourById(id);
		List<Image> imageList = this.imageService.findByTourId(id);

		List<TourStart> listDate = this.tourStartRepository.findByTourId(id);

		mdv.addObject("tour", tour);
		mdv.addObject("listDate", listDate);
		mdv.addObject("imageList", imageList);

		// Add user info for dropdown
		try {
			UserDTO user = SessionUtilities.getUser();
			mdv.addObject("user", user);
		} catch (Exception e) {
			mdv.addObject("user", null);
		}

		return mdv;
	}

	@GetMapping("/login")
	ModelAndView login() {
		if (SessionUtilities.getUsername() != null && SessionUtilities.getUser() != null) {
			return this.account();
		}
		ModelAndView mdv = new ModelAndView("user/login");
		mdv.addObject("active", "login");

		// Add user info for dropdown
		try {
			UserDTO user = SessionUtilities.getUser();
			mdv.addObject("user", user);
		} catch (Exception e) {
			mdv.addObject("user", null);
		}

		return mdv;
	}

	@GetMapping("/register")
	ModelAndView register() {

		if (SessionUtilities.getUsername() != null && SessionUtilities.getUser() != null) {
			return this.account();
		}

		ModelAndView mdv = new ModelAndView("user/register");
		mdv.addObject("active", "register");

		// Add user info for dropdown
		try {
			UserDTO user = SessionUtilities.getUser();
			mdv.addObject("user", user);
		} catch (Exception e) {
			mdv.addObject("user", null);
		}

		return mdv;
	}

	@PostMapping("/login")
	ModelAndView loginAction(LoginDTO login) {

		ModelAndView mdv = new ModelAndView("redirect:/account");

		if (!this.userService.login(login)) {
			ModelAndView mdvErr = new ModelAndView("user/login");
			mdvErr.addObject("err", "Sai thông tin đăng nhập");
			return mdvErr;
		}

		mdv.addObject("user", SessionUtilities.getUser());

		return mdv;
	}

	@PostMapping("/register")
	ModelAndView registerAction(RegisterDTO user) {

		ModelAndView mdv = new ModelAndView("user/login");
		String error = this.userService.getRegisterError(user);
		if (error != null) {
			ModelAndView mdvErr = new ModelAndView("user/register");
			mdvErr.addObject("err", error);
			return mdvErr;
		}
		mdv.addObject("message", "Đăng ký thành công vui lòng đăng nhập");
		return mdv;
	}

	@GetMapping("/logout")
	ModelAndView logout() {
		SessionUtilities.setUser(null);
		SessionUtilities.setUsername(null);
		return this.index();
	}

	@GetMapping("/account")
	ModelAndView account() {
		ModelAndView mdv = new ModelAndView();

		UserDTO user = SessionUtilities.getUser();
		if (user == null || SessionUtilities.getUsername() == null) {
			ModelAndView loginView = new ModelAndView("redirect:/login");
			return loginView;
		}

		mdv.setViewName("user/account");
		mdv.addObject("user", user);
		mdv.addObject("active", "account");

		return mdv;
	}

	@GetMapping("/changepassword")
	ModelAndView changePassword(ChangePasswordDTO changePasswordDTO) {

		ModelAndView mdv = new ModelAndView();
		if (!this.userService.checkLogin()) {
			mdv.setViewName("redirect:/login");
			return mdv;
		}
		mdv.setViewName("user/changepassword");
		mdv.addObject("active", "account");

		// Add user info for dropdown
		try {
			UserDTO user = SessionUtilities.getUser();
			mdv.addObject("user", user);
		} catch (Exception e) {
			mdv.addObject("user", null);
		}

		return mdv;

	}

	@PostMapping("/changePassword")
	ModelAndView changePasswordAction(ChangePasswordDTO changePasswordDTO) {

		if (changePasswordDTO.getNewPassword() != null && changePasswordDTO.getOldPassword() != null) {
			if (this.userService.changePassword(changePasswordDTO)) {
				ModelAndView accountView = this.account();
				accountView.addObject("message", "Thay đổi mật khẩu thành công");
				return accountView;
			}
		}

		ModelAndView mdv = new ModelAndView("user/changepassword");
		mdv.addObject("err", "Mật khẩu cũ không đúng");

		return mdv;
	}

	@PostMapping("/updateAccount")
	ModelAndView updateAccountAction(UpdateUserDTO updateUserDTO) {

		log.info("update account:{}", updateUserDTO);

		if (this.userService.updateUser(updateUserDTO)) {
			return this.account().addObject("message", "Cập nhật thành công!");
		} else {
			return this.account().addObject("message", "Có lỗi xảy ra!");
		}

	}

	@GetMapping("/tour/booking")
	ModelAndView booking(@RequestParam("id") Long tour_start_id,
			@RequestParam Integer so_nguoi,
			@RequestParam(value = "tong_tien", required = false) Long tong_tien) {

		ModelAndView mdv = new ModelAndView("user/booking-checkout");

		if (tour_start_id == null || so_nguoi == null) {
			return new ModelAndView("redirect:/err");
		}

		if (tong_tien == null)
			tong_tien = 0L;

		if (SessionUtilities.getUsername() == null) {
			ModelAndView loginView = new ModelAndView("redirect:/login");
			return loginView;
		}

		TourStart tourStart = this.tourStartRepository.findById(tour_start_id).get();
		if (tourStart == null) {
			return new ModelAndView("redirect:/error");
		}

		mdv.addObject("user", SessionUtilities.getUser());
		TourDTO tour = this.tourService.findTourById(tourStart.getTour().getId());
		mdv.addObject("tour", tour);
		mdv.addObject("tourStart", tourStart);
		mdv.addObject("so_nguoi", so_nguoi);
		mdv.addObject("tong_tien", tong_tien);

		return mdv;

	}

	@PostMapping(value = "/tour/booking/{tour_start_id}")
	ModelAndView bookingAction(@PathVariable("tour_start_id") Long tour_start_id,
			@RequestParam("so_luong_nguoi") Integer so_luong_nguoi,
			@RequestParam("ghi_chu") String ghi_chu,
			@RequestParam("payment_method") String payment_method) {

		ModelAndView mdv = new ModelAndView();

		BookingDTO bookingDTO = new BookingDTO();

		bookingDTO.setSo_luong_nguoi(so_luong_nguoi);
		bookingDTO.setGhi_chu(ghi_chu);
		bookingDTO.setPayment_method(payment_method);
		bookingDTO.setTour_id(tour_start_id);
		bookingDTO.setUser_id(SessionUtilities.getUser().getId());

		if (this.bookingService.addNewBooking(bookingDTO)) {
			mdv.setViewName("redirect:/account");
			return mdv;
		}

		mdv.setViewName("redirect:/error");
		return mdv;
	}

	@GetMapping("/error")
	public String error() {
		return "user/error";
	}

	@GetMapping("/user/tour")
	ModelAndView userTour() {

		if (!this.userService.checkLogin()) {
			return new ModelAndView("redirect:/login");
		}

		ModelAndView mdv = new ModelAndView("user/user-booking-list");

		List<BookingDTO> bookingList = this.bookingService.findBookingByUserId(SessionUtilities.getUser().getId());

		mdv.addObject("bookingList", bookingList);

		// Add user info for dropdown
		try {
			UserDTO user = SessionUtilities.getUser();
			mdv.addObject("user", user);
		} catch (Exception e) {
			mdv.addObject("user", null);
		}

		return mdv;

	}

	@GetMapping("/user/booking/{id}")
	ModelAndView userBookingDetai(@PathVariable Long id) {

		if (!this.userService.checkLogin()) {
			return new ModelAndView("redirect:/login");
		}

		ModelAndView mdv = new ModelAndView();

		BookingDTO booking = this.bookingService.getBookingById(id);

		if (booking != null) {
			mdv.setViewName("user/user-booking");
			mdv.addObject("booking", booking);

			// Add user info for dropdown
			try {
				UserDTO user = SessionUtilities.getUser();
				mdv.addObject("user", user);
			} catch (Exception e) {
				mdv.addObject("user", null);
			}

			return mdv;
		}

		mdv.setViewName("redirect:/error");
		return mdv;
	}

	@GetMapping("/user/booking/cancel/{id}")
	ModelAndView cancelTour(@PathVariable Long id) {

		if (id == null) {
			return new ModelAndView("redirect:/error");
		}

		String referrer = request.getHeader("Referer");

		log.info("Url trước đó : {}", referrer);

		BookingDTO bookingDTO = this.bookingService.getBookingById(id);

		if (bookingDTO != null) {

			if (bookingDTO.getTrang_thai().equals("cho_xac_nhan") || bookingDTO.getTrang_thai().equals("da_xac_nhan")) {
				this.bookingService.approveBooking(id, "huy");
			}

		} else {
			return new ModelAndView("redirect:/error");
		}

		return new ModelAndView("redirect:/user/tour");
	}

	@GetMapping("/about")
	public ModelAndView about() {
		ModelAndView mdv = new ModelAndView("user/about");
		mdv.addObject("active", "about");

		// Add user info for dropdown
		try {
			UserDTO user = SessionUtilities.getUser();
			mdv.addObject("user", user);
		} catch (Exception e) {
			mdv.addObject("user", null);
		}

		return mdv;
	}

	@GetMapping("/tin-tuc")
	ModelAndView news() {
		ModelAndView mdv = new ModelAndView("user/tin-tuc");

		// Lấy tất cả tin tức đã đăng
		List<TinTuc> tinTucList = this.tinTucService.getAllPage(PageRequest.of(0, 10)).getContent();

		mdv.addObject("tinTucList", tinTucList);
		mdv.addObject("active", "news");

		// Add user info for dropdown
		try {
			UserDTO user = SessionUtilities.getUser();
			mdv.addObject("user", user);
		} catch (Exception e) {
			mdv.addObject("user", null);
		}

		return mdv;
	}

	@GetMapping("/contact")
	public ModelAndView contact() {
		ModelAndView mdv = new ModelAndView("user/contact");
		mdv.addObject("active", "contact");

		// Add user info for dropdown
		try {
			UserDTO user = SessionUtilities.getUser();
			mdv.addObject("user", user);
		} catch (Exception e) {
			mdv.addObject("user", null);
		}

		return mdv;
	}

	@GetMapping("/testimonial")
	ModelAndView testimonial() {
		ModelAndView mdv = new ModelAndView("user/testimonial");

		// Lấy tất cả review đã được approve
		List<ReviewDTO> reviewList = this.reviewService.getAllApprovedReviews();

		mdv.addObject("reviewList", reviewList);
		mdv.addObject("active", "testimonial");

		// Add user info for dropdown
		try {
			UserDTO user = SessionUtilities.getUser();
			mdv.addObject("user", user);
		} catch (Exception e) {
			mdv.addObject("user", null);
		}

		return mdv;
	}

	@GetMapping("/blog")
	ModelAndView blog() {
		ModelAndView mdv = new ModelAndView("user/blog");

		// Lấy tất cả tin tức đã đăng cho trang blog
		List<TinTuc> blogList = this.tinTucService.getAllPage(PageRequest.of(0, 12)).getContent();

		mdv.addObject("blogList", blogList);
		mdv.addObject("active", "blog");

		// Add user info for dropdown
		try {
			UserDTO user = SessionUtilities.getUser();
			mdv.addObject("user", user);
		} catch (Exception e) {
			mdv.addObject("user", null);
		}

		return mdv;
	}

	@GetMapping("/voucher")
	ModelAndView voucher() {
		ModelAndView mdv = new ModelAndView("user/voucher");

		if (!this.userService.checkLogin()) {
			ModelAndView loginView = new ModelAndView("redirect:/login");
			return loginView;
		}

		try {
			Long userId = SessionUtilities.getUser().getId();
			List<VoucherDTO> userVouchers = this.userVoucherService.getUserVouchers(userId);
			List<VoucherDTO> availableVouchers = this.voucherService.getActiveVouchers();

			mdv.addObject("userVouchers", userVouchers);
			mdv.addObject("availableVouchers", availableVouchers);
		} catch (Exception e) {
			// Nếu có lỗi, trả về danh sách voucher có sẵn
			List<VoucherDTO> availableVouchers = this.voucherService.getActiveVouchers();
			mdv.addObject("userVouchers", java.util.Collections.emptyList());
			mdv.addObject("availableVouchers", availableVouchers);
		}

		mdv.addObject("active", "voucher");

		// Add user info for dropdown
		try {
			UserDTO user = SessionUtilities.getUser();
			mdv.addObject("user", user);
		} catch (Exception e) {
			mdv.addObject("user", null);
		}

		return mdv;
	}

}
