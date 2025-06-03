package io.github.korih.finance_processor.models;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "session")
@NoArgsConstructor
public class Session {

  @Id
  private UUID sessionId;
  private UUID refreshToken;
  private UUID userId;
  private Date expiration;
  private Date creation;

}
