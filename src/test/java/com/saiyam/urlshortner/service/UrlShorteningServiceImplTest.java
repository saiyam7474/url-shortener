package com.saiyam.urlshortner.service;

import com.saiyam.urlshortner.dto.ShortenUrlRequest;
import com.saiyam.urlshortner.dto.ShortenUrlResponse;
import com.saiyam.urlshortner.exception.AliasAlreadyExistsException;
import com.saiyam.urlshortner.exception.InvalidUrlException;
import com.saiyam.urlshortner.generator.ShortCodeGenerator;
import com.saiyam.urlshortner.model.UrlMapping;
import com.saiyam.urlshortner.repository.UrlRepository;
import com.saiyam.urlshortner.validation.UrlValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UrlShorteningServiceImplTest {

    private UrlRepository urlRepository;
    private ShortCodeGenerator shortCodeGenerator;
    private UrlValidator urlValidator;

    private UrlShorteningServiceImpl service;

    @BeforeEach
    void setUp() {
        urlRepository = mock(UrlRepository.class);
        shortCodeGenerator = mock(ShortCodeGenerator.class);
        urlValidator = mock(UrlValidator.class);

        service = new UrlShorteningServiceImpl(
                urlRepository,
                shortCodeGenerator,
                urlValidator
        );
    }

    @Test
    void shouldShortenValidUrl() {
        String url = "https://example.com/test";

        when(urlValidator.isValid(url)).thenReturn(true);
        when(shortCodeGenerator.generate()).thenReturn("abc123");
        when(urlRepository.existsByShortCode("abc123")).thenReturn(false);

        ShortenUrlResponse response =
                service.shorten(new ShortenUrlRequest(url, null));

        assertEquals("abc123", response.shortCode());

        verify(urlRepository).save(
                new UrlMapping("abc123", url)
        );
    }

    @Test
    void shouldRejectInvalidUrl() {
        String url = "hello";

        when(urlValidator.isValid(url)).thenReturn(false);

        assertThrows(
                InvalidUrlException.class,
                () -> service.shorten(new ShortenUrlRequest(url, null))
        );

        verifyNoInteractions(shortCodeGenerator);
        verifyNoInteractions(urlRepository);
    }

    @Test
    void shouldUseCustomAlias() {
        String url = "https://example.com/test";

        when(urlValidator.isValid(url)).thenReturn(true);
        when(urlRepository.existsByShortCode("my-link")).thenReturn(false);

        ShortenUrlResponse response =
                service.shorten(new ShortenUrlRequest(url, "my-link"));

        assertEquals("my-link", response.shortCode());

        verify(urlRepository).save(
                new UrlMapping("my-link", url)
        );

        verifyNoInteractions(shortCodeGenerator);
    }

    @Test
    void shouldRejectDuplicateCustomAlias() {
        String url = "https://example.com/test";

        when(urlValidator.isValid(url)).thenReturn(true);
        when(urlRepository.existsByShortCode("my-link")).thenReturn(true);

        assertThrows(
                AliasAlreadyExistsException.class,
                () -> service.shorten(
                        new ShortenUrlRequest(url, "my-link")
                )
        );

        verify(urlRepository, never()).save(any());
    }

    @Test
    void shouldRegenerateWhenGeneratedCodeAlreadyExists() {
        String url = "https://example.com/test";

        when(urlValidator.isValid(url)).thenReturn(true);

        when(shortCodeGenerator.generate())
                .thenReturn("abc123")
                .thenReturn("xyz789");

        when(urlRepository.existsByShortCode("abc123"))
                .thenReturn(true);

        when(urlRepository.existsByShortCode("xyz789"))
                .thenReturn(false);

        ShortenUrlResponse response =
                service.shorten(new ShortenUrlRequest(url, null));

        assertEquals("xyz789", response.shortCode());

        verify(shortCodeGenerator, times(2)).generate();
        verify(urlRepository).save(
                new UrlMapping("xyz789", url)
        );
    }

    @Test
    void shouldRedirectWhenCodeExists() {
        when(urlRepository.findByShortCode("abc123"))
                .thenReturn(Optional.of(
                        new UrlMapping(
                                "abc123",
                                "https://example.com/test"
                        )
                ));

        Optional<String> result =
                service.getOriginalUrl("abc123");

        assertTrue(result.isPresent());
        assertEquals(
                "https://example.com/test",
                result.get()
        );
    }

    @Test
    void shouldReturnEmptyWhenCodeDoesNotExist() {
        when(urlRepository.findByShortCode("missing"))
                .thenReturn(Optional.empty());

        Optional<String> result =
                service.getOriginalUrl("missing");

        assertTrue(result.isEmpty());
    }
}