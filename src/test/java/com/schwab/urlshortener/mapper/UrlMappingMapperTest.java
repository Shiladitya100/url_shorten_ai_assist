package com.schwab.urlshortener.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.schwab.urlshortener.dto.ShortUrlResponse;
import com.schwab.urlshortener.dto.UrlAnalyticsResponse;
import com.schwab.urlshortener.entity.UrlMapping;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;

class UrlMappingMapperTest {

    private final UrlMappingMapper mapper = new UrlMappingMapper();

    @Test
    void shouldMapShortUrlResponse() {
        OffsetDateTime now = OffsetDateTime.parse("2026-07-30T10:00:00Z");
        UrlMapping mapping = UrlMapping.builder()
                .originalUrl("https://example.com/articles/123")
                .shortCode("AbC123x")
                .createdAt(now)
                .expiresAt(now.plusDays(7))
                .build();

        ShortUrlResponse response = mapper.toShortUrlResponse(mapping, "https://sho.rt/AbC123x");

        assertThat(response.originalUrl()).isEqualTo("https://example.com/articles/123");
        assertThat(response.shortCode()).isEqualTo("AbC123x");
        assertThat(response.shortUrl()).isEqualTo("https://sho.rt/AbC123x");
        assertThat(response.createdAt()).isEqualTo(now);
        assertThat(response.expiresAt()).isEqualTo(now.plusDays(7));
        assertThat(response.active()).isTrue();
    }

    @Test
    void shouldMapAnalyticsResponseWithExpiredFlag() {
        OffsetDateTime now = OffsetDateTime.parse("2026-07-30T10:00:00Z");
        UrlMapping mapping = UrlMapping.builder()
                .originalUrl("https://example.com/articles/123")
                .shortCode("AbC123x")
                .createdAt(now.minusDays(2))
                .expiresAt(now.minusDays(1))
                .accessCount(3)
                .lastAccessedAt(now.minusHours(12))
                .build();

        UrlAnalyticsResponse response = mapper.toAnalyticsResponse(mapping, now);

        assertThat(response.shortCode()).isEqualTo("AbC123x");
        assertThat(response.originalUrl()).isEqualTo("https://example.com/articles/123");
        assertThat(response.accessCount()).isEqualTo(3);
        assertThat(response.createdAt()).isEqualTo(now.minusDays(2));
        assertThat(response.expiresAt()).isEqualTo(now.minusDays(1));
        assertThat(response.lastAccessedAt()).isEqualTo(now.minusHours(12));
        assertThat(response.active()).isTrue();
        assertThat(response.expired()).isTrue();
    }
}
