package com.saiyam.urlshortner.service;

import com.saiyam.urlshortner.dto.ShortenUrlRequest;
import com.saiyam.urlshortner.dto.ShortenUrlResponse;
import com.saiyam.urlshortner.model.UrlMapping;
import com.saiyam.urlshortner.repository.UrlRepository;
import org.springframework.stereotype.Service;

@Service
public class UrlShorteningServiceImpl implements UrlShorteningService {

    private final UrlRepository urlRepository;

    public UrlShorteningServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public ShortenUrlResponse shorten(ShortenUrlRequest request) {
        String shortCode = "temp123";

        UrlMapping mapping = new UrlMapping(
                shortCode,
                request.url()
        );

        urlRepository.save(mapping);

        return new ShortenUrlResponse(shortCode);
    }
}