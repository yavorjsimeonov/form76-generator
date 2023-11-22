package com.form76.generator.model;

import java.io.Serializable;
import java.util.Date;


public class DoorEvent implements Serializable {
  public Date timestamp;
  public String doorName;
  public boolean isInEvent;

  public DoorEvent(Date timestamp, String doorName) {
    this.timestamp = timestamp;
    this.doorName = doorName;
    this.isInEvent = doorName.startsWith("IN_");
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
