package com.hoangminh.service;

import com.hoangminh.dto.ReviewDTO;
import com.hoangminh.entity.Review;

import java.util.List;

public interface ReviewService {
    Review addReview(ReviewDTO reviewDTO);

    List<ReviewDTO> getReviewsByUserId(Long userId);
    java.util.List<com.hoangminh.dto.ReviewAdminDTO> getAllAdmin();
    ReviewDTO getReviewById(Long id);
    boolean approveReview(Long id);
    boolean deleteReview(Long id);
}