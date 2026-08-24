package com.urlshortener.service;

import com.urlshortener.dto.CreateUrlRequestDto;
import com.urlshortener.dto.UrlResponseDto;
import com.urlshortener.entity.Url;
import com.urlshortener.mapper.UrlMapper;
import com.urlshortener.repository.UrlRepository;
import com.urlshortener.util.ShortCodeGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private UrlMapper urlMapper;

    @Mock
    private ShortCodeGenerator shortCodeGenerator;

    @InjectMocks
    private UrlService urlService;

    @Test
    @DisplayName("Create url successfully")
    void CreateUrl() {
        CreateUrlRequestDto request = new CreateUrlRequestDto("https://www.google.com/", LocalDateTime.now().plusDays(7));

        Url url = new Url();

        Url savedUrl = new Url();

        savedUrl.setId(UUID.randomUUID());
        savedUrl.setOriginalUrl("https://www.google.com/");
        savedUrl.setShortCode("abc123");

        UrlResponseDto response = new UrlResponseDto(savedUrl.getId(), savedUrl.getOriginalUrl(), savedUrl.getShortCode(), savedUrl.getCreatedAt(), savedUrl.getExpiresAt());

        when(urlMapper.toEntity(request)).thenReturn(url);

        when(shortCodeGenerator.generate()).thenReturn("abc123");

        when(urlRepository.existsByShortCode("abc123")).thenReturn(false);

        when(urlRepository.save(url)).thenReturn(savedUrl);

        when(urlMapper.toResponse(savedUrl)).thenReturn(response);

        UrlResponseDto result = urlService.createUrl(request);

        assertThat(result).isSameAs(response);

        assertThat(url.getShortCode()).isEqualTo("abc123");

        verify(urlMapper).toEntity(request);
        verify(shortCodeGenerator).generate();
        verify(urlRepository).existsByShortCode("abc123");
        verify(urlRepository).save(url);
        verify(urlMapper).toResponse(savedUrl);
    }

    @Test
    @DisplayName("Return url if shortcode exists")
    void ReturnUrl_WhenShortCodeExists() {
        String shortCode = "abc123";

        Url url = new Url();
        url.setOriginalUrl("https://www.google.com/");
        url.setShortCode("abc123");

        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(url));

        Url result = urlService.getUrl(shortCode);

        assertThat(result).isSameAs(url);

        verify(urlRepository).findByShortCode(shortCode);
    }

    @Test
    @DisplayName("Throw exception if shortcode doesn't exist")
    void ThrowException_WhenShortCode_DoesNotExist() {
        String shortCode = "abc123";

        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> urlService.getUrl(shortCode)).isInstanceOf(RuntimeException.class).hasMessage("Url Not Found");

        verify(urlRepository).findByShortCode(shortCode);
    }
}
