package com.form76.generator.service.model;

import lombok.ToString;

@ToString
public class DoorOpeningLogResponse {
  public Integer code;
  public String msg;
  public String time;

  public DoorOpeningLog data;

  public DoorOpeningLogResponse(Integer code, String msg, String time, DoorOpeningLog data) {
    this.code = code;
    this.msg = msg;
    this.time = time;
    this.data = data;
  }

  public Integer getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }

  public String getTime() {
    return time;
  }

  public DoorOpeningLog getData() {
    return data;
  }
}
