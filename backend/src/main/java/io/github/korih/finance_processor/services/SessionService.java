package io.github.korih.finance_processor.services;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.korih.finance_processor.models.Session;
import io.github.korih.finance_processor.models.User;
import io.github.korih.finance_processor.models.repositories.SessionRepository;

@Service
public class SessionService {

  @Autowired
  private SessionRepository sessionRepository;

  public Session getSessionById(String id) {
    return sessionRepository.findBySessionId(UUID.fromString(id));
  }

  public Session getSessionByRefreshToken(String id) {
    return sessionRepository.findByRefreshToken(UUID.fromString(id));
  }

  public boolean createSession(UUID id, User user) {
    var session = sessionRepository.findByUserId(user.getId());
    if (!Objects.isNull(session)) {
      sessionRepository.delete(session);
    }

    try {
      Session createdSession = new Session(); 
      Date now = new Date();

      createdSession.setSessionId(id);
      createdSession.setUserId(user.getId());
      createdSession.setCreation(now);
      createdSession.setExpiration(new Date(now.getTime() + 1000 * 60 * 30));

      sessionRepository.save(createdSession);
      return true;
    } catch (Exception e) {
      e.printStackTrace();

      return false;
    }
  }
}
