package com.schwab.urlshortener.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.schwab.urlshortener.config.UrlShortenerProperties;
import com.schwab.urlshortener.dto.CreateShortUrlRequest;
import com.schwab.urlshortener.dto.ShortUrlResponse;
import com.schwab.urlshortener.entity.UrlMapping;
import com.schwab.urlshortener.exception.UrlMappingExpiredException;
import com.schwab.urlshortener.exception.UrlMappingNotFoundException;
import com.schwab.urlshortener.exception.UrlMappingNotRedirectableException;
import com.schwab.urlshortener.mapper.UrlMappingMapper;
import com.schwab.urlshortener.repository.UrlMappingRepository;
import com.schwab.urlshortener.service.ShortCodeGenerationService;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UrlShorteningServiceImplTest {

    @Mock
    private ShortCodeGenerationService shortCodeGenerationService;

    @Mock
    private UrlMappingRepository urlMappingRepository;

    private UrlShorteningServiceImpl service;

    @BeforeEach
    void setUp() {
        UrlShortenerProperties properties = new UrlShortenerProperties();
        properties.setBaseUrl("http://localhost:8080/");
        Clock clock = Clock.fixed(Instant.parse("2026-07-30T10:00:00Z"), ZoneOffset.UTC);

        service = new UrlShorteningServiceImpl(
                shortCodeGenerationService,
                urlMappingRepository,
                new UrlMappingMapper(),
                properties,
                clock
        );
    }

    @Test
    void shouldCreateShortUrlMapping() {
        OffsetDateTime expiresAt = OffsetDateTime.parse("2026-08-06T10:00:00Z");
        CreateShortUrlRequest request = new CreateShortUrlRequest("https://example.com/articles/123", expiresAt);
        when(shortCodeGenerationService.generateUniqueCode()).thenReturn("AbC123x");
        when(urlMappingRepository.save(any(UrlMapping.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShortUrlResponse response = service.createShortUrl(request);

        ArgumentCaptor<UrlMapping> mappingCaptor = ArgumentCaptor.forClass(UrlMapping.class);
        verify(urlMappingRepository).save(mappingCaptor.capture());
        UrlMapping savedMapping = mappingCaptor.getValue();

        assertThat(savedMapping.getOriginalUrl()).isEqualTo("https://example.com/articles/123");
        assertThat(savedMapping.getShortCode()).isEqualTo("AbC123x");
        assertThat(savedMapping.getCreatedAt()).isEqualTo(OffsetDateTime.parse("2026-07-30T10:00:00Z"));
        assertThat(savedMapping.getExpiresAt()).isEqualTo(expiresAt);
        assertThat(savedMapping.isActive()).isTrue();

        assertThat(response.originalUrl()).isEqualTo("https://example.com/articles/123");
        assertThat(response.shortCode()).isEqualTo("AbC123x");
        assertThat(response.shortUrl()).isEqualTo("http://localhost:8080/AbC123x");
        assertThat(response.createdAt()).isEqualTo(OffsetDateTime.parse("2026-07-30T10:00:00Z"));
        assertThat(response.expiresAt()).isEqualTo(expiresAt);
        assertThat(response.active()).isTrue();
    }

    @Test
    void shouldResolveRedirectUrlAndRecordAccess() {
        UrlMapping mapping = UrlMapping.builder()
                .originalUrl("https://example.com/articles/123")
                .shortCode("AbC123x")
                .createdAt(OffsetDateTime.parse("2026-07-30T09:00:00Z"))
                .expiresAt(OffsetDateTime.parse("2026-08-06T10:00:00Z"))
                .build();
        when(urlMappingRepository.findByShortCode("AbC123x")).thenReturn(Optional.of(mapping));

        String redirectUrl = service.resolveRedirectUrl("AbC123x");

        assertThat(redirectUrl).isEqualTo("https://example.com/articles/123");
        assertThat(mapping.getAccessCount()).isEqualTo(1);
        assertThat(mapping.getLastAccessedAt()).isEqualTo(OffsetDateTime.parse("2026-07-30T10:00:00Z"));
    }

    @Test
    void shouldRejectMissingShortCode() {
        when(urlMappingRepository.findByShortCode("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.resolveRedirectUrl("missing"))
                .isInstanceOf(UrlMappingNotFoundException.class)
                .hasMessageContaining("missing");
    }

    @Test
    void shouldRejectExpiredShortCode() {
        UrlMapping mapping = UrlMapping.builder()
                .originalUrl("https://example.com/articles/123")
                .shortCode("AbC123x")
                .createdAt(OffsetDateTime.parse("2026-07-29T10:00:00Z"))
                .expiresAt(OffsetDateTime.parse("2026-07-30T09:59:59Z"))
                .build();
        when(urlMappingRepository.findByShortCode("AbC123x")).thenReturn(Optional.of(mapping));

        assertThatThrownBy(() -> service.resolveRedirectUrl("AbC123x"))
                .isInstanceOf(UrlMappingExpiredException.class)
                .hasMessageContaining("AbC123x");
        assertThat(mapping.getAccessCount()).isZero();
        assertThat(mapping.getLastAccessedAt()).isNull();
    }

    @Test
    void shouldTreatExpirationAtCurrentTimeAsExpired() {
        UrlMapping mapping = UrlMapping.builder()
                .originalUrl("https://example.com/articles/123")
                .shortCode("AbC123x")
                .createdAt(OffsetDateTime.parse("2026-07-29T10:00:00Z"))
                .expiresAt(OffsetDateTime.parse("2026-07-30T10:00:00Z"))
                .build();
        when(urlMappingRepository.findByShortCode("AbC123x")).thenReturn(Optional.of(mapping));

        assertThatThrownBy(() -> service.resolveRedirectUrl("AbC123x"))
                .isInstanceOf(UrlMappingExpiredException.class);
        assertThat(mapping.getAccessCount()).isZero();
        assertThat(mapping.getLastAccessedAt()).isNull();
    }

    @Test
    void shouldRejectInactiveShortCodeWithoutRecordingAccess() {
        UrlMapping mapping = UrlMapping.builder()
                .originalUrl("https://example.com/articles/123")
                .shortCode("AbC123x")
                .createdAt(OffsetDateTime.parse("2026-07-29T10:00:00Z"))
                .expiresAt(OffsetDateTime.parse("2026-08-06T10:00:00Z"))
                .active(false)
                .build();
        when(urlMappingRepository.findByShortCode("AbC123x")).thenReturn(Optional.of(mapping));

        assertThatThrownBy(() -> service.resolveRedirectUrl("AbC123x"))
                .isInstanceOf(UrlMappingNotRedirectableException.class);
        assertThat(mapping.getAccessCount()).isZero();
        assertThat(mapping.getLastAccessedAt()).isNull();
    }
}
