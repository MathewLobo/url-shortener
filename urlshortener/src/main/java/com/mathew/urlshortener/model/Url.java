package com.mathew.urlshortener.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "urls")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalUrl;

    @Column(unique = true, nullable = false)
    private String shortCode;

    private Long clickCount = 0L;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}