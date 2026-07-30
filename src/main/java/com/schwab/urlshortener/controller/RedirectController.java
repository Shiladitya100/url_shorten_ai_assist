package com.schwab.urlshortener.controller;

import com.schwab.urlshortener.service.UrlShorteningService;
import com.schwab.urlshortener.validation.ShortCodeRules;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Tag(name = "Redirects", description = "Resolve shortened URLs")
public class RedirectController {

    private final UrlShorteningService urlShorteningService;

    public RedirectController(UrlShorteningService urlShorteningService) {
        this.urlShorteningService = urlShorteningService;
    }

    @GetMapping("/{shortCode}")
    @Operation(
            summary = "Redirect a short URL",
            description = "Redirects an active, non-expired short code to its original URL and records a successful access.",
            responses = {
                    @ApiResponse(responseCode = "302", description = "Redirect to original URL", content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid short code format",
                            content = @Content(schema = @Schema(implementation = com.schwab.urlshortener.dto.ApiErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Short code not found or inactive",
                            content = @Content(schema = @Schema(implementation = com.schwab.urlshortener.dto.ApiErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "410",
                            description = "Short code is expired",
                            content = @Content(schema = @Schema(implementation = com.schwab.urlshortener.dto.ApiErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<Void> redirect(
            @Parameter(description = "7-character Base62 short code", example = "AbC123x")
            @PathVariable @Pattern(regexp = ShortCodeRules.PATTERN, message = ShortCodeRules.VALIDATION_MESSAGE)
            String shortCode
    ) {
        String originalUrl = urlShorteningService.resolveRedirectUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}
