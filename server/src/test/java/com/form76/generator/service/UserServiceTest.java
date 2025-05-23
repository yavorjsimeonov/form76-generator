package com.form76.generator.service;

import com.form76.generator.db.entity.Administration;
import com.form76.generator.db.entity.Role;
import com.form76.generator.db.entity.User;
import com.form76.generator.db.repository.UserRepository;
import com.form76.generator.rest.model.AdministrationData;
import com.form76.generator.rest.model.PasswordChangeRequest;
import com.form76.generator.rest.model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

  private UserService userService;
  private UserRepository userRepository;
  private AdministrationService administrationService;
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    userService = new UserService();
    userRepository = mock(UserRepository.class);
    administrationService = mock(AdministrationService.class);
    passwordEncoder = mock(PasswordEncoder.class);

    userService.userRepository = userRepository;
    userService.administrationService = administrationService;
    userService.passwordEncoder = passwordEncoder;
  }

  @Test
  void testGetUserByUsername_Active() {
    User user = createUser();
    user.setActive(true);
    when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

    Optional<UserData> result = userService.getUserByUsername("john");

    assertTrue(result.isPresent());
    assertEquals("john", result.get().getUsername());
  }

  @Test
  void testValidateCredentials_Valid() {
    User user = createUser();
    user.setActive(true);
    when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);

    Optional<UserData> result = userService.validateCredentials("john", "password123");

    assertTrue(result.isPresent());
  }

  @Test
  void testValidateCredentials_InvalidPassword() {
    User user = createUser();
    user.setActive(true);
    when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("wrong", user.getPassword())).thenReturn(false);

    Optional<UserData> result = userService.validateCredentials("john", "wrong");

    assertFalse(result.isPresent());
  }

  @Test
  void testGetAllUsers() {
    when(userRepository.findAll()).thenReturn(List.of(createUser()));

    List<UserData> users = userService.getAllUsers();

    assertEquals(1, users.size());
    assertEquals("john", users.get(0).getUsername());
  }

  @Test
  void testCreateUser_WithAdministration() {
    UserData request = createUserData();
    request.setAdministrationId("admin1");

    AdministrationData adminData = new AdministrationData("admin1", "Admin", true, null);
    when(administrationService.findById("admin1")).thenReturn(adminData);
    when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    when(userRepository.save(captor.capture())).thenAnswer(i -> i.getArgument(0));

    UserData result = userService.createUser(request);

    assertEquals("john", result.getUsername());
    assertEquals("hashedPassword", captor.getValue().getPassword());
    assertEquals("admin1", captor.getValue().getAdministration().getId());
  }

  @Test
  void testUpdateUser_WithPasswordChange() {
    User existing = createUser();
    existing.setPassword("old");

    when(userRepository.findById("user1")).thenReturn(Optional.of(existing));
    when(passwordEncoder.encode("newpass")).thenReturn("encodedNew");
    when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

    UserData update = createUserData();
    update.setPassword("newpass");

    UserData result = userService.updateUser("user1", update);

    assertEquals("encodedNew", result.getPassword());
    verify(userRepository).save(any());
  }

  @Test
  void testChangePassword_Success() {
    User user = createUser();
    user.setPassword("encodedCurrent");

    when(userRepository.findById("user1")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("current", "encodedCurrent")).thenReturn(true);

    PasswordChangeRequest request = new PasswordChangeRequest();
    request.setCurrentPassword("current");
    request.setNewPassword("new");

    when(passwordEncoder.encode("new")).thenReturn("encodedNew");

    userService.changeUserPassword("user1", request);

    assertEquals("encodedNew", user.getPassword());
    verify(userRepository).save(user);
  }

  @Test
  void testChangePassword_WrongCurrent() {
    User user = createUser();
    user.setPassword("encodedCurrent");

    when(userRepository.findById("user1")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("wrong", "encodedCurrent")).thenReturn(false);

    PasswordChangeRequest request = new PasswordChangeRequest();
    request.setCurrentPassword("wrong");
    request.setNewPassword("new");

    assertThrows(IllegalArgumentException.class, () -> userService.changeUserPassword("user1", request));
  }

  @Test
  void testChangePassword_UserNotFound() {
    when(userRepository.findById("not_found")).thenReturn(Optional.empty());

    PasswordChangeRequest request = new PasswordChangeRequest();
    request.setCurrentPassword("current");
    request.setNewPassword("new");

    assertThrows(UsernameNotFoundException.class, () -> userService.changeUserPassword("not_found", request));
  }

  // Utility methods
  private User createUser() {
    User user = new User();
    user.setId("user1");
    user.setUsername("john");
    user.setFirstName("John");
    user.setLastName("Doe");
    user.setEmail("john@example.com");
    user.setPassword("password123");
    user.setRole(Role.USER);
    user.setActive(true);
    return user;
  }

  private UserData createUserData() {
    return new UserData(
        "user1", "John", "Doe", "john@example.com",
        "john", "password123", Role.USER, null, true
    );
  }
}
