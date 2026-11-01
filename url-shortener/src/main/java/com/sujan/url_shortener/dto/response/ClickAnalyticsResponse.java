package com.sujan.url_shortener.dto.response;

import java.time.Instant;
import java.util.List;

public record ClickAnalyticsResponse(
        String shortCode,
        long totalClicks,
        int totalPages,
        int currentPage,
        int pageSize,
        List<Instant> clicks
) {}
