package com.saiyam.urlshortner.service;

import com.saiyam.urlshortner.model.ShortenUrlRequest;
import com.saiyam.urlshortner.model.ShortenUrlResponse;

public interface UrlShorteningService {

    ShortenUrlResponse shorten(ShortenUrlRequest request);
}