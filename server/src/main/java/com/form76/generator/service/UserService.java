package com.form76.generator.service;

import com.form76.generator.db.entity.User;
import com.form76.generator.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

  @Autowired
  UserRepository userRepository;

  public Optional<User> loginUser(String username, String password) {
    User user = userRepository.findByUsernameAndPassword(username, password);
    if (user == null || !user.active) {
      return Optional.ofNullable(null);
    }

    return Optional.of(user);
  }
}
