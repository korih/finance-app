package io.github.korih.finance_processor.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.korih.finance_processor.models.User;
import io.github.korih.finance_processor.models.repositories.UserRepository;

@Service
public class UserService {

  @Autowired
  UserRepository userRepository;

  public Optional<User> findById(UUID userUuid) {
    return userRepository.findById(userUuid);
  }

}