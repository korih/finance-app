package io.github.korih.finance_processor.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.korih.finance_processor.models.User;
import io.github.korih.finance_processor.models.UserRepository;
import io.github.korih.finance_processor.models.dtos.LoginDto;
import io.github.korih.finance_processor.models.dtos.RegisterClassDto;

@Service
public class AuthenticationService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  public AuthenticationService(UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
  }

  public User signUp(RegisterClassDto input) {
    User user = new User();
    user.setEmail(input.getEmail());
    user.setPassword(passwordEncoder.encode(input.getPassword()));

    return userRepository.save(user);
  }

  public User authenticate(LoginDto input) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
    );
    return userRepository.findByEmail(input.getEmail()).orElseThrow();
  }
}
