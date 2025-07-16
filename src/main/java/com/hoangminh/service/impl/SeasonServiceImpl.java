package com.hoangminh.service.impl;

import com.hoangminh.entity.Season;
import com.hoangminh.repository.SeasonRepository;
import com.hoangminh.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeasonServiceImpl implements SeasonService {
    @Autowired
    private SeasonRepository seasonRepository;

    @Override
    public List<Season> findAll() {
        return seasonRepository.findAll();
    }

    @Override
    public Optional<Season> findById(Integer id) {
        return seasonRepository.findById(id);
    }

    @Override
    public Season save(Season season) {
        return seasonRepository.save(season);
    }

    @Override
    public void deleteById(Integer id) {
        seasonRepository.deleteById(id);
    }
}