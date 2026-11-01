package com.sujan.url_shortener.controller;

import com.sujan.url_shortener.dto.response.ClickAnalyticsResponse;
import com.sujan.url_shortener.service.ClickAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analytics")
public class ClickAnalyticsController {

    public final ClickAnalyticsService clickAnalyticsService;

    @GetMapping("/{shortCode}")
    public ClickAnalyticsResponse getAnalytics(@PathVariable String shortCode){
        return clickAnalyticsService.getAnalytics(shortCode);
    }
}
