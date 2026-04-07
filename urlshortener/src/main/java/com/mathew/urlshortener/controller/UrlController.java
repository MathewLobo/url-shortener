package com.mathew.urlshortener.controller;

import com.mathew.urlshortener.dto.ShortenRequest;
import com.mathew.urlshortener.dto.UrlResponse;
import com.mathew.urlshortener.model.Url;
import com.mathew.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class UrlController {

    private final UrlService urlService;

    @Value("${APP_BASE_URL:http://localhost:8080}")
    private String baseUrl;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<UrlResponse> shorten(
            @RequestBody @Valid ShortenRequest req) {
        Url url = urlService.shortenUrl(req);
        return ResponseEntity.ok(new UrlResponse(url, baseUrl));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            @PathVariable String shortCode) {
        String original = urlService.getAndTrack(shortCode);
        return ResponseEntity.status(302)
                .header("Location", original).build();
    }

    @GetMapping("/api/stats/{shortCode}")
    public ResponseEntity<UrlResponse> stats(
            @PathVariable String shortCode) {
        Url url = urlService.getStats(shortCode);
        return ResponseEntity.ok(new UrlResponse(url, baseUrl));
    }

    @DeleteMapping("/api/urls/{shortCode}")
    public ResponseEntity<String> delete(
            @PathVariable String shortCode) {
        urlService.deleteUrl(shortCode);
        return ResponseEntity.ok("Link deleted successfully");
    }
}