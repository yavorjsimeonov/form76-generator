package com.form76.generator.rest;

import com.form76.generator.db.entity.User;
import com.form76.generator.rest.model.AuthResponse;
import com.form76.generator.rest.model.LoginRequest;
import com.form76.generator.rest.model.UserData;
import com.form76.generator.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/auth")
public class AuthenticationResource {

  Logger logger = LoggerFactory.getLogger(AuthenticationResource.class);

  @Autowired
  UserService userService;

  @PostMapping("/authenticate")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
    logger.info("Received authenticate request");

    Optional<UserData> userOptional = userService.validateCredentials(loginRequest.getUsername(), loginRequest.getPassword());
    if (userOptional.isPresent()) {
      UserData userData = userOptional.get();
      logger.info("Found user for authenticate request");

      return ResponseEntity.ok(new AuthResponse(userData.getId(), userData.getUsername(), userData.getEmail(), userData.getRole()));
    }
    logger.info("User not authenticated");

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
