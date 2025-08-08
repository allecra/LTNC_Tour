package com.hoangminh.controller;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoangminh.repository.TourStartRepository;

@RestController
@RequestMapping("/api")
public class TourDateController {

    @Autowired
    private TourStartRepository tourStartRepository;

    @GetMapping("/tour-dates")
    public List<Date> getAvailableTourDates() {
        return tourStartRepository.findAllAvailableDates();
    }
}
