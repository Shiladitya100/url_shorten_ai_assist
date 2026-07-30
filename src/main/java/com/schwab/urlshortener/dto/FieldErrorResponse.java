package com.schwab.urlshortener.dto;

public record FieldErrorResponse(
        String field,
        String message
) {
}
