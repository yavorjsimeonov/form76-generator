package com.form76.generator.service.model;

import lombok.ToString;

import java.time.LocalDateTime;

@ToString
public class DoorOpeningLogRequest {

  public Integer extCommunityId;

  public String extCommunityUuid;

  public LocalDateTime startDateTime;

  public LocalDateTime endDateTime;

  public DoorOpeningLogRequest(Integer extCommunityId, String extCommunityUuid, LocalDateTime startDateTime, LocalDateTime endDateTime) {
    this.extCommunityId = extCommunityId;
    this.extCommunityUuid = extCommunityUuid;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
  }
}
