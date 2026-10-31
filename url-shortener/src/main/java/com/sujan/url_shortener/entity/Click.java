package com.sujan.url_shortener.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "clicks")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Click {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_code",nullable = false,length = 16)
    private String shortCode;

    @Column(name = "clicked_at",nullable = false)
    private Instant clickedAt;


}
