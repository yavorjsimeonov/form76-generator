package com.form76.generator.service;

import com.form76.generator.db.entity.User;
import com.form76.generator.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  public Optional<User> getUserByUsername(String username) {
    User user = userRepository.findByUsername(username).orElse(null);
    if (user == null || !user.active) {
      return Optional.ofNullable(null);
    }

    return Optional.of(user);
  }

  public Optional<User> validateCredentials(String username, String password) {
    return getUserByUsername(username)
        .filter(user -> passwordEncoder.matches(password, user.password));
  }

  public void createUser(User user) {
    user.password = passwordEncoder.encode(user.password);
    userRepository.save(user);
  }
}
