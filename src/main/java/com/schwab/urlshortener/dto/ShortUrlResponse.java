package com.schwab.urlshortener.dto;

import java.time.OffsetDateTime;

public record ShortUrlResponse(
        String originalUrl,
        String shortCode,
        String shortUrl,
        OffsetDateTime createdAt,
        OffsetDateTime expiresAt,
        boolean active
) {
}
