package com.sujan.url_shortener.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "short_urls")
@Getter
@Setter
@NoArgsConstructor
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_code",nullable = false,unique = true,length = 16)
    private String shortCode;

    @Column(name = "original_url",nullable = false,unique = true,columnDefinition = "TEXT")
    private String originalUrl;

    @Column(name = "created_at",nullable = false,updatable = false)
    private Instant createdAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "is_active",nullable = false)
    private boolean active=true;

    @PrePersist
    protected void onCreate(){
        if(createdAt==null){
            createdAt=Instant.now();
        }
    }

}
