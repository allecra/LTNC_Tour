package com.hoangminh.service;

import com.hoangminh.entity.Destination;
import java.util.List;

public interface DestinationService {
    List<Destination> findAllDestinations();
    List<Destination> findDomesticDestinations();
    List<Destination> findInternationalDestinations();
}
