package com.form76.generator.service.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@Getter
@Setter
public class Employee {
  private Integer id;
  private String uuid;
  private String names;
  private List<DoorEvent> doorEvents = new ArrayList<>();
  private Map<String, Long> workedHoursPerDate = new HashMap<>();

  public Employee() {
  }

  public Employee(Integer id, String names) {
    this.id = id;
    this.names = names;
  }
}
