package com.sujan.url_shortener.controller;

import com.sujan.url_shortener.dto.request.CreateShortUrlRequest;
import com.sujan.url_shortener.dto.response.CreateShortUrlResponse;
import com.sujan.url_shortener.entity.ShortUrl;
import com.sujan.url_shortener.exception.NotFoundException;
import com.sujan.url_shortener.messaging.ClickEventProducer;
import com.sujan.url_shortener.service.ShortUrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="URL Shortener",description = "APIs for creating and redirecting short URLs")
public class ShortUrlController {
    private final ShortUrlService shortUrlService;
    private final ClickEventProducer clickEventProducer;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a short URL",
            description = "Converts a long URL into a unique short URL"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Short URl created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid URL request")
    })
    public CreateShortUrlResponse shortenUrl( @Valid @RequestBody CreateShortUrlRequest request){
        ShortUrl shortUrl=shortUrlService.shortenUrl(request.originalUrl());
        return new CreateShortUrlResponse(
                shortUrl.getShortCode(),
                shortUrl.getOriginalUrl()
        );
    }

    @GetMapping("/{shortCode}")
    @Operation(
            summary = "Redirect using short code",
            description = "Redirects the user to the original URL and publishes a click event"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redirect successful"),
            @ApiResponse(responseCode = "404", description = "Short URL not found")
    })
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        ShortUrl shortUrl=shortUrlService.getByShortCode(shortCode).orElseThrow(()->new NotFoundException("Short URL not found"));

        clickEventProducer.publishClick(shortCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(shortUrl.getOriginalUrl()));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
