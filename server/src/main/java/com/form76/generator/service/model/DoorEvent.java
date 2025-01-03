package com.form76.generator.service.model;

import com.form76.generator.service.DateHelper;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;


@ToString
public class DoorEvent implements Serializable {

  public String doorName;
  public String devSn;
  public String devName;
  public String empName;
  public Integer empId;
  public String empUUID;
  public Integer eventType;
  public String eventTypeName;
  public String captureImage;
  public Integer faceMatchScore;
  public Double bodyTemperature;
  public String eventTime;
  public String pushRtEventResult;
  public Integer openStatus;
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


  public String getDoorName() {
    return doorName;
  }

  public String getDevSn() {
    return devSn;
  }

  public String getDevName() {
    return devName;
  }

  public String getEmpName() {
    return empName;
  }

  public Integer getEmpId() {
    return empId;
  }

  public String getEmpUUID() {
    return empUUID;
  }

  public Integer getEventType() {
    return eventType;
  }

  public String getEventTypeName() {
    return eventTypeName;
  }

  public String getCaptureImage() {
    return captureImage;
  }

  public Integer getFaceMatchScore() {
    return faceMatchScore;
  }

  public Double getBodyTemperature() {
    return bodyTemperature;
  }

  public String getEventTime() {
    return eventTime;
  }

  public String getPushRtEventResult() {
    return pushRtEventResult;
  }

  public Integer getOpenStatus() {
    return openStatus;
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

