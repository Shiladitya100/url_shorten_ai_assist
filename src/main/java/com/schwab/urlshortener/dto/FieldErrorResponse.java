package com.schwab.urlshortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Field-level validation error")
public record FieldErrorResponse(
        @Schema(description = "Field or parameter path", example = "originalUrl")
        String field,
        @Schema(description = "Validation message", example = "must be a valid URL")
        String message
) {
}
