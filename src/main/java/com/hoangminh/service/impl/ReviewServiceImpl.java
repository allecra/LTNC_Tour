package com.hoangminh.service.impl;

import com.hoangminh.dto.ReviewDTO;
import com.hoangminh.entity.Review;
import com.hoangminh.entity.Tour;
import com.hoangminh.entity.User;
import com.hoangminh.repository.ReviewRepository;
import com.hoangminh.repository.TourRepository;
import com.hoangminh.repository.UserRepository;
import com.hoangminh.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TourRepository tourRepository;

    @Override
    public Review addReview(ReviewDTO reviewDTO) {
        User user = userRepository.findById(reviewDTO.getUserId()).orElse(null);
        Tour tour = tourRepository.findById(reviewDTO.getTourId()).orElse(null);

        if (user != null && tour != null) {
            Review review = new Review();
            review.setUser(user);
            review.setTour(tour);
            review.setRating(reviewDTO.getRating());
            review.setComment(reviewDTO.getComment());
            return reviewRepository.save(review);
        }
        return null;
    }

    @Override
    public List<ReviewDTO> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public java.util.List<com.hoangminh.dto.ReviewAdminDTO> getAllAdmin() {
        return reviewRepository.findAllReviewAdmin();
    }

    @Override
    public boolean approveReview(Long id) {
        var review = reviewRepository.findById(id).orElse(null);
        if (review != null) {
            review.setTrangThai("Đã duyệt");
            reviewRepository.save(review);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteReview(Long id) {
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        dto.setTourId(review.getTour().getId());
        dto.setUserId(review.getUser().getId());
        dto.setUserName(review.getUser().getHo_ten());
        dto.setTourName(review.getTour().getTen_tour());
        dto.setCreatedAt(review.getCreatedAt() != null ? review.getCreatedAt().toString() : null);
        dto.setTrangThai(review.getTrangThai());
        return dto;
    }
}