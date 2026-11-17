package com.sujan.url_shortener.controller;

import com.sujan.url_shortener.dto.response.ClickAnalyticsResponse;
import com.sujan.url_shortener.service.ClickAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/analytics")
@Tag(name = "Click Analytics", description = "APIs for retrieving URL click analytics")
public class ClickAnalyticsController {

    public final ClickAnalyticsService clickAnalyticsService;

    @GetMapping("/{shortCode}")
    @Operation(
            summary = "Get click analytics",
            description = "Retrieves paginated click analytics for a given short URL"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analytics retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "404", description = "Short URL not found")
    })
    public ClickAnalyticsResponse getAnalytics(
            @PathVariable String shortCode,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        return clickAnalyticsService.getAnalytics(shortCode, page, size);
    }
}
