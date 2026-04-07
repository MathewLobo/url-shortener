package com.mathew.urlshortener.scheduler;

import com.mathew.urlshortener.repository.UrlRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import com.mathew.urlshortener.model.Url;

@Component
public class UrlCleanupScheduler {

    private final UrlRepository urlRepository;

    public UrlCleanupScheduler(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Scheduled(cron = "0 0 0 * * *") // runs every day at midnight
    public void deleteExpiredUrls() {
        List<Url> expired = urlRepository
            .findByExpiresAtBefore(LocalDateTime.now());
        urlRepository.deleteAll(expired);
        System.out.println("Cleaned up " + expired.size() + " expired URLs");
    }
}