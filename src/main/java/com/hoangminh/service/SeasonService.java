package com.hoangminh.service;

import com.hoangminh.entity.Season;
import java.util.List;
import java.util.Optional;

public interface SeasonService {
    List<Season> findAll();

    Optional<Season> findById(Integer id);

    Season save(Season season);

    void deleteById(Integer id);
}