package com.hoangminh.repository;

import com.hoangminh.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUserIdAndTourId(Long userId, Long tourId);

    List<Favorite> findByUserId(Long userId);
}