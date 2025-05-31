package io.github.korih.finance_processor.models.dtos;

import lombok.Data;

@Data
public class LoginResponse {
  private String token;
  private long expiration; 
}
