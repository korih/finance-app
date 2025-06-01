package io.github.korih.finance_processor.models.auth;

import lombok.Data;

@Data
public class RegisterClassDto {
  private String email;
  private String password;
}
