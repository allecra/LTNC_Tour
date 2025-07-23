package com.hoangminh.service.impl;

import com.hoangminh.entity.Image;
import com.hoangminh.entity.Tour;
import com.hoangminh.repository.ImageRepository;
import com.hoangminh.repository.TourRepository;
import com.hoangminh.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private TourRepository tourRepository;

    @Override
    public List<Image> findByTourId(Long id) {
        return this.imageRepository.findByTourId(id);
    }

    @Override
    public Image addToTour(Long tourId, String url) {
        Image image = new Image();
        Optional<Tour> tourOptional = this.tourRepository.findById(tourId);

        if (tourOptional.isPresent()) {
            image.setTour(tourOptional.get());
            image.setUrl(url);
            return this.imageRepository.save(image);
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        this.imageRepository.deleteById(id);
    }
}
