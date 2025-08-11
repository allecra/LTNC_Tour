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
        System.out.println("Data size: " + (data != null ? data.size() : "null"));
        
        if (data == null || data.isEmpty()) {
            System.out.println("No month data found, returning default values");
            return result;
        }
        
        for (Object[] row : data) {
            if (row == null || row.length < 2) {
                System.out.println("Invalid row data: " + Arrays.toString(row));
                continue;
            }
            
            Object monthObj = row[0];
            Object countObj = row[1];
            
            System.out.println("Processing row: month=" + monthObj + ", count=" + countObj);
            
            Integer month = null;
            Long count = null;
            
            try {
                // Xử lý month
                if (monthObj instanceof Number) {
                    month = ((Number) monthObj).intValue();
                } else if (monthObj instanceof String) {
                    month = Integer.parseInt((String) monthObj);
                }
                
                // Xử lý count
                if (countObj instanceof Number) {
                    count = ((Number) countObj).longValue();
                } else if (countObj instanceof String) {
                    count = Long.parseLong((String) countObj);
                }
                
                System.out.println("Converted: month=" + month + ", count=" + count);
                
                if (month != null && month >= 1 && month <= 12) {
                    result.set(month - 1, count != null ? count : 0L);
                    System.out.println("Set result[" + (month - 1) + "] = " + count);
                } else {
                    System.out.println("Invalid month value: " + month);
                }
            } catch (Exception e) {
                System.out.println("Error processing row: " + e.getMessage());
                e.printStackTrace();
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
        System.out.println("Data size: " + (data != null ? data.size() : "null"));
        
        if (data == null || data.isEmpty()) {
            System.out.println("No season data found, returning default values");
            return result;
        }
        
        // Mapping từ tên mùa trong database sang index trong array
        Map<String, Integer> seasonIndex = Map.of(
            "Xuan", 0, "Ha", 1, "Thu", 2, "Dong", 3
        );
        
        for (Object[] row : data) {
            if (row == null || row.length < 2) {
                System.out.println("Invalid row data: " + Arrays.toString(row));
                continue;
            }
            
            Object seasonObj = row[0];
            Object countObj = row[1];
            
            System.out.println("Processing row: season=" + seasonObj + ", count=" + countObj);
            
            String season = null;
            Long count = null;
            
            try {
                // Xử lý season
                if (seasonObj instanceof String) {
                    season = (String) seasonObj;
                } else if (seasonObj != null) {
                    season = seasonObj.toString();
                }
                
                // Xử lý count
                if (countObj instanceof Number) {
                    count = ((Number) countObj).longValue();
                } else if (countObj instanceof String) {
                    count = Long.parseLong((String) countObj);
                }
                
                System.out.println("Converted: season=" + season + ", count=" + count);
                
                Integer idx = seasonIndex.get(season);
                if (idx != null) {
                    result.set(idx, count != null ? count : 0L);
                    System.out.println("Set result[" + idx + "] = " + count);
                } else {
                    System.out.println("Unknown season: " + season + ", available seasons: " + seasonIndex.keySet());
                }
            } catch (Exception e) {
                System.out.println("Error processing season row: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println("Final season result: " + result);
        System.out.println("=== END SEASON DEBUG ===");
        return result;
    }

    @Override
    public List<Object[]> getCompletedBookingsByMonth() {
        try {
            List<Object[]> data = bookingRepository.findCompletedBookingsByMonth();
            System.out.println("=== COMPLETED BOOKINGS BY MONTH DEBUG ===");
            System.out.println("Raw data: " + data);
            System.out.println("Data size: " + (data != null ? data.size() : "null"));
            System.out.println("=== END COMPLETED BOOKINGS BY MONTH DEBUG ===");
            return data;
        } catch (Exception e) {
            System.out.println("Error getting completed bookings by month: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Object[]> getCompletedBookingsBySeason() {
        try {
            List<Object[]> data = bookingRepository.findCompletedBookingsBySeason();
            System.out.println("=== COMPLETED BOOKINGS BY SEASON DEBUG ===");
            System.out.println("Raw data: " + data);
            System.out.println("Data size: " + (data != null ? data.size() : "null"));
            System.out.println("=== END COMPLETED BOOKINGS BY SEASON DEBUG ===");
            return data;
        } catch (Exception e) {
            System.out.println("Error getting completed bookings by season: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
} 