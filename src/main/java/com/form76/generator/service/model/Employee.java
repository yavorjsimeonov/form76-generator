package com.form76.generator.service.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Employee {
  public String id;
  public String names;
  public List<DoorEvent> doorEvents = new ArrayList<>();
  public Map<String, Long> workedHoursPerDate = new HashMap<>();


  @Override
  public String toString() {
    return "Employee{" +
        "id='" + id + '\'' +
        ", names='" + names + '\'' +
        ", doorEvents=" + doorEvents +
        ", workedHoursPerDate=" + workedHoursPerDate +
        '}';
  }

  public String getId() {
    return id;
  }

  public String getNames() {
    return names;
  }

  public List<DoorEvent> getDoorEvents() {
    return doorEvents;
  }

  public Map<String, Long> getWorkedHoursPerDate() {
    return workedHoursPerDate;
  }
}
