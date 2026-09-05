package com.saiyam.urlshortner.controller;

import com.saiyam.urlshortner.dto.ShortenUrlRequest;
import com.saiyam.urlshortner.dto.ShortenUrlResponse;
import com.saiyam.urlshortner.service.UrlShorteningService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;


@RestController
public class UrlController {

    private final UrlShorteningService urlShorteningService;

    public UrlController(UrlShorteningService urlShorteningService) {
        this.urlShorteningService = urlShorteningService;
    }

    @PostMapping("/shorten")
    public ShortenUrlResponse shorten(@RequestBody ShortenUrlRequest request) {
        return urlShorteningService.shorten(request);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Object> redirect(@PathVariable String code) {
        return urlShorteningService.getOriginalUrl(code)
                .map(url -> ResponseEntity.status(301)
                        .header(HttpHeaders.LOCATION, url)
                        .build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}