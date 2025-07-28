package com.hoangminh.service.impl;

import com.hoangminh.entity.Destination;
import com.hoangminh.repository.DestinationRepository;
import com.hoangminh.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationServiceImpl implements DestinationService {

    @Autowired
    private DestinationRepository destinationRepository;

    @Override
    public List<Destination> findAllDestinations() {
        return destinationRepository.findAll();
    }

    @Override
    public List<Destination> findDomesticDestinations() {
        return destinationRepository.findDomesticDestinations();
    }

    @Override
    public List<Destination> findInternationalDestinations() {
        return destinationRepository.findInternationalDestinations();
    }
}
