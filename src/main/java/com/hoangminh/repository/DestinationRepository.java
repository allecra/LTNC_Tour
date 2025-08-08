package com.hoangminh.repository;

import com.hoangminh.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
    Destination findByName(String name);
}
