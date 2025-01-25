package com.form76.generator.service.model;

import com.form76.generator.db.entity.Location;
import com.form76.generator.db.entity.ReportAlgorithm;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class DoorOpeningLogRequest {

  private String  locationName;

  private Integer locationExtCommunityId;

  private String locationExtCommunityUuid;

  private ReportAlgorithm reportAlgorithm;

  private LocalDateTime startDateTime;

  private LocalDateTime endDateTime;

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
