package com.hoangminh.service;

import com.hoangminh.entity.TourType;
import java.util.List;
import java.util.Optional;

public interface TourTypeService {
    List<TourType> findAll();

    Optional<TourType> findById(Integer id);

    TourType save(TourType tourType);

    void deleteById(Integer id);
}