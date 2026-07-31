package com.schwab.urlshortener.config;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.schwab.urlshortener.controller.RedirectController;
import com.schwab.urlshortener.controller.SecurityController;
import com.schwab.urlshortener.controller.UrlController;
import com.schwab.urlshortener.dto.CreateShortUrlRequest;
import com.schwab.urlshortener.dto.ShortUrlResponse;
import com.schwab.urlshortener.service.UrlShorteningService;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import jakarta.servlet.http.Cookie;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest({UrlController.class, RedirectController.class, SecurityController.class})
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UrlShorteningService urlShorteningService;

    @Test
    void shouldRequireCsrfForCreateEndpoint() throws Exception {
        CreateShortUrlRequest request = new CreateShortUrlRequest("https://example.com/articles/123", null);

        mockMvc.perform(post("/api/v1/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldExposeCsrfTokenForManualAndBrowserClients() throws Exception {
        mockMvc.perform(get("/api/v1/security/csrf"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"))
                .andExpect(jsonPath("$.headerName").value("X-XSRF-TOKEN"))
                .andExpect(jsonPath("$.parameterName").value("_csrf"))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void shouldAllowCreateEndpointWithFetchedCsrfCookieAndHeader() throws Exception {
        MvcResult csrfResult = mockMvc.perform(get("/api/v1/security/csrf"))
                .andExpect(status().isOk())
                .andReturn();
        String token = JsonPath.read(csrfResult.getResponse().getContentAsString(), "$.token");
        Cookie csrfCookie = csrfResult.getResponse().getCookie("XSRF-TOKEN");

        OffsetDateTime createdAt = OffsetDateTime.parse("2026-07-30T10:00:00Z");
        CreateShortUrlRequest request = new CreateShortUrlRequest("https://example.com/articles/123", null);
        ShortUrlResponse response = new ShortUrlResponse(
                "https://example.com/articles/123",
                "AbC123x",
                "http://localhost:8080/AbC123x",
                createdAt,
                null,
                true
        );
        when(urlShorteningService.createShortUrl(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(csrfCookie)
                        .header("X-XSRF-TOKEN", token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost:8080/AbC123x"));
    }

    @Test
    void shouldAllowCreateEndpointWithCsrf() throws Exception {
        OffsetDateTime createdAt = OffsetDateTime.parse("2026-07-30T10:00:00Z");
        CreateShortUrlRequest request = new CreateShortUrlRequest("https://example.com/articles/123", null);
        ShortUrlResponse response = new ShortUrlResponse(
                "https://example.com/articles/123",
                "AbC123x",
                "http://localhost:8080/AbC123x",
                createdAt,
                null,
                true
        );
        when(urlShorteningService.createShortUrl(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost:8080/AbC123x"))
                .andExpect(header().string("X-Content-Type-Options", "nosniff"));
    }

    @Test
    void shouldAllowPublicRedirectWithoutCsrf() throws Exception {
        when(urlShorteningService.resolveRedirectUrl("AbC123x"))
                .thenReturn("https://example.com/articles/123");

        mockMvc.perform(get("/AbC123x"))
                .andExpect(status().isFound())
                .andExpect(header().string(HttpHeaders.LOCATION, "https://example.com/articles/123"));
    }

    @Test
    void shouldDenyUnrecognizedEndpoints() throws Exception {
        mockMvc.perform(get("/api/v1/admin"))
                .andExpect(status().isForbidden());
    }
}
