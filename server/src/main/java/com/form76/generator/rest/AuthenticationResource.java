package com.form76.generator.rest;

import com.form76.generator.db.entity.User;
import com.form76.generator.rest.model.AuthResponse;
import com.form76.generator.rest.model.LoginRequest;
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
//@CrossOrigin(origins = Application.UI_HOST)
@RequestMapping("/auth")
public class AuthenticationResource {

  Logger logger = LoggerFactory.getLogger(AuthenticationResource.class);

  @Autowired
  UserService userService;

  @GetMapping("/ping")
  @ResponseBody
  public String ping() {
    return "Ping successful...";
  }


  @PostMapping("/authenticate")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
    logger.info("Received authenticate request");

    Optional<User> userOptional = userService.loginUser(loginRequest.username, loginRequest.password);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      logger.info("Found user authenticate request");

      return ResponseEntity.ok(new AuthResponse(user.id, user.email, user.role));
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
