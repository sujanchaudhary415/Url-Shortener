package com.sujan.url_shortener.service;

import com.sujan.url_shortener.dto.response.ClickAnalyticsResponse;

public interface ClickAnalyticsService {
    ClickAnalyticsResponse getAnalytics(String shortCode);
}
