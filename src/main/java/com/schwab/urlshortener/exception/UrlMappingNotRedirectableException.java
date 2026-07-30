package com.schwab.urlshortener.exception;

public class UrlMappingNotRedirectableException extends RuntimeException {

    public UrlMappingNotRedirectableException(String shortCode) {
        super("URL mapping is not redirectable for short code: " + shortCode);
    }
}
