package com.form76.generator.service.model;

import com.form76.generator.db.entity.Location;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
public class DoorOpeningLogRequest {

  public Location location;

  public LocalDateTime startDateTime;

  public LocalDateTime endDateTime;

  public DoorOpeningLogRequest(Location locaction, LocalDateTime startDateTime, LocalDateTime endDateTime) {
    this.location = locaction;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
  }

  public DoorOpeningLogRequest() {
  }
}
