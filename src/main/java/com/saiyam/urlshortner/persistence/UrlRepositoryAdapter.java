package com.saiyam.urlshortner.persistence;

import com.saiyam.urlshortner.model.UrlMapping;
import com.saiyam.urlshortner.repository.UrlRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UrlRepositoryAdapter implements UrlRepository {

    private final JpaUrlMappingRepository jpaRepository;

    public UrlRepositoryAdapter(JpaUrlMappingRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public UrlMapping save(UrlMapping mapping) {
        UrlMappingEntity entity =
                new UrlMappingEntity(mapping.shortCode(), mapping.originalUrl());

        UrlMappingEntity saved = jpaRepository.save(entity);

        return new UrlMapping(
                saved.getShortCode(),
                saved.getOriginalUrl()
        );
    }

    @Override
    public Optional<UrlMapping> findByShortCode(String shortCode) {
        return jpaRepository.findById(shortCode)
                .map(entity ->
                        new UrlMapping(
                                entity.getShortCode(),
                                entity.getOriginalUrl()
                        ));
    }
}