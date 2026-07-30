package com.schwab.urlshortener.controller;

import com.schwab.urlshortener.exception.UrlMappingNotFoundException;
import com.schwab.urlshortener.exception.UrlMappingExpiredException;
import com.schwab.urlshortener.exception.UrlMappingNotRedirectableException;
import com.schwab.urlshortener.service.UrlShorteningService;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class RedirectController {

    private final UrlShorteningService urlShorteningService;

    public RedirectController(UrlShorteningService urlShorteningService) {
        this.urlShorteningService = urlShorteningService;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        try {
            String originalUrl = urlShorteningService.resolveRedirectUrl(shortCode);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(originalUrl))
                    .build();
        } catch (UrlMappingNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        } catch (UrlMappingExpiredException exception) {
            throw new ResponseStatusException(HttpStatus.GONE, exception.getMessage(), exception);
        } catch (UrlMappingNotRedirectableException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }
}
