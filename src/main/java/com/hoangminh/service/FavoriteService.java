package com.hoangminh.service;

import com.hoangminh.entity.Favorite;
import java.util.List;
import java.util.Optional;

public interface FavoriteService {
    List<Favorite> findAll();

    Optional<Favorite> findById(Integer id);

    Favorite save(Favorite favorite);

    void deleteById(Integer id);
}