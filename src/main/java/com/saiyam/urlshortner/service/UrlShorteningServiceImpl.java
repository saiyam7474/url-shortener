package com.saiyam.urlshortner.service;

import com.saiyam.urlshortner.dto.ShortenUrlRequest;
import com.saiyam.urlshortner.dto.ShortenUrlResponse;
import com.saiyam.urlshortner.generator.ShortCodeGenerator;
import com.saiyam.urlshortner.model.UrlMapping;
import com.saiyam.urlshortner.repository.UrlRepository;
import org.springframework.stereotype.Service;

@Service
public class UrlShorteningServiceImpl implements UrlShorteningService {

    private final UrlRepository urlRepository;
    private final ShortCodeGenerator shortCodeGenerator;

    public UrlShorteningServiceImpl(
            UrlRepository urlRepository,
            ShortCodeGenerator shortCodeGenerator) {
        this.urlRepository = urlRepository;
        this.shortCodeGenerator = shortCodeGenerator;
    }

    @Override
    public ShortenUrlResponse shorten(ShortenUrlRequest request) {
        String shortCode = shortCodeGenerator.generate();

        UrlMapping mapping = new UrlMapping(
                shortCode,
                request.url()
        );

        urlRepository.save(mapping);

        return new ShortenUrlResponse(shortCode);
    }
}