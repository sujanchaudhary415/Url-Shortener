package com.sujan.url_shortener.service.impl;

import com.sujan.url_shortener.entity.Click;
import com.sujan.url_shortener.exception.NotFoundException;
import com.sujan.url_shortener.repository.ClickRepository;
import com.sujan.url_shortener.repository.ShortUrlRepository;
import com.sujan.url_shortener.service.Impl.ClickAnalyticsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClickAnalyticsServiceImplTest {
    @Mock
    private ShortUrlRepository shortUrlRepository;

    @Mock
    private ClickRepository clickRepository;

    @InjectMocks
    private ClickAnalyticsServiceImpl clickAnalyticsService;

    @Test
    void getAnalytics_shouldReturnAnalytics_whenShortCodeExists(){

        //Arrange
        String shortCode="wGYpJbh";
        when(shortUrlRepository.existsByShortCode(shortCode)).thenReturn(true);

        Page<Click>clickPage=new PageImpl<>(List.of(), PageRequest.of(0,10),0);
        when(clickRepository.findByShortCodeOrderByClickedAtDesc(eq(shortCode),any(Pageable.class))).thenReturn(clickPage);

        //Act
        var response=clickAnalyticsService.getAnalytics(shortCode,0,10);

        //Assert
        assertNotNull(response);
        assertEquals(shortCode,response.shortCode());
        assertEquals(0,response.totalClicks());
        assertEquals(0,response.totalPages());
        assertEquals(0,response.currentPage());
        assertEquals(10, response.pageSize());
        assertTrue(response.clicks().isEmpty());

        verify(shortUrlRepository).existsByShortCode(shortCode);
        verify(clickRepository).findByShortCodeOrderByClickedAtDesc(eq(shortCode),any(Pageable.class));
    }

    @Test
    void getAnalytics_shouldThrowNotFoundExceptionWhenShortCodeDoesNotExist(){
        //Arrange
        String shortCode="Invalid";
        when(shortUrlRepository.existsByShortCode(shortCode)).thenReturn(false);

        //Act+Assert
        assertThrows(NotFoundException.class,()->clickAnalyticsService.getAnalytics(shortCode,0,10));

        verify(shortUrlRepository).existsByShortCode(shortCode);
        verifyNoInteractions(clickRepository);
    }

    @Test
    void getAnalytics_shouldReturnClickRecords_whenClicksExist(){
        //Arrange
        String shortCode="wGYpJbh";
        when(shortUrlRepository.existsByShortCode(shortCode))
                .thenReturn(true);

        Click click1=new Click(
                1L,
                shortCode,
                Instant.parse("2026-11-01T10:00:00Z")
        );

        Click click2=new Click(
                2L,
                shortCode,
                Instant.parse("2026-11-02T10:00:00Z")
        );

        Page<Click>clickPage=new PageImpl<>(List.of(click1,click2),PageRequest.of(0,10),2);
        when(clickRepository.findByShortCodeOrderByClickedAtDesc(eq(shortCode),any(Pageable.class))).thenReturn(clickPage);

        //Act
        var response=clickAnalyticsService.getAnalytics(shortCode,0,10);

        //Assert
        assertNotNull(response);
        assertEquals(2, response.totalClicks());
        assertEquals(1, response.totalPages());
        assertEquals(0, response.currentPage());
        assertEquals(10, response.pageSize());
        assertEquals(2, response.clicks().size());

        assertEquals(
                Instant.parse("2026-11-02T10:00:00Z"),
                response.clicks().get(0)
        );

        assertEquals(
                Instant.parse("2026-11-01T10:00:00Z"),
                response.clicks().get(1)
        );

        //Verify
        verify(shortUrlRepository).existsByShortCode(shortCode);
        verify(clickRepository).findByShortCodeOrderByClickedAtDesc(eq(shortCode),any(Pageable.class));
    }
}