package com.form76.generator.service.model;

import com.form76.generator.db.entity.Location;
import com.form76.generator.db.entity.ReportAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class DoorOpeningLogRequest {

  private String  locationId;

  private String  locationName;

  private Integer locationExtCommunityId;

  private String locationExtCommunityUuid;

  private ReportAlgorithm reportAlgorithm;

  private LocalDateTime startDateTime;

  private LocalDateTime endDateTime;
}
