package com.schwab.urlshortener.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.schwab.urlshortener.config.UrlShortenerProperties;
import com.schwab.urlshortener.dto.CreateShortUrlRequest;
import com.schwab.urlshortener.dto.ShortUrlResponse;
import com.schwab.urlshortener.entity.UrlMapping;
import com.schwab.urlshortener.mapper.UrlMappingMapper;
import com.schwab.urlshortener.repository.UrlMappingRepository;
import com.schwab.urlshortener.service.ShortCodeGenerationService;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
}
