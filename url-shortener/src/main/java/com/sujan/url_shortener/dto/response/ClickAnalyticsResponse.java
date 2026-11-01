package com.sujan.url_shortener.dto.response;

import java.time.Instant;
import java.util.List;

public record ClickAnalyticsResponse(
        String shortCode,
        long totalCLicks,
        List<Instant>clicks
) {}
