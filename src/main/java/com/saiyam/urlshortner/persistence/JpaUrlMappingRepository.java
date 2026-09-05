package com.saiyam.urlshortner.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUrlMappingRepository extends JpaRepository<UrlMappingEntity, String> {
}