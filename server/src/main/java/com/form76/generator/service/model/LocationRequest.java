package com.form76.generator.service.model;


import com.form76.generator.db.entity.ReportAlgorithm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRequest {
  private String name;
  private Integer extCommunityId;
  private String extCommunityUuid;
  private String representativeName;
  private String representativeEmail;
  private ReportAlgorithm reportAlgorithm;
  private boolean active;
  private String administrationId;
}
