package com.hoangminh.controller.api.admin;

import com.hoangminh.dto.ReviewDTO;
import com.hoangminh.entity.Review;
import com.hoangminh.entity.Tour;
import com.hoangminh.entity.User;
import com.hoangminh.service.ReviewService;
import com.hoangminh.service.UserService;
import com.hoangminh.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private TourRepository tourRepository;

    @GetMapping("")
    public List<ReviewDTO> getAll() {
        return reviewService.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ReviewDTO getById(@PathVariable Integer id) {
        Optional<Review> review = reviewService.findById(id);
        return review.map(this::toDTO).orElse(null);
    }

    @PostMapping("")
    public ReviewDTO create(@RequestBody ReviewDTO reviewDTO) {
        Review review = toEntity(reviewDTO);
        return toDTO(reviewService.save(review));
    }

    @PutMapping("/{id}")
    public ReviewDTO update(@PathVariable Integer id, @RequestBody ReviewDTO reviewDTO) {
        reviewDTO.setId(id);
        Review review = toEntity(reviewDTO);
        return toDTO(reviewService.save(review));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        reviewService.deleteById(id);
    }

    private ReviewDTO toDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setTourId(review.getTour() != null ? review.getTour().getId() : null);
        dto.setUserId(review.getUser() != null ? review.getUser().getId() : null);
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }

    private Review toEntity(ReviewDTO dto) {
        Review review = new Review();
        review.setId(dto.getId());
        if (dto.getUserId() != null) {
            User user = userService.findUserById(dto.getUserId());
            review.setUser(user);
        }
        if (dto.getTourId() != null) {
            Tour tour = tourRepository.findById(dto.getTourId()).orElse(null);
            review.setTour(tour);
        }
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setCreatedAt(dto.getCreatedAt());
        return review;
    }
}