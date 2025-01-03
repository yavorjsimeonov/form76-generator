package com.form76.generator.service.model;

import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
public class Employee {
  public Integer id;
  public String uuid;
  public String names;
  public List<DoorEvent> doorEvents = new ArrayList<>();
  public Map<String, Long> workedHoursPerDate = new HashMap<>();

  public Employee() {
  }

  public Employee(Integer id, String names) {
    this.id = id;
    this.names = names;
  }
}
