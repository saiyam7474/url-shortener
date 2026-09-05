package com.saiyam.urlshortner.model;

public record UrlMapping(
        String shortCode,
        String originalUrl) {
}