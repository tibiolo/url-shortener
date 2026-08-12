package com.urlshortener.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UrlResponseDto(UUID id, String originalUrl, String shortCode, LocalDateTime createdAt,
                             LocalDateTime expiresAt) {
}
