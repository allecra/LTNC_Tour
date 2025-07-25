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
        for (Object[] row : data) {
            Integer month = (Integer) row[0];
            Long count = (Long) row[1];
            if (month != null && month >= 1 && month <= 12) {
                result.set(month - 1, count);
            }
        }
        return result;
    }

    @Override
    public List<Long> getTourCountBySeason() {
        List<Long> result = new ArrayList<>(Arrays.asList(0L, 0L, 0L, 0L));
        List<Object[]> data = tourRepository.countToursBySeason();
        Map<String, Integer> seasonIndex = Map.of(
            "Xuân", 0, "Hạ", 1, "Thu", 2, "Đông", 3
        );
        for (Object[] row : data) {
            String season = (String) row[0];
            Long count = (Long) row[1];
            Integer idx = seasonIndex.get(season);
            if (idx != null) result.set(idx, count);
        }
        return result;
    }
} 