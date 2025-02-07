package com.form76.generator.service.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
public class ReportRequest {

  private String startDateTime;
  private String endDateTime;
  private String reportAlgorithm;
  private String reportFileFormat;

  public LocalDateTime getStartDateTimeMillis() {
    return LocalDateTime.parse(startDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));
  }

  public LocalDateTime getEndDateTimeMillis() {
    return LocalDateTime.parse(endDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));
  }

  public LocalDateTime getStartDateTime() {
    return LocalDateTime.parse(startDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  public LocalDateTime getEndDateTime() {
    return LocalDateTime.parse(endDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

}

