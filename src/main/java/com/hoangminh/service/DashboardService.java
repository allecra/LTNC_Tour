package com.hoangminh.service;

import java.util.List;

public interface DashboardService {
    Long getUserCount();
    Long getTourCount();
    Double getTotalRevenue();
    Double getAverageRating();
    List<Long> getTourCountByMonth(); // 12 tháng
    List<Long> getTourCountBySeason(); // 4 mùa
    
    // Thống kê booking đã hoàn tất
    List<Object[]> getCompletedBookingsByMonth();
    List<Object[]> getCompletedBookingsBySeason();
} 