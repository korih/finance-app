package com.korih.finance_app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "reddit.api")
public class RedditConfig {
  private String clientId;
  private String clientSecret;
  private String userAgent;
  private String redirectUri;
}
