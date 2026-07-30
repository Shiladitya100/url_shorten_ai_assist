package com.schwab.urlshortener.service;

import com.schwab.urlshortener.dto.CreateShortUrlRequest;
import com.schwab.urlshortener.dto.ShortUrlResponse;
import com.schwab.urlshortener.dto.UrlAnalyticsResponse;

public interface UrlShorteningService {

    ShortUrlResponse createShortUrl(CreateShortUrlRequest request);

    String resolveRedirectUrl(String shortCode);

    UrlAnalyticsResponse getAnalytics(String shortCode);
}
