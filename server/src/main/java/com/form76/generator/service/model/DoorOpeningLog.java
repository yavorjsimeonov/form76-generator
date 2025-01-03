package com.form76.generator.service.model;
import lombok.ToString;

import java.util.List;

@ToString
public class DoorOpeningLog {

  public int totalCount;
  public int totalPage;
  public List<DoorEvent> list;

  public DoorOpeningLog(int totalCount, int totalPage, List<DoorEvent> list) {
    this.totalCount = totalCount;
    this.totalPage = totalPage;
    this.list = list;
  }

  public int getTotalCount() {
    return totalCount;
  }

  public int getTotalPage() {
    return totalPage;
  }

  public List<DoorEvent> getList() {
    return list;
  }
}
