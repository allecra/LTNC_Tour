package com.hoangminh.controller;

import com.hoangminh.service.UserService;
import com.hoangminh.utilities.SessionUtilities;
import jakarta.servlet.annotation.HandlesTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hoangminh.repository.UserRepository;
import com.hoangminh.repository.TourRepository;
import com.hoangminh.repository.ImageRepository;
import com.hoangminh.repository.TourStartRepository;
import org.springframework.ui.Model;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TourRepository tourRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    TourStartRepository tourStartRepository;

    @GetMapping("/user")
    public String userManage() {

        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }

        return "admin/user";
    }

    @GetMapping("/index")
    public String index(Model model) {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }
        long userCount = userRepository.count();
        long tourCount = tourRepository.count();
        long tourImageCount = imageRepository.count();
        long tourStartCount = tourStartRepository.count();
        model.addAttribute("userCount", userCount);
        model.addAttribute("tourCount", tourCount);
        model.addAttribute("tourImageCount", tourImageCount);
        model.addAttribute("tourStartCount", tourStartCount);
        return "admin/index";
    }
    
    @GetMapping("/guides")
    public String guidesManage() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }
        return "admin/guides";
    }

    @GetMapping("/guides/{id}")
    public String guidesDetail(@PathVariable("id") Long id) {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }
        return "admin/guides-detail";
    }

    @GetMapping("/guides/add")
    public String guidesAdd() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }
        return "admin/guides-add";
    }

    @GetMapping("/guides/edit/{id}")
    public String guidesEdit(@PathVariable("id") Long id) {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }
        return "admin/guides-edit";
    }

    @GetMapping("/guides/delete/{id}")
    public String guidesDelete(@PathVariable("id") Long id) {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }
        return "admin/guides-delete";
    }

    @GetMapping("/guides/search")
    public String guidesSearch() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }
        return "admin/guides-search";
    }
    
    @GetMapping("/tour")
    public String tourManage() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }
        return "admin/tour";
    }

    @GetMapping("/booking")
    public String bookingManager() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }
        return "admin/booking";
    }

    @GetMapping("/payment-method")
    public String paymentMethodManage() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }
        return "admin/payment-method";
    }

    @GetMapping("/transaction-history")
    public String transactionHistoryManage() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }
        return "admin/transaction-history";
    }

    @GetMapping("/review")
    public String reviewManage() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }
        return "admin/review";
    }

    @GetMapping("/login")
    public String adminLogin() {
        return "admin/login";
    }

    @GetMapping("/logout")
    public String adminLogout() {
        this.userService.adminLogout();
        return "redirect:/admin/login";
    }
}
