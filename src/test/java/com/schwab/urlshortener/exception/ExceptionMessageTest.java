package com.schwab.urlshortener.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ExceptionMessageTest {

    @Test
    void shouldCreateNotFoundMessage() {
        UrlMappingNotFoundException exception = new UrlMappingNotFoundException("AbC123x");

        assertThat(exception).hasMessage("URL mapping not found for short code: AbC123x");
    }

    @Test
    void shouldCreateExpiredMessage() {
        UrlMappingExpiredException exception = new UrlMappingExpiredException("AbC123x");

        assertThat(exception).hasMessage("URL mapping expired for short code: AbC123x");
    }

    @Test
    void shouldCreateNotRedirectableMessage() {
        UrlMappingNotRedirectableException exception = new UrlMappingNotRedirectableException("AbC123x");

        assertThat(exception).hasMessage("URL mapping is not redirectable for short code: AbC123x");
    }
}
