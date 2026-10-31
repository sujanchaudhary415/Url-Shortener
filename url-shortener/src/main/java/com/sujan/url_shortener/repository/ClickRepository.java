package com.sujan.url_shortener.repository;

import com.sujan.url_shortener.entity.Click;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClickRepository extends JpaRepository<Click,Long> {
}
