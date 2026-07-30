package com.schwab.urlshortener.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.schwab.urlshortener.exception.UrlMappingExpiredException;
import com.schwab.urlshortener.exception.UrlMappingNotFoundException;
import com.schwab.urlshortener.exception.UrlMappingNotRedirectableException;
import com.schwab.urlshortener.service.UrlShorteningService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RedirectController.class)
class RedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlShorteningService urlShorteningService;

    @Test
    void shouldRedirectToOriginalUrl() throws Exception {
        when(urlShorteningService.resolveRedirectUrl("AbC123x"))
                .thenReturn("https://example.com/articles/123");

        mockMvc.perform(get("/AbC123x"))
                .andExpect(status().isFound())
                .andExpect(header().string(HttpHeaders.LOCATION, "https://example.com/articles/123"));
    }

    @Test
    void shouldReturnNotFoundWhenShortCodeDoesNotExist() throws Exception {
        when(urlShorteningService.resolveRedirectUrl("missing"))
                .thenThrow(new UrlMappingNotFoundException("missing"));

        mockMvc.perform(get("/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("URL mapping not found for short code: missing"))
                .andExpect(jsonPath("$.path").value("/missing"));
    }

    @Test
    void shouldReturnGoneWhenShortCodeIsExpired() throws Exception {
        when(urlShorteningService.resolveRedirectUrl("expired"))
                .thenThrow(new UrlMappingExpiredException("expired"));

        mockMvc.perform(get("/expired"))
                .andExpect(status().isGone())
                .andExpect(jsonPath("$.status").value(410))
                .andExpect(jsonPath("$.error").value("Gone"))
                .andExpect(jsonPath("$.message").value("URL mapping expired for short code: expired"))
                .andExpect(jsonPath("$.path").value("/expired"));
    }

    @Test
    void shouldReturnNotFoundWhenShortCodeIsInactive() throws Exception {
        when(urlShorteningService.resolveRedirectUrl("inactive"))
                .thenThrow(new UrlMappingNotRedirectableException("inactive"));

        mockMvc.perform(get("/inactive"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("URL mapping is not redirectable for short code: inactive"))
                .andExpect(jsonPath("$.path").value("/inactive"));
    }
}
