package com.form76.generator.service.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class DoorOpeningLogResponse {
  private Integer code;
  private String msg;
  private String time;

  private DoorOpeningLog data;

  public DoorOpeningLogResponse(){
  }

  public DoorOpeningLogResponse(Integer code, String msg, String time, DoorOpeningLog data) {
    this.code = code;
    this.msg = msg;
    this.time = time;
    this.data = data;
  }
}
