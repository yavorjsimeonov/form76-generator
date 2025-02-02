package com.form76.generator.rest;

import com.form76.generator.rest.model.PasswordChangeRequest;
import com.form76.generator.rest.model.UserData;
import com.form76.generator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/users")
public class UserResource {

  @Autowired
  UserService userService;

  @GetMapping
  public ResponseEntity<List<UserData>> getAllUsers() {
    List<UserData> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @PostMapping
  public ResponseEntity<UserData> createUser(@RequestBody UserData userData) {
    try {
      UserData createdUser = userService.createUser(userData);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }

  @GetMapping("/{id}")
  public UserData getUserById(@PathVariable String id) {
    return userService.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
  }

  @PostMapping("/{userId}/change-password")
  public ResponseEntity<?> changePassword(@PathVariable String userId, @RequestBody PasswordChangeRequest request) {
    userService.changeUserPassword(userId, request);
    return ResponseEntity.ok().build();
  }

}
