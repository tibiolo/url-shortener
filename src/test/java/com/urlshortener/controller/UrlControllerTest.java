package com.urlshortener.controller;

import com.urlshortener.dto.CreateUrlRequestDto;
import com.urlshortener.dto.UrlResponseDto;
import com.urlshortener.entity.Url;
import com.urlshortener.service.UrlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UrlController.class)
public class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlService urlService;

    @Test
    @DisplayName("Creates and returns short url")
    void createUrl() throws Exception {
        UrlResponseDto response = new UrlResponseDto(UUID.randomUUID(), "https://www.google.com/", "abc123", LocalDateTime.now(), LocalDateTime.now().plusDays(7));

        when(urlService.createUrl(any(CreateUrlRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content("""
                {
                    "originalUrl": "https://www.google.com/",
                    "expiresAt": "2026-12-31T23:59:59"
                }
                """)).andExpect(status().isCreated()).andExpect(jsonPath("$.originalUrl").value("https://www.google.com/")).andExpect(jsonPath("$.shortCode").value("abc123"));

        verify(urlService).createUrl(any(CreateUrlRequestDto.class));
    }

    @Test
    @DisplayName("Responds with the original full url")
    void getUrl() throws Exception {
        Url url = new Url();

        url.setOriginalUrl("https://www.google.com/");
        url.setShortCode("abc123");

        when(urlService.getUrl("abc123")).thenReturn(url);

        mockMvc.perform(get("/abc123")).andExpect(status().isFound()).andExpect(header().string("Location", "https://www.google.com/"));

        verify(urlService).getUrl("abc123");

    }


}
