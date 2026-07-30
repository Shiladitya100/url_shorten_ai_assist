package com.schwab.urlshortener.service.impl;

import com.schwab.urlshortener.exception.ShortCodeGenerationException;
import com.schwab.urlshortener.repository.UrlMappingRepository;
import com.schwab.urlshortener.service.ShortCodeGenerationService;
import com.schwab.urlshortener.util.ShortCodeGenerator;
import com.schwab.urlshortener.validation.ShortCodeRules;
import org.springframework.stereotype.Service;

@Service
public class ShortCodeGenerationServiceImpl implements ShortCodeGenerationService {

    private static final int MAX_GENERATION_ATTEMPTS = 10;

    private final ShortCodeGenerator shortCodeGenerator;
    private final UrlMappingRepository urlMappingRepository;

    public ShortCodeGenerationServiceImpl(
            ShortCodeGenerator shortCodeGenerator,
            UrlMappingRepository urlMappingRepository
    ) {
        this.shortCodeGenerator = shortCodeGenerator;
        this.urlMappingRepository = urlMappingRepository;
    }

    @Override
    public String generateUniqueCode() {
        for (int attempt = 1; attempt <= MAX_GENERATION_ATTEMPTS; attempt++) {
            String candidate = shortCodeGenerator.generate(ShortCodeRules.LENGTH);
            if (!ShortCodeRules.isReserved(candidate) && !urlMappingRepository.existsByShortCode(candidate)) {
                return candidate;
            }
        }

        throw new ShortCodeGenerationException(
                "Unable to generate a unique short code after " + MAX_GENERATION_ATTEMPTS + " attempts"
        );
    }
}
