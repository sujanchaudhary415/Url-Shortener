package com.sujan.url_shortener;

import com.sujan.url_shortener.entity.Click;
import com.sujan.url_shortener.entity.ShortUrl;
import com.sujan.url_shortener.repository.ClickRepository;
import com.sujan.url_shortener.repository.ShortUrlRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ShortUrlIntegrationTest {

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Autowired
    private ClickRepository clickRepository;

    @Test
    void contextLoads() {
        // Verifies that the Spring application context starts successfully
    }

    @Test
    void shouldSaveAndRetrieveShortUrl() {

        // Arrange
        String originalUrl = "https://example.com/" + UUID.randomUUID();
        String shortCode = "test" + UUID.randomUUID()
                .toString().substring(0, 8);

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(originalUrl);
        shortUrl.setShortCode(shortCode);

        // Act
        ShortUrl savedUrl = shortUrlRepository.save(shortUrl);

        Optional<ShortUrl> result =
                shortUrlRepository.findByShortCode(shortCode);

        // Assert
        assertNotNull(savedUrl.getId());
        assertTrue(result.isPresent());
        assertEquals(originalUrl, result.get().getOriginalUrl());
        assertEquals(shortCode, result.get().getShortCode());
    }

    @Test
    void shouldSaveAndRetrieveClick() {

        // Arrange
        String shortCode = "test" + UUID.randomUUID()
                .toString().substring(0, 8);

        Instant clickedAt = Instant.parse("2026-11-16T10:00:00Z");

        Click click = new Click(null, shortCode, clickedAt);

        // Act
        Click savedClick = clickRepository.save(click);

        Page<Click> result =
                clickRepository.findByShortCodeOrderByClickedAtDesc(
                        shortCode,
                        PageRequest.of(0, 10)
                );

        // Assert
        assertNotNull(savedClick.getId());

        assertEquals(1, result.getTotalElements());

        assertEquals(shortCode, result.getContent().get(0).getShortCode());

        assertEquals(clickedAt, result.getContent().get(0).getClickedAt());
    }

}
