package com.hoangminh.dto;

import lombok.Data;

@Data
public class ReviewDTO {
    private Long id;
    private Long tourId;
    private Long userId;
    private int rating;
    private String comment;
    private String userName;
    private String tourName;
    private String createdAt;
    private String trangThai;

    public ReviewDTO() {}

    public ReviewDTO(Long id, Long tourId, Long userId, Integer rating, String comment, String userName, String tourName, String createdAt, String trangThai) {
        this.id = id;
        this.tourId = tourId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.userName = userName;
        this.tourName = tourName;
        this.createdAt = createdAt;
        this.trangThai = trangThai;
    }
}