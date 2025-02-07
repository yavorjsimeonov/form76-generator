package com.form76.generator.rest.model;

import com.form76.generator.db.entity.Administration;
import com.form76.generator.db.entity.Device;
import com.form76.generator.db.entity.ReportAlgorithm;
import com.form76.generator.db.entity.ReportFileFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
public class LocationData {

  private String id;

  private String name;

  private Integer extCommunityId;

  private String extCommunityUuid;

  private String representativeName;

  private String representativeEmail;

  private ReportAlgorithm reportAlgorithm;

  private ReportFileFormat fileFormat;

  private boolean active;

  private boolean sendEmail;

  private String administrationId;

  private Set<Device> devices;

}
