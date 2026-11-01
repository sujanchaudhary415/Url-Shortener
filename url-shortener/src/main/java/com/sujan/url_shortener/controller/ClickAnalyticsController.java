package com.sujan.url_shortener.controller;

import com.sujan.url_shortener.dto.response.ClickAnalyticsResponse;
import com.sujan.url_shortener.service.ClickAnalyticsService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/analytics")
public class ClickAnalyticsController {

    public final ClickAnalyticsService clickAnalyticsService;

    @GetMapping("/{shortCode}")
    public ClickAnalyticsResponse getAnalytics(
            @PathVariable String shortCode,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        return clickAnalyticsService.getAnalytics(shortCode, page, size);
    }
}
