package com.sujan.url_shortener.dto.response;

public record CreateShortUrlResponse(
        String shortCode,
        String originalUrl
) {}
