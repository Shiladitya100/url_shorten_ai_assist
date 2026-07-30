package com.schwab.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

public record CreateShortUrlRequest(
        @NotBlank
        @Size(max = 2048)
        String originalUrl,

        OffsetDateTime expiresAt
) {
}
