package com.schwab.urlshortener.mapper;

import com.schwab.urlshortener.dto.ShortUrlResponse;
import com.schwab.urlshortener.dto.UrlAnalyticsResponse;
import com.schwab.urlshortener.entity.UrlMapping;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Component;

@Component
public class UrlMappingMapper {

    public ShortUrlResponse toShortUrlResponse(UrlMapping mapping, String shortUrl) {
        return new ShortUrlResponse(
                mapping.getOriginalUrl(),
                mapping.getShortCode(),
                shortUrl,
                mapping.getCreatedAt(),
                mapping.getExpiresAt(),
                mapping.isActive()
        );
    }

    public UrlAnalyticsResponse toAnalyticsResponse(UrlMapping mapping, OffsetDateTime referenceTime) {
        return new UrlAnalyticsResponse(
                mapping.getShortCode(),
                mapping.getOriginalUrl(),
                mapping.getAccessCount(),
                mapping.getCreatedAt(),
                mapping.getExpiresAt(),
                mapping.getLastAccessedAt(),
                mapping.isActive(),
                mapping.isExpired(referenceTime)
        );
    }
}
