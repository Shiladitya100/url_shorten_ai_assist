package com.schwab.urlshortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "CSRF token response for browser or manual API clients")
public record CsrfTokenResponse(
        @Schema(description = "HTTP header name that must carry the CSRF token", example = "X-XSRF-TOKEN")
        String headerName,

        @Schema(description = "Request parameter name accepted by Spring Security", example = "_csrf")
        String parameterName,

        @Schema(description = "CSRF token value")
        String token
) {
}
