package com.sujan.url_shortener.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path,
        Map<String, List<String>> validationErrors
) {}
