package com.saiyam.urlshortner.repository;

import com.saiyam.urlshortner.model.UrlMapping;

import java.util.Optional;

public interface UrlRepository {

    UrlMapping save(UrlMapping mapping);

    Optional<UrlMapping> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);
}