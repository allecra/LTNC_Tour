package com.hoangminh.service.impl;

import com.hoangminh.repository.*;
import com.hoangminh.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TourRepository tourRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    ReviewRepository reviewRepository;

    @Override
    public Long getUserCount() {
        return userRepository.count();
    }

    @Override
    public Long getTourCount() {
        return tourRepository.count();
    }

    @Override
    public Double getTotalRevenue() {
        Double sum = bookingRepository.sumTotalRevenue();
        return sum != null ? sum : 0.0;
    }

    @Override
    public Double getAverageRating() {
        Double avg = reviewRepository.averageRating();
        return avg != null ? Math.round(avg * 100.0) / 100.0 : 0.0;
    }

    @Override
    public List<Long> getTourCountByMonth() {
        List<Long> result = new ArrayList<>(Collections.nCopies(12, 0L));
        List<Object[]> data = tourRepository.countToursByMonth();
        System.out.println("=== MONTH DATA DEBUG ===");
        System.out.println("Raw data from repository: " + data);
        for (Object[] row : data) {
            Object monthObj = row[0];
            Object countObj = row[1];
            System.out.println("Raw month object: " + monthObj + " (type: " + (monthObj != null ? monthObj.getClass().getSimpleName() : "null") + ")");
            System.out.println("Raw count object: " + countObj + " (type: " + (countObj != null ? countObj.getClass().getSimpleName() : "null") + ")");
            
            Integer month = null;
            Long count = null;
            
            try {
                if (monthObj instanceof Number) {
                    month = ((Number) monthObj).intValue();
                } else if (monthObj instanceof String) {
                    month = Integer.parseInt((String) monthObj);
                }
                
                if (countObj instanceof Number) {
                    count = ((Number) countObj).longValue();
                } else if (countObj instanceof String) {
                    count = Long.parseLong((String) countObj);
                }
                
                System.out.println("Converted month: " + month + " (type: " + (month != null ? month.getClass().getSimpleName() : "null") + ")");
                System.out.println("Converted count: " + count + " (type: " + (count != null ? count.getClass().getSimpleName() : "null") + ")");
                
                if (month != null && month >= 1 && month <= 12) {
                    result.set(month - 1, count != null ? count : 0L);
                    System.out.println("Set result[" + (month - 1) + "] = " + count);
                } else {
                    System.out.println("Invalid month: " + month);
                }
            } catch (Exception e) {
                System.out.println("Error converting data: " + e.getMessage());
            }
        }
        System.out.println("Final month result: " + result);
        System.out.println("=== END MONTH DEBUG ===");
        return result;
    }

    @Override
    public List<Long> getTourCountBySeason() {
        List<Long> result = new ArrayList<>(Arrays.asList(0L, 0L, 0L, 0L));
        List<Object[]> data = tourRepository.countToursBySeason();
        System.out.println("=== SEASON DATA DEBUG ===");
        System.out.println("Raw data from repository: " + data);
        
        // Mapping từ tên mùa trong database (không dấu) sang index trong array
        // Database: "Xuan", "Ha", "Thu", "Dong" 
        // Frontend labels: "Xuân", "Hạ", "Thu", "Đông"
        Map<String, Integer> seasonIndex = Map.of(
            "Xuan", 0, "Ha", 1, "Thu", 2, "Dong", 3
        );
        
        for (Object[] row : data) {
            Object seasonObj = row[0];
            Object countObj = row[1];
            System.out.println("Raw season object: " + seasonObj + " (type: " + (seasonObj != null ? seasonObj.getClass().getSimpleName() : "null") + ")");
            System.out.println("Raw count object: " + countObj + " (type: " + (countObj != null ? countObj.getClass().getSimpleName() : "null") + ")");
            
            String season = null;
            Long count = null;
            
            try {
                if (seasonObj instanceof String) {
                    season = (String) seasonObj;
                } else if (seasonObj != null) {
                    season = seasonObj.toString();
                }
                
                if (countObj instanceof Number) {
                    count = ((Number) countObj).longValue();
                } else if (countObj instanceof String) {
                    count = Long.parseLong((String) countObj);
                }
                
                System.out.println("Converted season: " + season + " (type: " + (season != null ? season.getClass().getSimpleName() : "null") + ")");
                System.out.println("Converted count: " + count + " (type: " + (count != null ? count.getClass().getSimpleName() : "null") + ")");
                
                Integer idx = seasonIndex.get(season);
                if (idx != null) {
                    result.set(idx, count != null ? count : 0L);
                    System.out.println("Set result[" + idx + "] = " + count);
                } else {
                    System.out.println("Unknown season: " + season);
                }
            } catch (Exception e) {
                System.out.println("Error converting season data: " + e.getMessage());
            }
        }
        System.out.println("Final season result: " + result);
        System.out.println("=== END SEASON DEBUG ===");
        return result;
    }
} 