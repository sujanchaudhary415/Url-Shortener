package com.sujan.url_shortener.controller;

import com.sujan.url_shortener.dto.request.CreateShortUrlRequest;
import com.sujan.url_shortener.dto.response.CreateShortUrlResponse;
import com.sujan.url_shortener.entity.ShortUrl;
import com.sujan.url_shortener.exception.NotFoundException;
import com.sujan.url_shortener.service.ShortUrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/urls")
public class ShortUrlController {
    private final ShortUrlService shortUrlService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateShortUrlResponse shortenUrl( @Valid @RequestBody CreateShortUrlRequest request){
        ShortUrl shortUrl=shortUrlService.shortenUrl(request.originalUrl());
        return new CreateShortUrlResponse(
                shortUrl.getShortCode(),
                shortUrl.getOriginalUrl()
        );
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        ShortUrl shortUrl=shortUrlService.getByShortCode(shortCode).orElseThrow(()->new NotFoundException("Short URL not found"));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(shortUrl.getOriginalUrl()));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
