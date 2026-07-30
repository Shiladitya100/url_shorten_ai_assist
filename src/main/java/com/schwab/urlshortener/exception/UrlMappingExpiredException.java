package com.schwab.urlshortener.exception;

public class UrlMappingExpiredException extends RuntimeException {

    public UrlMappingExpiredException(String shortCode) {
        super("URL mapping expired for short code: " + shortCode);
    }
}
