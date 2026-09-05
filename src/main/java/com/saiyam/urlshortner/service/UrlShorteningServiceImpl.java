package com.saiyam.urlshortner.service;

import com.saiyam.urlshortner.dto.ShortenUrlRequest;
import com.saiyam.urlshortner.dto.ShortenUrlResponse;
import com.saiyam.urlshortner.exception.InvalidUrlException;
import com.saiyam.urlshortner.generator.ShortCodeGenerator;
import com.saiyam.urlshortner.model.UrlMapping;
import com.saiyam.urlshortner.repository.UrlRepository;
import org.springframework.stereotype.Service;
import com.saiyam.urlshortner.validation.UrlValidator;

import java.util.Optional;

@Service
public class UrlShorteningServiceImpl implements UrlShorteningService {

    private final UrlRepository urlRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final UrlValidator urlValidator;

    public UrlShorteningServiceImpl(
            UrlRepository urlRepository,
            ShortCodeGenerator shortCodeGenerator,
            com.saiyam.urlshortner.validation.UrlValidator urlValidator) {
        this.urlRepository = urlRepository;
        this.shortCodeGenerator = shortCodeGenerator;
        this.urlValidator = urlValidator;
    }

    @Override
    public ShortenUrlResponse shorten(ShortenUrlRequest request) {

        if (!urlValidator.isValid(request.url())) {
            throw new InvalidUrlException("Invalid URL passed");
        }

        String shortCode;

        do {
            shortCode = shortCodeGenerator.generate();
        } while (urlRepository.existsByShortCode(shortCode));

        UrlMapping mapping = new UrlMapping(
                shortCode,
                request.url()
        );

        urlRepository.save(mapping);

        return new ShortenUrlResponse(shortCode);
    }

    @Override
    public Optional<String> getOriginalUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .map(UrlMapping::originalUrl);
    }
}