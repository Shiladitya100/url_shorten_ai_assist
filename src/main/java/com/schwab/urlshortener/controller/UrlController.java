package com.schwab.urlshortener.controller;

import com.schwab.urlshortener.dto.CreateShortUrlRequest;
import com.schwab.urlshortener.dto.ShortUrlResponse;
import com.schwab.urlshortener.dto.UrlAnalyticsResponse;
import com.schwab.urlshortener.service.UrlShorteningService;
import com.schwab.urlshortener.validation.ShortCodeRules;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/urls")
@Validated
@Tag(name = "URLs", description = "Create and inspect shortened URLs")
public class UrlController {

    private final UrlShorteningService urlShorteningService;

    public UrlController(UrlShorteningService urlShorteningService) {
        this.urlShorteningService = urlShorteningService;
    }

    @PostMapping
    @Operation(
            summary = "Create a short URL",
            description = "Creates a short code for a valid HTTPS original URL. Optional expiration must be in the future.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Short URL created"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content = @Content(schema = @Schema(implementation = com.schwab.urlshortener.dto.ApiErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Short-code generation exhausted retry attempts",
                            content = @Content(schema = @Schema(implementation = com.schwab.urlshortener.dto.ApiErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<ShortUrlResponse> createShortUrl(@Valid @RequestBody CreateShortUrlRequest request) {
        ShortUrlResponse response = urlShorteningService.createShortUrl(request);
        return ResponseEntity.created(URI.create(response.shortUrl())).body(response);
    }

    @GetMapping("/{shortCode}/analytics")
    @Operation(
            summary = "Get URL analytics",
            description = "Returns aggregate analytics for a short code.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Analytics returned"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid short code format",
                            content = @Content(schema = @Schema(implementation = com.schwab.urlshortener.dto.ApiErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Short code not found",
                            content = @Content(schema = @Schema(implementation = com.schwab.urlshortener.dto.ApiErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<UrlAnalyticsResponse> getAnalytics(
            @Parameter(description = "7-character Base62 short code", example = "AbC123x")
            @PathVariable @Pattern(regexp = ShortCodeRules.PATTERN, message = ShortCodeRules.VALIDATION_MESSAGE)
            String shortCode
    ) {
        return ResponseEntity.ok(urlShorteningService.getAnalytics(shortCode));
    }
}
