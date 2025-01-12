package com.form76.generator.db.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReportAlgorithm {

  FIRST_IN_LAST_OUT,
  EVERY_IN_OUT;

  @JsonValue
  public String toValue() {
    return name();
  }
}
