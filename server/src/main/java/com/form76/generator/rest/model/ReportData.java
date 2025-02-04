package com.form76.generator.rest.model;

import com.form76.generator.db.entity.Location;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ReportData {

  private String id;

  private String fileName;

  private LocalDateTime creationDate;

  private String cloudStorageReference;

  private LocalDateTime reportPeriodStartDateTime;

  private LocalDateTime reportPeriodEndDateTime;

  private String locationId;

  private String locationName;

  private String administrationName;
}
