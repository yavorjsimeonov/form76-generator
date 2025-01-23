package com.form76.generator.service.model;

import com.form76.generator.db.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
  private String firstName;
  private String lastName;
  private String email;
  private String username;
  private String password;
  private Role role;
  private String administrationId;
  private boolean active;
}
