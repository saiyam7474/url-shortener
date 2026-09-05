package com.saiyam.urlshortner.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UrlControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldShortenUrl() throws Exception {
        mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "url": "https://example.com/test"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectInvalidUrl() throws Exception {
        mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "url": "hello"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRedirectUsingShortCode() throws Exception {
        createAlias("redirect-test", "https://example.com/redirect-test");

        mockMvc.perform(get("/redirect-test"))
                .andExpect(status().isMovedPermanently())
                .andExpect(header().string(
                        "Location",
                        "https://example.com/redirect-test"
                ));
    }

    @Test
    void shouldReturn404ForUnknownCode() throws Exception {
        mockMvc.perform(get("/unknown-code"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateUrlWithCustomAlias() throws Exception {
        createAlias("custom-test", "https://example.com/custom-test");
    }

    @Test
    void shouldRejectDuplicateAlias() throws Exception {
        createAlias("duplicate-test", "https://example.com/first");

        mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "url": "https://example.com/second",
                                    "customAlias": "duplicate-test"
                                }
                                """))
                .andExpect(status().isConflict());
    }

    private void createAlias(String alias, String url) throws Exception {
        mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "url": "%s",
                                    "customAlias": "%s"
                                }
                                """.formatted(url, alias)))
                .andExpect(status().isOk());
    }
}