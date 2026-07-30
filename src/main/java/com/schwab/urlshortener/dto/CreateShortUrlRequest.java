package com.schwab.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import org.hibernate.validator.constraints.URL;

public record CreateShortUrlRequest(
        @NotBlank
        @Size(max = 2048)
        @URL(protocol = "https")
        String originalUrl,

        OffsetDateTime expiresAt
) {
}
