package com.mathew.urlshortener.dto;

import com.mathew.urlshortener.model.Url;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UrlResponse {

    private String shortCode;
    private String shortUrl;
    private String originalUrl;
    private Long clickCount;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public UrlResponse(Url url, String baseUrl) {
        this.shortCode = url.getShortCode();
        this.shortUrl = baseUrl + "/" + url.getShortCode();
        this.originalUrl = url.getOriginalUrl();
        this.clickCount = url.getClickCount();
        this.createdAt = url.getCreatedAt();
        this.expiresAt = url.getExpiresAt();
    }
}