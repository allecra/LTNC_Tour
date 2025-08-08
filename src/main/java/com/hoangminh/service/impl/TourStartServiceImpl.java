package com.hoangminh.service.impl;

import com.hoangminh.entity.TourStart;
import com.hoangminh.repository.TourStartRepository;
import com.hoangminh.service.TourStartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourStartServiceImpl implements TourStartService {

    @Autowired
    private TourStartRepository tourStartRepository;

    @Override
    public List<TourStart> getAllTourStarts() {
        return tourStartRepository.findAll();
    }

    @Override
    public TourStart getTourStartById(Long id) {
        return tourStartRepository.findById(id).orElse(null);
    }

    @Override
    public List<TourStart> getTourStartsByTourId(Long tourId) {
        return tourStartRepository.findByTourId(tourId);
    }

    @Override
    public TourStart saveTourStart(TourStart tourStart) {
        return tourStartRepository.save(tourStart);
    }

    @Override
    public void deleteTourStart(Long id) {
        tourStartRepository.deleteById(id);
    }
}
