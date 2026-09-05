package com.saiyam.urlshortner.persistence;

import com.saiyam.urlshortner.model.UrlMapping;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "url_mappings")
public class UrlMappingEntity {

    @Id
    private String shortCode;

    private String originalUrl;

    protected UrlMappingEntity() {
    }

    public UrlMappingEntity(String shortCode, String originalUrl) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
    }

}