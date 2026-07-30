package com.schwab.urlshortener.controller;

import com.schwab.urlshortener.service.UrlShorteningService;
import com.schwab.urlshortener.validation.ShortCodeRules;
import jakarta.validation.constraints.Pattern;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class RedirectController {

    private final UrlShorteningService urlShorteningService;

    public RedirectController(UrlShorteningService urlShorteningService) {
        this.urlShorteningService = urlShorteningService;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            @PathVariable @Pattern(regexp = ShortCodeRules.PATTERN, message = ShortCodeRules.VALIDATION_MESSAGE)
            String shortCode
    ) {
        String originalUrl = urlShorteningService.resolveRedirectUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}
