package com.form76.generator.service.model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class DoorOpeningLog {

  private int totalCount;
  private int totalPage;
  private List<DoorEvent> list;

  public DoorOpeningLog(int totalCount, int totalPage, List<DoorEvent> list) {
    this.totalCount = totalCount;
    this.totalPage = totalPage;
    this.list = list;
  }

}
