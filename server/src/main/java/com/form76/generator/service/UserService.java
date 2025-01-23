package com.form76.generator.service;

import com.form76.generator.db.entity.User;
import com.form76.generator.db.repository.AdministrationRepository;
import com.form76.generator.db.repository.UserRepository;
import com.form76.generator.service.model.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  AdministrationRepository administrationRepository;

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

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public User createUser(UserRequest userRequest) {
    User user = new User();
    user.firstName = userRequest.getFirstName();
    user.lastName = userRequest.getLastName();
    user.email = userRequest.getEmail();
    user.username = userRequest.getUsername();
    user.password = passwordEncoder.encode(userRequest.getPassword());
    user.role = userRequest.getRole();
    user.active = userRequest.isActive();

    if (userRequest.getAdministrationId() != null) {
      user.administration = administrationRepository.findById(userRequest.getAdministrationId())
          .orElseThrow(() -> new IllegalArgumentException("Administration not found"));
    }

    return userRepository.save(user);
  }

}
