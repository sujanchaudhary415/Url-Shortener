package com.sujan.url_shortener.service.impl;

import com.sujan.url_shortener.entity.ShortUrl;
import com.sujan.url_shortener.repository.ShortUrlRepository;
import com.sujan.url_shortener.service.Impl.ShortUrlServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShortUrlServiceImplTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @InjectMocks
    private ShortUrlServiceImpl shortUrlService;

    @Test
    void shortenUrl_shouldReturnExistingUrl_whenOriginalUrlAlreadyExists(){
        //Arrange
        String originalUrl="https://google.com";
        ShortUrl existingUrl = new ShortUrl();

        existingUrl.setId(1L);
        existingUrl.setShortCode("abc1234");
        existingUrl.setOriginalUrl(originalUrl);
        existingUrl.setCreatedAt(Instant.now());
        existingUrl.setActive(true);

        when(shortUrlRepository.findByOriginalUrl(originalUrl)).thenReturn(Optional.of(existingUrl));

        //Act
        var result=shortUrlService.shortenUrl(originalUrl);

        //Assert
        assertNotNull(result);
        assertSame(existingUrl,result);
        assertEquals("abc1234",result.getShortCode());

        //verify
        verify(shortUrlRepository).findByOriginalUrl(originalUrl);
        verify(shortUrlRepository,never()).save(any(ShortUrl.class));
    }

    @Test
    void shortenUrl_shouldCreateNewUrl_whenOriginalUrlDoesNotExist() {

        // Arrange
        String originalUrl = "https://youtube.com";

        when(shortUrlRepository.findByOriginalUrl(originalUrl))
                .thenReturn(Optional.empty());

        when(shortUrlRepository.findByShortCode(anyString()))
                .thenReturn(Optional.empty());

        ShortUrl savedUrl = new ShortUrl();
        savedUrl.setId(1L);
        savedUrl.setOriginalUrl(originalUrl);
        savedUrl.setShortCode("abc1234");
        savedUrl.setActive(true);

        when(shortUrlRepository.save(any(ShortUrl.class)))
                .thenReturn(savedUrl);


        // Act
        ShortUrl result = shortUrlService.shortenUrl(originalUrl);


        // Assert
        assertNotNull(result);
        assertEquals(originalUrl, result.getOriginalUrl());
        assertEquals("abc1234", result.getShortCode());
        assertTrue(result.isActive());


        // Verify
        verify(shortUrlRepository).findByOriginalUrl(originalUrl);
        verify(shortUrlRepository).findByShortCode(anyString());
        verify(shortUrlRepository).save(any(ShortUrl.class));
    }

    @Test
    void shortenUrl_shouldGenerateAnotherCode_whenGeneratedCodeAlreadyExists() {

        // Arrange
        String originalUrl = "https://youtube.com";

        when(shortUrlRepository.findByOriginalUrl(originalUrl))
                .thenReturn(Optional.empty());

        when(shortUrlRepository.findByShortCode(anyString()))
                .thenReturn(
                        Optional.of(new ShortUrl()), // First code already exists
                        Optional.empty()             // Second code is available
                );

        ShortUrl savedUrl = new ShortUrl();
        savedUrl.setId(1L);
        savedUrl.setOriginalUrl(originalUrl);
        savedUrl.setShortCode("newCode2");
        savedUrl.setActive(true);

        when(shortUrlRepository.save(any(ShortUrl.class)))
                .thenReturn(savedUrl);

        // Act
        ShortUrl result = shortUrlService.shortenUrl(originalUrl);

        // Assert
        assertNotNull(result);
        assertEquals(originalUrl, result.getOriginalUrl());
        assertEquals("newCode2", result.getShortCode());

        // Verify
        verify(shortUrlRepository).findByOriginalUrl(originalUrl);
        verify(shortUrlRepository, times(2)).findByShortCode(anyString());
        verify(shortUrlRepository).save(any(ShortUrl.class));
    }

    @Test
    void getByShortCode_shouldReturnUrl_whenUrlIsActiveAndNotExpired() {

        // Arrange
        String shortCode = "abc1234";

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setId(1L);
        shortUrl.setShortCode(shortCode);
        shortUrl.setOriginalUrl("https://google.com");
        shortUrl.setActive(true);
        shortUrl.setExpiresAt(Instant.now().plusSeconds(3600));

        when(shortUrlRepository.findByShortCode(shortCode))
                .thenReturn(Optional.of(shortUrl));


        // Act
        Optional<ShortUrl> result = shortUrlService.getByShortCode(shortCode);


        // Assert
        assertTrue(result.isPresent());
        assertSame(shortUrl, result.get());
        assertEquals(shortCode, result.get().getShortCode());


        // Verify
        verify(shortUrlRepository).findByShortCode(shortCode);
    }
}
