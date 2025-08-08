package com.hoangminh.repository;

import com.hoangminh.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.hoangminh.dto.ReviewDTO;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserId(Long userId);
    
    List<Review> findByTrangThai(String trangThai);

    @Query("SELECT AVG(r.rating) FROM Review r")
    Double averageRating();

    @Query("SELECT new com.hoangminh.dto.ReviewAdminDTO(r.id, u.ho_ten, t.ten_tour, r.rating, r.comment, r.createdAt, r.trangThai) FROM Review r JOIN r.user u JOIN r.tour t ORDER BY r.createdAt DESC")
    java.util.List<com.hoangminh.dto.ReviewAdminDTO> findAllReviewAdmin();
}