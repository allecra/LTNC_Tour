package com.hoangminh.service.impl;

import com.hoangminh.dto.FavoriteDTO;
import com.hoangminh.dto.TourDTO;
import com.hoangminh.entity.Favorite;
import com.hoangminh.entity.Tour;
import com.hoangminh.entity.User;
import com.hoangminh.repository.FavoriteRepository;
import com.hoangminh.repository.TourRepository;
import com.hoangminh.repository.UserRepository;
import com.hoangminh.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TourRepository tourRepository;

    @Override
    public FavoriteDTO addToWishlist(Long userId, Long tourId) {
        User user = userRepository.findById(userId).orElse(null);
        Tour tour = tourRepository.findById(tourId).orElse(null);

        if (user != null && tour != null) {
            if (!favoriteRepository.existsByUserIdAndTourId(userId, tourId)) {
                Favorite favorite = new Favorite();
                favorite.setUser(user);
                favorite.setTour(tour);
                Favorite savedFavorite = favoriteRepository.save(favorite);
                return convertToDTO(savedFavorite);
            }
        }
        return null;
    }

    @Override
    public void removeFromWishlist(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }

    @Override
    public List<FavoriteDTO> getWishlistByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private FavoriteDTO convertToDTO(Favorite favorite) {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setId(favorite.getId());
        dto.setUserId(favorite.getUser().getId());
        dto.setTourId(favorite.getTour().getId());

        Tour tour = favorite.getTour();
        TourDTO tourDTO = new TourDTO(
                tour.getId(),
                tour.getTen_tour(),
                tour.getGioi_thieu_tour(),
                tour.getSo_ngay(),
                tour.getNoi_dung_tour(),
                tour.getDestination().getName(),
                tour.getTourType().getTen_loai(),
                tour.getAnh_dai_dien(),
                tour.getDiem_khoi_hanh(),
                tour.getTrang_thai(),
                tour.getGia_tour(),
                tour.getSale_price());
        dto.setTour(tourDTO);

        return dto;
    }
}