package io.github.korih.finance_processor.models.auth;

import lombok.Data;

@Data
public class LoginResponse {
  private String token;
  private long expiration; 
}
