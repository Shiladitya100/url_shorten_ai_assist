package com.schwab.urlshortener.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schwab.urlshortener.dto.CreateShortUrlRequest;
import com.schwab.urlshortener.dto.ShortUrlResponse;
import com.schwab.urlshortener.dto.UrlAnalyticsResponse;
import com.schwab.urlshortener.exception.UrlMappingNotFoundException;
import com.schwab.urlshortener.service.UrlShorteningService;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UrlController.class)
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UrlShorteningService urlShorteningService;

    @Test
    void shouldCreateShortUrl() throws Exception {
        OffsetDateTime createdAt = OffsetDateTime.parse("2026-07-30T10:00:00Z");
        OffsetDateTime expiresAt = OffsetDateTime.parse("2026-08-06T10:00:00Z");
        CreateShortUrlRequest request = new CreateShortUrlRequest("https://example.com/articles/123", expiresAt);
        ShortUrlResponse response = new ShortUrlResponse(
                "https://example.com/articles/123",
                "AbC123x",
                "http://localhost:8080/AbC123x",
                createdAt,
                expiresAt,
                true
        );
        when(urlShorteningService.createShortUrl(any(CreateShortUrlRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost:8080/AbC123x"))
                .andExpect(jsonPath("$.originalUrl").value("https://example.com/articles/123"))
                .andExpect(jsonPath("$.shortCode").value("AbC123x"))
                .andExpect(jsonPath("$.shortUrl").value("http://localhost:8080/AbC123x"))
                .andExpect(jsonPath("$.active").value(true));

        verify(urlShorteningService).createShortUrl(any(CreateShortUrlRequest.class));
    }

    @Test
    void shouldRejectInvalidOriginalUrl() throws Exception {
        CreateShortUrlRequest request = new CreateShortUrlRequest("not-a-url", null);

        mockMvc.perform(post("/api/v1/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectPastExpiration() throws Exception {
        CreateShortUrlRequest request = new CreateShortUrlRequest(
                "https://example.com/articles/123",
                OffsetDateTime.parse("2026-07-29T10:00:00Z")
        );

        mockMvc.perform(post("/api/v1/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnAnalytics() throws Exception {
        UrlAnalyticsResponse response = new UrlAnalyticsResponse(
                "AbC123x",
                "https://example.com/articles/123",
                5,
                OffsetDateTime.parse("2026-07-29T10:00:00Z"),
                OffsetDateTime.parse("2026-08-06T10:00:00Z"),
                OffsetDateTime.parse("2026-07-30T09:30:00Z"),
                true,
                false
        );
        when(urlShorteningService.getAnalytics("AbC123x")).thenReturn(response);

        mockMvc.perform(get("/api/v1/urls/AbC123x/analytics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortCode").value("AbC123x"))
                .andExpect(jsonPath("$.originalUrl").value("https://example.com/articles/123"))
                .andExpect(jsonPath("$.accessCount").value(5))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.expired").value(false));
    }

    @Test
    void shouldReturnNotFoundForMissingAnalyticsShortCode() throws Exception {
        when(urlShorteningService.getAnalytics("missing"))
                .thenThrow(new UrlMappingNotFoundException("missing"));

        mockMvc.perform(get("/api/v1/urls/missing/analytics"))
                .andExpect(status().isNotFound());
    }
}
