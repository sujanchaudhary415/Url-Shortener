package com.sujan.url_shortener.service.Impl;

import com.sujan.url_shortener.entity.ShortUrl;
import com.sujan.url_shortener.repository.ShortUrlRepository;
import com.sujan.url_shortener.service.ShortUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShortUrlServiceImpl implements ShortUrlService {
    private final ShortUrlRepository shortUrlRepository;
    private static final String CHARACTERS="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int code_length=7;
    private static final SecureRandom random=new SecureRandom();

    @Override
    @Transactional
    public ShortUrl shortenUrl(String originalUrl) {
        Optional<ShortUrl> existingUrl =
                shortUrlRepository.findByOriginalUrl(originalUrl);

        if (existingUrl.isPresent()) {
            return existingUrl.get();
        }

        String shortCode=generateUniqueCode();
        ShortUrl shortUrl=new ShortUrl();
        shortUrl.setOriginalUrl(originalUrl);
        shortUrl.setShortCode(shortCode);
        return shortUrlRepository.save(shortUrl);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShortUrl> getByShortCode(String shortCode) {
        return shortUrlRepository.findByShortCode(shortCode)
                .filter(ShortUrl::isActive)
                .filter(shortUrl ->
                        shortUrl.getExpiresAt()==null ||
                        shortUrl.getExpiresAt().isAfter(Instant.now()));
    }

    private String generateUniqueCode(){
        String shortCode;
        do {
            shortCode = generateRandomCode();
        } while(shortUrlRepository.findByShortCode(shortCode).isPresent());
        return shortCode;
    }

    private String generateRandomCode(){
      StringBuilder code=new StringBuilder();
      for(int i=0;i<code_length;i++){
          int index= random.nextInt(CHARACTERS.length());
          code.append(CHARACTERS.charAt(index));
      }
      return code.toString();
    }
}
