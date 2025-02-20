package com.form76.generator.service;

import com.form76.generator.db.entity.Administration;
import com.form76.generator.db.entity.User;
import com.form76.generator.db.repository.UserRepository;
import com.form76.generator.rest.model.AdministrationData;
import com.form76.generator.rest.model.PasswordChangeRequest;
import com.form76.generator.rest.model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  AdministrationService administrationService;

  @Autowired
  PasswordEncoder passwordEncoder;

  public Optional<UserData> getUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .filter(User::isActive)
        .map(this::convertToUserData);
  }

  public Optional<UserData> validateCredentials(String username, String password) {
    return getUserByUsername(username)
        .filter(user -> passwordEncoder.matches(password, user.getPassword()));
  }

  public List<UserData> getAllUsers() {
    return userRepository.findAll().stream()
        .map(this::convertToUserData)
        .collect(Collectors.toList());
  }

  public UserData createUser(UserData userData) {
    User user = new User();
    user.setFirstName(userData.getFirstName());
    user.setLastName(userData.getLastName());
    user.setEmail(userData.getEmail());
    user.setUsername(userData.getUsername());
    user.setPassword(passwordEncoder.encode(userData.getPassword()));
    user.setRole(userData.getRole());
    user.setActive(userData.isActive());

    if (userData.getAdministrationId() != null) {
      user.setAdministration(convertToAdministration(administrationService.findById(userData.getAdministrationId())));
    }

    User savedUser = userRepository.save(user);
    return convertToUserData(savedUser);
  }

  public Optional<UserData> findById(String id) {
    return userRepository.findById(id)
        .map(this::convertToUserData);
  }

  public UserData updateUser(String userId, UserData request) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setEmail(request.getEmail());
    user.setRole(request.getRole());
    user.setActive(request.isActive());

    if (request.getPassword() != null && !request.getPassword().isEmpty()) {
      user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    return convertToUserData(userRepository.save(user));
  }

  private User convertToUser(UserData userData) {
    User user = new User();
    user.setId(userData.getId());
    user.setFirstName(userData.getFirstName());
    user.setLastName(userData.getLastName());
    user.setEmail(userData.getEmail());
    user.setUsername(userData.getUsername());
    user.setPassword(userData.getPassword());
    user.setRole(userData.getRole());
    user.setActive(userData.isActive());

    if (userData.getAdministrationId() != null) {
      Administration administration = convertToAdministration(administrationService.findById(userData.getAdministrationId()));
      user.setAdministration(administration);
    }

    return user;
  }

  private UserData convertToUserData(User user) {
    return new UserData(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getEmail(),
        user.getUsername(),
        user.getPassword(),
        user.getRole(),
        user.getAdministration() != null ? user.getAdministration().getId() : null,
        user.isActive()
    );
  }

  private Administration convertToAdministration(AdministrationData administrationData) {
    Administration administration = new Administration();
    administration.setId(administrationData.getId());
    administration.setName(administrationData.getName());
    administration.setActive(administrationData.isActive());

    return administration;
  }

  private AdministrationData convertToAdministrationData(Administration administration) {
    return new AdministrationData(
        administration.getId(),
        administration.getName(),
        administration.isActive(),
        null // Locations can be added here if necessary
    );
  }

  public void changeUserPassword(String userId, PasswordChangeRequest request) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new IllegalArgumentException("Current password is incorrect");
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
  }

}
