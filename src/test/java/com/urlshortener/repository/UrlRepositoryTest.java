package com.urlshortener.repository;

import com.urlshortener.entity.Url;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class UrlRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17");

    @Autowired
    private UrlRepository urlRepository;

    @BeforeEach
    void setUp() {
        urlRepository.deleteAll();
    }

    @Test
    @DisplayName("Save url successfully")
    void shouldSaveUrl() {
        Url url = new Url();

        url.setOriginalUrl("https://www.google.com/");
        url.setShortCode("abc123");

        Url savedUrl = urlRepository.save(url);

        assertThat(savedUrl.getId()).isNotNull();
    }

    @Test
    @DisplayName("Find url using shortcode successfully")
    void shouldFindUrl_ByShortCode() {
        Url url = new Url();

        url.setOriginalUrl("https://www.google.com/");
        url.setShortCode("abc123");

        urlRepository.save(url);

        Optional<Url> result = urlRepository.findByShortCode("abc123");

        assertThat(result).isPresent();
        assertThat(result.get().getOriginalUrl()).isEqualTo(url.getOriginalUrl());
    }

    @Test
    @DisplayName("Return empty if shortcode doesn't exist")
    void shouldReturnEmpty_IfShortCode_DoesNotExist() {
        Optional<Url> result = urlRepository.findByShortCode("does not exist");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Return true if shortcode exists")
    void shouldReturnTrue_IfShortCode_Exists() {
        Url url = new Url();

        url.setOriginalUrl("https://www.google.com/");
        url.setShortCode("abc123");

        urlRepository.save(url);

        assertThat(urlRepository.existsByShortCode("abc123")).isTrue();
    }

    @Test
    @DisplayName("Return false if shortcode doesn't exist")
    void ReturnFalse_IfShortCode_DoesNotExist() {
        assertThat(urlRepository.existsByShortCode("does_not_exist")).isFalse();
    }

}
