package com.hoangminh.dto;

import lombok.Data;

@Data
public class FavoriteDTO {
    private Long id;
    private Long userId;
    private Long tourId;
    private TourDTO tour; // Để trả về thông tin chi tiết của tour trong wishlist
}