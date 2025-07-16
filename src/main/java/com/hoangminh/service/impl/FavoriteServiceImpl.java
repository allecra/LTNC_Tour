package com.hoangminh.service.impl;

import com.hoangminh.entity.Favorite;
import com.hoangminh.repository.FavoriteRepository;
import com.hoangminh.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Override
    public List<Favorite> findAll() {
        return favoriteRepository.findAll();
    }

    @Override
    public Optional<Favorite> findById(Integer id) {
        return favoriteRepository.findById(id);
    }

    @Override
    public Favorite save(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    @Override
    public void deleteById(Integer id) {
        favoriteRepository.deleteById(id);
    }
}