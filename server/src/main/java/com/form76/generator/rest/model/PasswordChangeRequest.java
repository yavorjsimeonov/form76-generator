package com.form76.generator.rest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequest {
  private String currentPassword;
  private String newPassword;
}
