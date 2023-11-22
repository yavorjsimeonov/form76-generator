package com.form76.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class Form76GeneratorApplication {

  public static void main(String[] args) {
    SpringApplication.run(Form76GeneratorApplication.class, args);
  }

  @GetMapping("/ping")
  @ResponseBody
  public String ping(@RequestParam(value = "myName", defaultValue = "World") String name) {
    return String.format("Hello %s!", name);
  }

  @PostMapping("/report/generate")
  @ResponseBody
  public String generateReport(@RequestParam(value = "myName", defaultValue = "World") String name) {
    return String.format("Hello %s!", name);
  }
}
