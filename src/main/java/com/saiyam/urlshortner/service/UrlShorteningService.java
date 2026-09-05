package com.saiyam.urlshortner.service;

import com.saiyam.urlshortner.dto.ShortenUrlRequest;
import com.saiyam.urlshortner.dto.ShortenUrlResponse;

public interface UrlShorteningService {

    ShortenUrlResponse shorten(ShortenUrlRequest request);
}