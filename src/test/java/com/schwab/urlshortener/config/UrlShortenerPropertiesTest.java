package com.schwab.urlshortener.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UrlShortenerPropertiesTest {

    @Test
    void shouldBuildShortUrlWithoutDuplicatingSlash() {
        UrlShortenerProperties properties = new UrlShortenerProperties();
        properties.setBaseUrl("http://localhost:8080/");

        String shortUrl = properties.buildShortUrl("AbC123x");

        assertThat(shortUrl).isEqualTo("http://localhost:8080/AbC123x");
    }

    @Test
    void shouldBuildShortUrlWhenBaseUrlHasNoTrailingSlash() {
        UrlShortenerProperties properties = new UrlShortenerProperties();
        properties.setBaseUrl("http://localhost:8080");

        String shortUrl = properties.buildShortUrl("AbC123x");

        assertThat(shortUrl).isEqualTo("http://localhost:8080/AbC123x");
    }
}
