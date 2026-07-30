package com.schwab.urlshortener.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;

class UrlMappingTest {

    @Test
    void shouldTreatMappingAsExpiredWhenExpirationIsAtReferenceTime() {
        OffsetDateTime now = OffsetDateTime.parse("2026-07-30T10:00:00Z");
        UrlMapping mapping = UrlMapping.builder()
                .originalUrl("https://example.com")
                .shortCode("AbC123x")
                .createdAt(now.minusDays(1))
                .expiresAt(now)
                .build();

        assertThat(mapping.isExpired(now)).isTrue();
        assertThat(mapping.isRedirectable(now)).isFalse();
    }

    @Test
    void shouldTreatMappingWithoutExpirationAsRedirectableWhenActive() {
        OffsetDateTime now = OffsetDateTime.parse("2026-07-30T10:00:00Z");
        UrlMapping mapping = UrlMapping.builder()
                .originalUrl("https://example.com")
                .shortCode("AbC123x")
                .createdAt(now)
                .build();

        assertThat(mapping.isExpired(now)).isFalse();
        assertThat(mapping.isRedirectable(now)).isTrue();
    }

    @Test
    void shouldRecordAccess() {
        OffsetDateTime now = OffsetDateTime.parse("2026-07-30T10:00:00Z");
        OffsetDateTime accessedAt = now.plusMinutes(5);
        UrlMapping mapping = UrlMapping.builder()
                .originalUrl("https://example.com")
                .shortCode("AbC123x")
                .createdAt(now)
                .build();

        mapping.recordAccess(accessedAt);

        assertThat(mapping.getAccessCount()).isEqualTo(1);
        assertThat(mapping.getLastAccessedAt()).isEqualTo(accessedAt);
    }
}
