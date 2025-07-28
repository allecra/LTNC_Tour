package com.hoangminh.repository;

import com.hoangminh.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
    
    @Query("SELECT d FROM Destination d WHERE d.is_domestic = true")
    List<Destination> findDomesticDestinations();
    
    @Query("SELECT d FROM Destination d WHERE d.is_domestic = false")
    List<Destination> findInternationalDestinations();
}
