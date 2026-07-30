package com.schwab.urlshortener.service;

import com.schwab.urlshortener.dto.CreateShortUrlRequest;
import com.schwab.urlshortener.dto.ShortUrlResponse;

public interface UrlShorteningService {

    ShortUrlResponse createShortUrl(CreateShortUrlRequest request);
}
