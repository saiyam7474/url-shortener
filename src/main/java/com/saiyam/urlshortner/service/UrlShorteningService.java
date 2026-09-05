package com.saiyam.urlshortner.service;

import com.saiyam.urlshortner.dto.ShortenUrlRequest;
import com.saiyam.urlshortner.dto.ShortenUrlResponse;

import java.util.Optional;

public interface UrlShorteningService {

    ShortenUrlResponse shorten(ShortenUrlRequest request);

    Optional<String> getOriginalUrl(String shortCode);
}