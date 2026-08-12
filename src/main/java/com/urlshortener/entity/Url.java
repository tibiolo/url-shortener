package com.urlshortener.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Urls")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String originalUrl;

    @Column(nullable = false, unique = true, length = 10)
    private String shortCode;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime expiresAt;
}
