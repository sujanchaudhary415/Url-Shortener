package com.sujan.url_shortener.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API error response")
public record ErrorResponse(
        @Schema(description = "Time when the error occurred",
                example = "2026-11-17T10:30:00Z")
        Instant timestamp,

        @Schema(description = "HTTP status code",
                example = "404")
        Integer status,

        @Schema(description = "HTTP error description",
                example = "Not Found")
        String error,

        @Schema(description = "Detailed error message",
                example = "Short URL not found")
        String message,

        @Schema(description = "API request path",
                example = "/api/urls/invalid")
        String path,


        @Schema(description = "Field-specific validation errors")
        Map<String, List<String>> validationErrors
) {}
