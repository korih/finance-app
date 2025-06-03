package io.github.korih.finance_processor.models.repositories;

import io.github.korih.finance_processor.models.Session;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SessionRepository extends CrudRepository <Session, UUID> {
  Session findBySessionId(UUID id);
  Session findByRefreshToken(UUID id);
  Session findByUserId(UUID userId);
  boolean deleteBySessionId(UUID id);
} 