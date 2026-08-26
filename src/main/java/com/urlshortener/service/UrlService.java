package com.urlshortener.service;

import com.urlshortener.dto.CreateUrlRequestDto;
import com.urlshortener.dto.UrlResponseDto;
import com.urlshortener.entity.Url;
import com.urlshortener.exception.UrlNotFoundException;
import com.urlshortener.mapper.UrlMapper;
import com.urlshortener.repository.UrlRepository;
import com.urlshortener.util.ShortCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final UrlMapper urlMapper;
    private final ShortCodeGenerator shortCodeGenerator;

    public UrlResponseDto createUrl(CreateUrlRequestDto request) {
        Url url = urlMapper.toEntity(request);

        String shortCode;

        do {
            shortCode = shortCodeGenerator.generate();
        } while (urlRepository.existsByShortCode(shortCode));

        url.setShortCode(shortCode);

        Url savedUrl = urlRepository.save(url);

        return urlMapper.toResponse(savedUrl);
    }

    public Url getUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode).orElseThrow(() -> new UrlNotFoundException("Url Not Found"));
    }

}
