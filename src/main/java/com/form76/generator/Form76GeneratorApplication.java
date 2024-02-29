package com.form76.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.form76.generator", "com.form76.generator.resource"})
public class Form76GeneratorApplication {

  private final static Logger logger = LoggerFactory.getLogger(Form76GeneratorApplication.class.getName());

  public static void main(String[] args) {
    SpringApplication.run(Form76GeneratorApplication.class, args);
  }

}
