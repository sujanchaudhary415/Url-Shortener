package com.sujan.url_shortener.service.Impl;

import com.sujan.url_shortener.dto.response.ClickAnalyticsResponse;
import com.sujan.url_shortener.entity.Click;
import com.sujan.url_shortener.exception.NotFoundException;
import com.sujan.url_shortener.repository.ClickRepository;
import com.sujan.url_shortener.repository.ShortUrlRepository;
import com.sujan.url_shortener.service.ClickAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClickAnalyticsServiceImpl implements ClickAnalyticsService {

    private final ClickRepository clickRepository;
    private final ShortUrlRepository shortUrlRepository;
    @Override
    @Transactional(readOnly = true)
    public ClickAnalyticsResponse getAnalytics(String shortCode) {
        if (!shortUrlRepository.existsByShortCode(shortCode)) {
            throw new NotFoundException("Short URL not found");
        }
         long totalClicks=clickRepository.countByShortCode(shortCode);
         List<Click> clicks= clickRepository.findByShortCodeOrderByClickedAtDesc(shortCode);
         List<Instant>timestamps=clicks.stream()
                 .map(Click::getClickedAt)
                 .toList();

         return new ClickAnalyticsResponse(
                 shortCode,
                 totalClicks,
                 timestamps
         );
    }
}
