package com.schwab.urlshortener.config;

import static org.assertj.core.api.Assertions.assertThat;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

class OpenApiConfigTest {

    private final OpenApiConfig config = new OpenApiConfig();

    @Test
    void shouldConfigureOpenApiMetadata() {
        OpenAPI openAPI = config.urlShortenerOpenApi();

        assertThat(openAPI.getInfo().getTitle()).isEqualTo("URL Shortener API");
        assertThat(openAPI.getInfo().getVersion()).isEqualTo("v1");
        assertThat(openAPI.getInfo().getDescription()).contains("create", "redirect", "analytics");
        assertThat(openAPI.getInfo().getLicense().getName()).isEqualTo("Internal Evaluation");
    }
}
