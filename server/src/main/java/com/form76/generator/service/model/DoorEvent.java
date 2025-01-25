package com.form76.generator.service.model;

import com.form76.generator.service.DateHelper;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;


@ToString
@Getter
@Setter
public class DoorEvent implements Serializable {

  private String doorName;
  private String devSn;
  private String devName;
  private String empName;
  private Integer empId;
  private String empUUID;
  private Integer eventType;
  private String eventTypeName;
  private String captureImage;
  private Integer faceMatchScore;
  private Double bodyTemperature;
  private String eventTime;
  private String pushRtEventResult;
  private Integer openStatus;
  private boolean isInEvent;


  public DoorEvent() {
  }

  public DoorEvent(String eventTime, String doorName, String devName) {
    this.eventTime = eventTime;
    this.doorName = doorName;
    this.devName = devName;
  }

  public DoorEvent(String doorName, String devSn, String devName, String empName, Integer empId, String empUUID,
                   Integer eventType, String eventTypeName, String captureImage, Integer faceMatchScore,
                   Double bodyTemperature, String eventTime, String pushRtEventResult, Integer openStatus) {
    this.doorName = doorName;
    this.devSn = devSn;
    this.devName = devName;
    this.empName = empName;
    this.empId = empId;
    this.empUUID = empUUID;
    this.eventType = eventType;
    this.eventTypeName = eventTypeName;
    this.captureImage = captureImage;
    this.faceMatchScore = faceMatchScore;
    this.bodyTemperature = bodyTemperature;
    this.eventTime = eventTime;
    this.pushRtEventResult = pushRtEventResult;
    this.openStatus = openStatus;
    this.isInEvent = devName.endsWith("-IN") || devName.endsWith("-in");
  }

  public boolean isInEvent() {
    return devName.endsWith("-IN") || devName.endsWith("-in");
  }

  public Date getEventDateTime() {
    try {
      return DateHelper.parseReportDate(eventTime);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
}

