package com.hoangminh.repository;

import com.hoangminh.entity.TourType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourTypeRepository extends JpaRepository<TourType, Long> {
}