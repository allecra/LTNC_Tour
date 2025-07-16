package com.hoangminh.controller.api.admin;

import com.hoangminh.dto.FavoriteDTO;
import com.hoangminh.entity.Favorite;
import com.hoangminh.entity.Tour;
import com.hoangminh.entity.User;
import com.hoangminh.repository.TourRepository;
import com.hoangminh.service.FavoriteService;
import com.hoangminh.service.TourService;
import com.hoangminh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private UserService userService;
    @Autowired
    private TourService tourService;
    @Autowired
    private TourRepository tourRepository;

    @GetMapping("")
    public List<FavoriteDTO> getAll() {
        return favoriteService.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public FavoriteDTO getById(@PathVariable Integer id) {
        Optional<Favorite> favorite = favoriteService.findById(id);
        return favorite.map(this::toDTO).orElse(null);
    }

    @PostMapping("")
    public FavoriteDTO create(@RequestBody FavoriteDTO favoriteDTO) {
        Favorite favorite = toEntity(favoriteDTO);
        return toDTO(favoriteService.save(favorite));
    }

    @PutMapping("/{id}")
    public FavoriteDTO update(@PathVariable Integer id, @RequestBody FavoriteDTO favoriteDTO) {
        favoriteDTO.setId(id);
        Favorite favorite = toEntity(favoriteDTO);
        return toDTO(favoriteService.save(favorite));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        favoriteService.deleteById(id);
    }

    private FavoriteDTO toDTO(Favorite favorite) {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setId(favorite.getId());
        dto.setUserId(favorite.getUser() != null ? favorite.getUser().getId() : null);
        dto.setTourId(favorite.getTour() != null ? favorite.getTour().getId() : null);
        return dto;
    }

    private Favorite toEntity(FavoriteDTO dto) {
        Favorite favorite = new Favorite();
        favorite.setId(dto.getId());
        if (dto.getUserId() != null) {
            User user = userService.findUserById(dto.getUserId());
            favorite.setUser(user);
        }
        if (dto.getTourId() != null) {
            Tour tour = tourRepository.findById(dto.getTourId()).orElse(null);
            favorite.setTour(tour);
        }
        return favorite;
    }
}