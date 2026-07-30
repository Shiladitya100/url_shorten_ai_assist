package com.schwab.urlshortener.exception;

public class UrlMappingNotFoundException extends RuntimeException {

    public UrlMappingNotFoundException(String shortCode) {
        super("URL mapping not found for short code: " + shortCode);
    }
}
