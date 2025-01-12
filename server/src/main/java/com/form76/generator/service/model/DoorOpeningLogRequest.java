package com.form76.generator.service.model;

import com.form76.generator.db.entity.Location;
import com.form76.generator.db.entity.ReportAlgorithm;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
public class DoorOpeningLogRequest {

  public String  locationName;

  public Integer locationExtCommunityId;

  public String locationExtCommunityUuid;

  public ReportAlgorithm reportAlgorithm;

  public LocalDateTime startDateTime;

  public LocalDateTime endDateTime;

  public DoorOpeningLogRequest(String name, Integer extCommunityId, String extCommunityUuid, ReportAlgorithm reportAlgorithm, LocalDateTime startDateTime, LocalDateTime endDateTime) {
    this.locationName = name;
    this.locationExtCommunityId = extCommunityId;
    this.locationExtCommunityUuid = extCommunityUuid;
    this.reportAlgorithm = reportAlgorithm;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
  }

  public DoorOpeningLogRequest() {
  }
}
