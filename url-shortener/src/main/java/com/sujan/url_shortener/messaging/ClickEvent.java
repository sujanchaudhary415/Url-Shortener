package com.sujan.url_shortener.messaging;

import java.time.Instant;

public record ClickEvent(
        String shortCode,
        Instant clickedAt
) {}
