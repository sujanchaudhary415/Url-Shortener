package com.sujan.url_shortener.service.Impl;

import com.sujan.url_shortener.dto.response.ClickAnalyticsResponse;
import com.sujan.url_shortener.entity.Click;
import com.sujan.url_shortener.exception.NotFoundException;
import com.sujan.url_shortener.repository.ClickRepository;
import com.sujan.url_shortener.repository.ShortUrlRepository;
import com.sujan.url_shortener.service.ClickAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ClickAnalyticsResponse getAnalytics(String shortCode, int page,int size
    ) {
        if (!shortUrlRepository.existsByShortCode(shortCode)) {
            throw new NotFoundException("Short URL not found");
        }
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Order.desc("clickedAt"),
                        Sort.Order.desc("id")
                )
        );

        Page<Click> clickPage = clickRepository.findByShortCodeOrderByClickedAtDesc(
                        shortCode,
                        pageable);

        List<Instant> timestamps = clickPage.getContent()
                .stream()
                .map(Click::getClickedAt)
                .toList();

        return new ClickAnalyticsResponse(
                shortCode,
                clickPage.getTotalElements(),
                clickPage.getTotalPages(),
                clickPage.getNumber(),
                clickPage.getSize(),
                timestamps
        );
    }
}
