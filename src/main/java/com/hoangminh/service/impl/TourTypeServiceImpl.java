package com.hoangminh.service.impl;

import com.hoangminh.entity.TourType;
import com.hoangminh.repository.TourTypeRepository;
import com.hoangminh.service.TourTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TourTypeServiceImpl implements TourTypeService {
    @Autowired
    private TourTypeRepository tourTypeRepository;

    @Override
    public List<TourType> findAll() {
        return tourTypeRepository.findAll();
    }

    @Override
    public Optional<TourType> findById(Integer id) {
        return tourTypeRepository.findById(id);
    }

    @Override
    public TourType save(TourType tourType) {
        return tourTypeRepository.save(tourType);
    }

    @Override
    public void deleteById(Integer id) {
        tourTypeRepository.deleteById(id);
    }
}