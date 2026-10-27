package com.sujan.url_shortener.controller;

import com.sujan.url_shortener.dto.request.CreateShortUrlRequest;
import com.sujan.url_shortener.dto.response.CreateShortUrlResponse;
import com.sujan.url_shortener.entity.ShortUrl;
import com.sujan.url_shortener.service.ShortUrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
