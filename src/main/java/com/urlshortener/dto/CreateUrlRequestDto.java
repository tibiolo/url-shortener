package com.urlshortener.dto;

import java.time.LocalDateTime;

public record CreateUrlRequestDto(String originalUrl, LocalDateTime expiresAt) {
}
