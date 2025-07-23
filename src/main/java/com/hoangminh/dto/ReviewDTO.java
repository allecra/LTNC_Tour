package com.hoangminh.dto;

import lombok.Data;

@Data
public class ReviewDTO {
    private Long id;
    private Long tourId;
    private Long userId;
    private int rating;
    private String comment;
}