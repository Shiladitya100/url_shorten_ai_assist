package com.schwab.urlshortener.dto;

import java.time.OffsetDateTime;

public record UrlAnalyticsResponse(
        String shortCode,
        String originalUrl,
        long accessCount,
        OffsetDateTime createdAt,
        OffsetDateTime expiresAt,
        OffsetDateTime lastAccessedAt,
        boolean active,
        boolean expired
) {
}
