package com.hoangminh.service;

import com.hoangminh.dto.FavoriteDTO;

import java.util.List;

public interface FavoriteService {
    FavoriteDTO addToWishlist(Long userId, Long tourId);

    void removeFromWishlist(Long favoriteId);

    List<FavoriteDTO> getWishlistByUserId(Long userId);
}