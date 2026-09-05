package com.saiyam.urlshortner.service;

import com.saiyam.urlshortner.dto.ShortenUrlRequest;
import com.saiyam.urlshortner.dto.ShortenUrlResponse;
import org.springframework.stereotype.Service;

@Service
public class UrlShorteningServiceImpl implements UrlShorteningService {

    @Override
    public ShortenUrlResponse shorten(ShortenUrlRequest request) {
        return new ShortenUrlResponse("TODO");
    }
}