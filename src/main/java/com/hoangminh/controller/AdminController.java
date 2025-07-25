package com.hoangminh.controller;

import com.hoangminh.service.UserService;
import com.hoangminh.utilities.SessionUtilities;
import jakarta.servlet.annotation.HandlesTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @GetMapping({"/index", "", "/"})
    public String adminIndex() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }
        return "admin/index";
    }

    @GetMapping("/user")
    public String userManage() {

        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }

        return "admin/user";
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

    @GetMapping("/login")
    public String adminLogin() {
        return "admin/login";
    }

    @GetMapping("/tourStart/{id}")
    public String tourStart(@PathVariable("id")Long id) {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }
        return "admin/tourstart";
    }
    @GetMapping("/tourImage/{id}")
    public String tourImage(@PathVariable("id") Long id) {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }
        return "admin/tourImage";
    }

    @GetMapping("/guides")
    public String guidesManage() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }
        return "admin/guides";
    }

    @GetMapping("/review")
    public String reviewManage() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }
        return "admin/review";
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

    @GetMapping("/voucher")
    public String voucherManage() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/admin/login";
        }
        return "admin/voucher";
    }

    @GetMapping("/logout")
    public String adminLogout() {
        this.userService.adminLogout();
        return "redirect:/admin/login";
    }
}
