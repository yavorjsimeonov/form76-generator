package com.form76.generator.rest.model;

import com.form76.generator.db.entity.Administration;
import com.form76.generator.db.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserData {

  private String id;

  private String firstName;

  private String lastName;

  private String email;

  private String username;

  private String password;

  private Role role;

  private String administrationId;

  private boolean active;
}
