package com.schwab.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import org.hibernate.validator.constraints.URL;

@Schema(description = "Request to create a short URL")
public record CreateShortUrlRequest(
        @Schema(description = "Original HTTPS URL to shorten", example = "https://example.com/articles/123", maxLength = 2048)
        @NotBlank
        @Size(max = 2048)
        @URL(protocol = "https")
        String originalUrl,

        @Schema(description = "Optional expiration timestamp. Must be in the future.", example = "2026-08-06T10:00:00Z")
        @Future
        OffsetDateTime expiresAt
) {
}
