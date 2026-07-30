package com.schwab.urlshortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;

@Schema(description = "Aggregate analytics for a short URL")
public record UrlAnalyticsResponse(
        @Schema(description = "Short code", example = "AbC123x")
        String shortCode,
        @Schema(description = "Original URL", example = "https://example.com/articles/123")
        String originalUrl,
        @Schema(description = "Successful redirect count", example = "5")
        long accessCount,
        @Schema(description = "Creation timestamp", example = "2026-07-29T10:00:00Z")
        OffsetDateTime createdAt,
        @Schema(description = "Optional expiration timestamp", example = "2026-08-06T10:00:00Z")
        OffsetDateTime expiresAt,
        @Schema(description = "Most recent successful redirect timestamp", example = "2026-07-30T09:30:00Z")
        OffsetDateTime lastAccessedAt,
        @Schema(description = "Whether the mapping is active", example = "true")
        boolean active,
        @Schema(description = "Whether the mapping is expired at response time", example = "false")
        boolean expired
) {
}
