package com.form76.generator.service.model;

import java.io.Serializable;
import java.util.Date;


public class DoorEvent implements Serializable {
  public Date timestamp;
  public String doorName;
  public String entryPointName;
  public boolean isInEvent;

  public DoorEvent(Date timestamp, String doorName, String entryPointName) {
    this.timestamp = timestamp;
    this.doorName = doorName;
    this.entryPointName = entryPointName;
    this.isInEvent = entryPointName.endsWith("-IN") || entryPointName.endsWith("-in");
  }

  @Override
  public String toString() {
    return "DoorEvents{" +
        "timestamp=" + timestamp +
        ", doorName='" + doorName + '\'' +
        ", inOffice=" + isInEvent +
        '}';
  }
}
