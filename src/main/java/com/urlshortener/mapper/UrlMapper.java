package com.urlshortener.mapper;

import com.urlshortener.dto.CreateUrlRequestDto;
import com.urlshortener.dto.UrlResponseDto;
import com.urlshortener.entity.Url;
import org.springframework.stereotype.Component;

@Component
public class UrlMapper {

    public Url toEntity(CreateUrlRequestDto request) {
        Url url = new Url();

        url.setOriginalUrl(request.originalUrl());
        url.setExpiresAt(request.expiresAt());

        return url;
    }

    public UrlResponseDto toResponse(Url url) {
        return new UrlResponseDto(url.getId(), url.getOriginalUrl(), url.getShortCode(), url.getCreatedAt(), url.getExpiresAt());
    }

}
