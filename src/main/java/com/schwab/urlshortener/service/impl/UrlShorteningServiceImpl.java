package com.schwab.urlshortener.service.impl;

import com.schwab.urlshortener.config.UrlShortenerProperties;
import com.schwab.urlshortener.dto.CreateShortUrlRequest;
import com.schwab.urlshortener.dto.ShortUrlResponse;
import com.schwab.urlshortener.entity.UrlMapping;
import com.schwab.urlshortener.exception.UrlMappingNotFoundException;
import com.schwab.urlshortener.exception.UrlMappingNotRedirectableException;
import com.schwab.urlshortener.mapper.UrlMappingMapper;
import com.schwab.urlshortener.repository.UrlMappingRepository;
import com.schwab.urlshortener.service.ShortCodeGenerationService;
import com.schwab.urlshortener.service.UrlShorteningService;
import java.time.Clock;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UrlShorteningServiceImpl implements UrlShorteningService {

    private final ShortCodeGenerationService shortCodeGenerationService;
    private final UrlMappingRepository urlMappingRepository;
    private final UrlMappingMapper urlMappingMapper;
    private final UrlShortenerProperties properties;
    private final Clock clock;

    public UrlShorteningServiceImpl(
            ShortCodeGenerationService shortCodeGenerationService,
            UrlMappingRepository urlMappingRepository,
            UrlMappingMapper urlMappingMapper,
            UrlShortenerProperties properties,
            Clock clock
    ) {
        this.shortCodeGenerationService = shortCodeGenerationService;
        this.urlMappingRepository = urlMappingRepository;
        this.urlMappingMapper = urlMappingMapper;
        this.properties = properties;
        this.clock = clock;
    }

    @Override
    @Transactional
    public ShortUrlResponse createShortUrl(CreateShortUrlRequest request) {
        String shortCode = shortCodeGenerationService.generateUniqueCode();
        OffsetDateTime createdAt = OffsetDateTime.now(clock);

        UrlMapping mapping = UrlMapping.builder()
                .originalUrl(request.originalUrl())
                .shortCode(shortCode)
                .createdAt(createdAt)
                .expiresAt(request.expiresAt())
                .build();

        UrlMapping savedMapping = urlMappingRepository.save(mapping);
        return urlMappingMapper.toShortUrlResponse(savedMapping, properties.buildShortUrl(shortCode));
    }

    @Override
    @Transactional
    public String resolveRedirectUrl(String shortCode) {
        UrlMapping mapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlMappingNotFoundException(shortCode));

        OffsetDateTime accessedAt = OffsetDateTime.now(clock);
        if (!mapping.isRedirectable(accessedAt)) {
            throw new UrlMappingNotRedirectableException(shortCode);
        }

        mapping.recordAccess(accessedAt);
        return mapping.getOriginalUrl();
    }
}
