package com.schwab.urlshortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.List;

@Schema(description = "Standard API error response")
public record ApiErrorResponse(
        @Schema(description = "Error timestamp", example = "2026-07-30T10:00:00Z")
        OffsetDateTime timestamp,
        @Schema(description = "HTTP status code", example = "400")
        int status,
        @Schema(description = "HTTP reason phrase", example = "Bad Request")
        String error,
        @Schema(description = "Human-readable error message", example = "Request validation failed")
        String message,
        @Schema(description = "Request path", example = "/api/v1/urls")
        String path,
        @Schema(description = "Field-level validation errors")
        List<FieldErrorResponse> fieldErrors
) {
}
