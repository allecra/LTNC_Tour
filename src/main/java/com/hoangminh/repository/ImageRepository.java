package com.hoangminh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hoangminh.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

	List<Image> findByTourId(Long tourId);

}
