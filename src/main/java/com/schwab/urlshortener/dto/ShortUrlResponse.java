package com.schwab.urlshortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;

@Schema(description = "Created short URL response")
public record ShortUrlResponse(
        @Schema(description = "Original URL", example = "https://example.com/articles/123")
        String originalUrl,
        @Schema(description = "Generated short code", example = "AbC123x")
        String shortCode,
        @Schema(description = "Public short URL", example = "http://localhost:8080/AbC123x")
        String shortUrl,
        @Schema(description = "Creation timestamp", example = "2026-07-30T10:00:00Z")
        OffsetDateTime createdAt,
        @Schema(description = "Optional expiration timestamp", example = "2026-08-06T10:00:00Z")
        OffsetDateTime expiresAt,
        @Schema(description = "Whether the mapping is active", example = "true")
        boolean active
) {
}
