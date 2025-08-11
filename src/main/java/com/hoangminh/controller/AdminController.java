package com.hoangminh.controller;

import com.hoangminh.service.DashboardService;
import com.hoangminh.service.UserService;
import com.hoangminh.utilities.SessionUtilities;
import jakarta.servlet.annotation.HandlesTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    DashboardService dashboardService;

    @GetMapping({"/index", "", "/"})
    public String adminIndex(Model model) {
        // Tạm thời bỏ qua kiểm tra admin để test
        // if(!this.userService.checkAdminLogin()) {
        //     return "redirect:/account";
        // }
        
        // Chỉ giữ lại các thuộc tính cơ bản, dữ liệu sẽ được load qua API
        model.addAttribute("userCount", dashboardService.getUserCount());
        model.addAttribute("tourCount", dashboardService.getTourCount());
        
        // Format tổng doanh thu
        Double totalRevenue = dashboardService.getTotalRevenue();
        String formattedRevenue = String.format("%,.0f VNĐ", totalRevenue);
        model.addAttribute("totalRevenue", formattedRevenue);
        
        // Format đánh giá trung bình
        Double averageRating = dashboardService.getAverageRating();
        String formattedRating = String.format("%.1f/5.0", averageRating);
        model.addAttribute("averageRating", formattedRating);
        
        // Thêm dữ liệu cho biểu đồ
        List<String> monthLabels = List.of(
            "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
            "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
        );
        List<Long> monthData = dashboardService.getTourCountByMonth();
        List<String> seasonLabels = List.of("Xuân", "Hạ", "Thu", "Đông");
        List<Long> seasonData = dashboardService.getTourCountBySeason();
        
        System.out.println("=== CONTROLLER DEBUG ===");
        System.out.println("Month labels: " + monthLabels);
        System.out.println("Month data: " + monthData);
        System.out.println("Season labels: " + seasonLabels);
        System.out.println("Season data: " + seasonData);
        System.out.println("=== END CONTROLLER DEBUG ===");
        
        model.addAttribute("monthLabels", monthLabels);
        model.addAttribute("monthData", monthData);
        model.addAttribute("seasonLabels", seasonLabels);
        model.addAttribute("seasonData", seasonData);
        
        return "admin/index";
    }

    @GetMapping("/user")
    public String userManage() {

        if(!this.userService.checkAdminLogin()) {
            return "redirect:/account";
        }

        return "admin/user";
    }

    @GetMapping("/tour")
    public String tourManage() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/account";
        }
        return "admin/tour";
    }

    @GetMapping("/booking")
    public String bookingManager() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/account";
        }
        return "admin/booking";
    }

    @GetMapping("/tourStart/{id}")
    public String tourStart(@PathVariable("id")Long id) {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/account";
        }
        return "admin/tourstart";
    }
    @GetMapping("/tourImage/{id}")
    public String tourImage(@PathVariable("id") Long id) {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/account";
        }
        return "admin/tourImage";
    }

    @GetMapping("/guides")
    public String guidesManage() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/account";
        }
        return "admin/guides";
    }

    @GetMapping("/review")
    public String reviewManage() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/account";
        }
        return "admin/review";
    }

    @GetMapping("/payment-method")
    public String paymentMethodManage() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/account";
        }
        return "admin/payment-method";
    }

    @GetMapping("/transaction-history")
    public String transactionHistoryManage() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/account";
        }
        return "admin/transaction-history";
    }

    @GetMapping("/voucher")
    public String voucherManage() {
        if(!this.userService.checkAdminLogin()) {
            return "redirect:/account";
        }
        return "admin/voucher";
    }

    @GetMapping("/test-stats")
    public String testStats(Model model) {
        System.out.println("=== TEST STATS DEBUG ===");
        
        // Test từng service riêng biệt
        System.out.println("User count: " + dashboardService.getUserCount());
        System.out.println("Tour count: " + dashboardService.getTourCount());
        System.out.println("Total revenue: " + dashboardService.getTotalRevenue());
        System.out.println("Average rating: " + dashboardService.getAverageRating());
        System.out.println("Tour by month: " + dashboardService.getTourCountByMonth());
        System.out.println("Tour by season: " + dashboardService.getTourCountBySeason());
        
        System.out.println("=== END TEST STATS DEBUG ===");
        
        return "redirect:/admin/index";
    }

    @GetMapping("/debug-data")
    public String debugData(Model model) {
        System.out.println("=== DEBUG DATA ===");
        
        // Kiểm tra dữ liệu trực tiếp từ repository
        try {
            // Test tour count
            Long tourCount = dashboardService.getTourCount();
            System.out.println("Tour count: " + tourCount);
            
            // Test month data
            List<Long> monthData = dashboardService.getTourCountByMonth();
            System.out.println("Month data: " + monthData);
            
            // Test season data  
            List<Long> seasonData = dashboardService.getTourCountBySeason();
            System.out.println("Season data: " + seasonData);
            
        } catch (Exception e) {
            System.out.println("Error in debug: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== END DEBUG DATA ===");
        return "redirect:/admin/index";
    }

    @GetMapping("/api/stats")
    @ResponseBody
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            stats.put("userCount", dashboardService.getUserCount());
            stats.put("tourCount", dashboardService.getTourCount());
            stats.put("totalRevenue", dashboardService.getTotalRevenue());
            stats.put("averageRating", dashboardService.getAverageRating());
            stats.put("tourByMonth", dashboardService.getTourCountByMonth());
            stats.put("tourBySeason", dashboardService.getTourCountBySeason());
            
            System.out.println("=== API STATS DEBUG ===");
            System.out.println("Stats data: " + stats);
            System.out.println("=== END API STATS DEBUG ===");
            
        } catch (Exception e) {
            System.out.println("Error getting stats: " + e.getMessage());
            e.printStackTrace();
            stats.put("error", e.getMessage());
        }
        
        return stats;
    }

    @GetMapping("/api/booking/completed-by-month")
    @ResponseBody
    public Map<String, Object> getCompletedBookingsByMonth() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Object[]> data = dashboardService.getCompletedBookingsByMonth();
            response.put("success", true);
            response.put("message", "Lấy thống kê booking theo tháng thành công");
            response.put("data", data);
            
            System.out.println("=== COMPLETED BOOKINGS BY MONTH API ===");
            System.out.println("Response: " + response);
            System.out.println("=== END COMPLETED BOOKINGS BY MONTH API ===");
            
        } catch (Exception e) {
            System.out.println("Error in completed-by-month API: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            response.put("data", new ArrayList<>());
        }
        
        return response;
    }

    @GetMapping("/api/booking/completed-by-season")
    @ResponseBody
    public Map<String, Object> getCompletedBookingsBySeason() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Object[]> data = dashboardService.getCompletedBookingsBySeason();
            response.put("success", true);
            response.put("message", "Lấy thống kê booking theo mùa thành công");
            response.put("data", data);
            
            System.out.println("=== COMPLETED BOOKINGS BY SEASON API ===");
            System.out.println("Response: " + response);
            System.out.println("=== END COMPLETED BOOKINGS BY SEASON API ===");
            
        } catch (Exception e) {
            System.out.println("Error in completed-by-season API: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            response.put("data", new ArrayList<>());
        }
        
        return response;
    }

    @GetMapping("/logout")
    public String adminLogout() {
        this.userService.adminLogout();
        return "redirect:/account";
    }
}
