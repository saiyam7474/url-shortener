package com.saiyam.urlshortner.dto;

public record ShortenUrlRequest(
        String url,
        String customAlias) {
}