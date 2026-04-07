package com.mathew.urlshortener.service;

import com.mathew.urlshortener.dto.ShortenRequest;
import com.mathew.urlshortener.model.Url;
import com.mathew.urlshortener.repository.UrlRepository;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    private static final String CHARS =
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    private String generateShortCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    public Url shortenUrl(ShortenRequest req) {
        String code;
        do {
            code = generateShortCode();
        } while (urlRepository.existsByShortCode(code));

        Url url = new Url();
        url.setOriginalUrl(req.getUrl());
        url.setShortCode(code);
        url.setCreatedAt(LocalDateTime.now());
        url.setClickCount(0L);

        if (req.getExpiryDays() > 0) {
            url.setExpiresAt(LocalDateTime.now().plusDays(req.getExpiryDays()));
        }

        return urlRepository.save(url);
    }

    public String getAndTrack(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode)
            .orElseThrow(() -> new RuntimeException("Short URL not found"));

        if (url.getExpiresAt() != null &&
                url.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("This link has expired");
        }

        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);
        return url.getOriginalUrl();
    }

    public Url getStats(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
            .orElseThrow(() -> new RuntimeException("Short URL not found"));
    }
}