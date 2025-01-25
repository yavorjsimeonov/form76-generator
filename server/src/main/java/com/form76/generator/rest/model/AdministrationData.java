package com.form76.generator.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class AdministrationData {

  private String id;

  private String name;

  private boolean active;

  private Set<LocationData> locations;

}
