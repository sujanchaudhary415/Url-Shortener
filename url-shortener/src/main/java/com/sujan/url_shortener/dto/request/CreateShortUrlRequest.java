package com.sujan.url_shortener.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateShortUrlRequest(
  @NotBlank
  @Size(max=2048,message = "URL must not exceed 2048 characters")
  @Pattern(
          regexp = "^https?://.+$",
          message = "URL must start with http:// or https://"
  )
  String originalUrl
){}
