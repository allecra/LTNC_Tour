package com.hoangminh.repository;

import com.hoangminh.entity.TinTuc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TinTucRepository extends JpaRepository<TinTuc, Long> {
    boolean existsByTieuDe(String tieuDe);
}
