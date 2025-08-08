package com.hoangminh.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.hoangminh.dto.*;
import com.hoangminh.service.BookingService;
import com.hoangminh.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.hoangminh.entity.Image;
import com.hoangminh.entity.Tour;
import com.hoangminh.repository.ImageRepository;
import com.hoangminh.repository.TourStartRepository;
import com.hoangminh.service.ImageService;
import com.hoangminh.service.TourService;
import com.hoangminh.service.UserService;
import com.hoangminh.utilities.SessionUtilities;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import com.hoangminh.entity.TourStart;

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
	private TransactionService transactionService;

	@Autowired
	private HttpServletRequest request;

	@GetMapping("")
	ModelAndView index() {
		ModelAndView mdv = new ModelAndView("user/index");

		Page<TourDTO> tourPage = this.tourService.findAllTour(null, null, null, null, PageRequest.of(0, 6));

		List<TourDTO> tours = tourPage.getContent();

		mdv.addObject("tours", tours);

		return mdv;
	}

	@GetMapping("/tour/trong-nuoc")
	ModelAndView tourTrongNuoc(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageIndex,
			@RequestParam(value = "ten_tour", required = false) String ten_tour,
			@RequestParam(value = "gia_tour", required = false) Long gia_tour) {
		ModelAndView mdv = new ModelAndView("user/tour1");
		BigDecimal gia_tour_from = null;
		BigDecimal gia_tour_to = null;
		if (gia_tour != null) {
			gia_tour_from = gia_tour == 0 ? null
					: (gia_tour == 1 ? BigDecimal.valueOf(0)
							: (gia_tour == 2 ? BigDecimal.valueOf(10000000) : BigDecimal.valueOf(50000000)));

			gia_tour_to = gia_tour == 0 ? null
					: (gia_tour == 1 ? BigDecimal.valueOf(10000000)
							: (gia_tour == 2 ? BigDecimal.valueOf(50000000) : BigDecimal.valueOf(500000000)));
		}

		Page<TourDTO> tourPage = this.tourService.findAllTour(ten_tour, gia_tour_from, gia_tour_to,
				1L, PageRequest.of(pageIndex - 1, 12));

		List<TourDTO> tours = tourPage.getContent();

		mdv.addObject("tours", tours);
		return mdv;
	}

	@GetMapping("/tour/ngoai-nuoc")
	ModelAndView tourNgoaiNuoc(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageIndex,
			@RequestParam(value = "ten_tour", required = false) String ten_tour,
			@RequestParam(value = "gia_tour", required = false) Long gia_tour) {

		BigDecimal gia_tour_from = null;
		BigDecimal gia_tour_to = null;
		if (gia_tour != null) {
			gia_tour_from = gia_tour == 0 ? null
					: (gia_tour == 1 ? BigDecimal.valueOf(0)
							: (gia_tour == 2 ? BigDecimal.valueOf(10000000) : BigDecimal.valueOf(50000000)));

			gia_tour_to = gia_tour == 0 ? null
					: (gia_tour == 1 ? BigDecimal.valueOf(10000000)
							: (gia_tour == 2 ? BigDecimal.valueOf(50000000) : BigDecimal.valueOf(500000000)));
		}

		ModelAndView mdv = new ModelAndView("user/tour2");

		Page<TourDTO> tourPage = this.tourService.findAllTour(ten_tour, gia_tour_from, gia_tour_to, 2L,
				PageRequest.of(pageIndex - 1, 12));

		List<TourDTO> tours = tourPage.getContent();

		mdv.addObject("tours", tours);
		return mdv;
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

		return mdv;
	}

	@GetMapping("/login")
	ModelAndView login() {
		if (this.userService.checkLogin()) {
			return this.account();
		}
		ModelAndView mdv = new ModelAndView("user/login");

		return mdv;
	}

	@GetMapping("/register")
	ModelAndView register() {

		if (this.userService.checkLogin()) {
			return this.account();
		}

		ModelAndView mdv = new ModelAndView("user/register");

		return mdv;
	}

	@PostMapping("/login")
	ModelAndView loginAction(LoginDTO login) {

		if (!this.userService.login(login)) {
			ModelAndView mdvErr = new ModelAndView("user/login");
			mdvErr.addObject("err", "Sai thông tin đăng nhập");
			return mdvErr;
		}

		// Kiểm tra nếu là admin thì chuyển sang trang quản lý
		com.hoangminh.dto.UserDTO user = com.hoangminh.utilities.SessionUtilities.getUser();
		if (user != null && user.getRole_id() == 1) {
			// Set session admin để các trang admin nhận diện
			com.hoangminh.utilities.SessionUtilities.setAdmin(user);
			return new ModelAndView("redirect:/admin/index");
		}

		// Nếu là user thường
		ModelAndView mdv = new ModelAndView("redirect:/account");
		mdv.addObject("user", user);
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
		com.hoangminh.utilities.SessionUtilities.setUser(null);
		com.hoangminh.utilities.SessionUtilities.setUsername(null);
		com.hoangminh.utilities.SessionUtilities.setAdmin(null);
		return new ModelAndView("redirect:/account"); // luôn về trang tài khoản user
	}

	@GetMapping("/account")
	ModelAndView account() {
		ModelAndView mdv = new ModelAndView();

		if (SessionUtilities.getUsername() == null) {
			// Thay vì redirect, hiển thị trang account với form đăng nhập
			mdv.setViewName("user/account");
			mdv.addObject("user", null); // Để hiển thị form đăng nhập
			return mdv;
		}

		mdv.setViewName("user/account");
		mdv.addObject("user", SessionUtilities.getUser());

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

		List<BookingDTOWithTransaction> bookingList = this.transactionService.findBookingWithTransactionByUserId(SessionUtilities.getUser().getId());

		mdv.addObject("bookingList", bookingList);

		return mdv;

	}

	@GetMapping("/user/booking/{id}")
	ModelAndView userBookingDetai(@PathVariable Long id) {

		if (!this.userService.checkLogin()) {
			return new ModelAndView("redirect:/login");
		}

		ModelAndView mdv = new ModelAndView();

		BookingDTOWithTransaction booking = this.transactionService.findBookingWithTransactionById(id);

		if (booking != null) {
			mdv.setViewName("user/user-booking");
			mdv.addObject("booking", booking);
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
	public String about() {
		return "/user/about";
	}

	@GetMapping("/tin-tuc")
	public String news() {
		return "/user/tin-tuc";
	}

	@GetMapping("/guide")
	public String guide() {
		return "/user/guide";
	}

	@GetMapping("/contact")
	public String contact() {
		return "/user/contact";
	}

}
