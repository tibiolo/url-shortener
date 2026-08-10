package com.urlshortener.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UrlResponseDto(UUID id, String originalUrl, String shortUrl, LocalDateTime createdAt,
                             LocalDateTime expiresAt) {
}
