package com.form76.generator.rest;

import com.form76.generator.service.Form76ReportService;
import com.form76.generator.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.FileSystems;


@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class Form76GeneratorResource {

  Logger logger = LoggerFactory.getLogger(Form76GeneratorResource.class);

  @Autowired
  Form76ReportService form76ReportService;

  @Autowired
  UserService userService;

  @GetMapping("/ping")
  @ResponseBody
  public String ping() {
    return "Ping successful...";
  }
//
//  @PostMapping(path = "/login")
//  public UserInfo login(@RequestBody UserCredentials credentials) throws LoginException {
//    logger.info("Received request for login");
//    return userService.loginUser(credentials.username, credentials.password);
//  }


  @GetMapping(path = "/generate")
  public String generatorFor(Model model)  {
    return "generator";
  }



}
