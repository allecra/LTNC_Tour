package com.hoangminh.service;

import com.hoangminh.entity.TourStart;
import java.util.List;

public interface TourStartService {
    List<TourStart> getAllTourStarts();
    TourStart getTourStartById(Long id);
    List<TourStart> getTourStartsByTourId(Long tourId);
    TourStart saveTourStart(TourStart tourStart);
    void deleteTourStart(Long id);
}
