package com.sujan.url_shortener.repository;

import com.sujan.url_shortener.entity.ShortUrl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl,Long> {

    @Cacheable(cacheNames = "shortUrls", key = "#shortCode", unless = "#result == null")
    Optional<ShortUrl>findByShortCode(String shortCode);
    boolean existsByOriginalUrl(String originalUrl);
    Optional<ShortUrl> findByOriginalUrl(String originalUrl);

}
