package com.sujan.url_shortener.service;

import com.sujan.url_shortener.entity.ShortUrl;

import java.util.Optional;

public interface ShortUrlService {
   ShortUrl shortenUrl(String originalUrl);
   Optional<ShortUrl>getByShortCode(String shortCode);
}
