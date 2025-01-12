package com.form76.generator.service.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportRequest {

  public String startDateTime;
  public String endDateTime;

  public LocalDateTime getStartDateTime() {
    return LocalDateTime.parse(startDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  public LocalDateTime getEndDateTime() {
    return LocalDateTime.parse(endDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

}

