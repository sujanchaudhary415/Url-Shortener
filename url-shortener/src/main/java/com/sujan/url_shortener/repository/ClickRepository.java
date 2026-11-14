package com.sujan.url_shortener.repository;

import com.sujan.url_shortener.entity.Click;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClickRepository extends JpaRepository<Click,Long> {
    Long countByShortCode(String shortCode);
    Page<Click> findByShortCodeOrderByClickedAtDesc(String shortCode, Pageable pageable);


}
