package com.form76.generator.service;

import com.form76.generator.db.entity.Location;
import com.form76.generator.rest.model.LocationData;
import com.form76.generator.db.entity.ReportAlgorithm;
import com.form76.generator.kafka.ReportGenerationRequestEventProducer;
import com.form76.generator.rest.model.ReportData;
import com.form76.generator.service.model.*;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class Form76ReportService {

  Logger logger = LoggerFactory.getLogger(Form76ReportService.class);

  static boolean xlsxFile = true;

  private static final List<String> ACCEPTED_OPEN_DOOR_TYPES = Arrays.asList("Mobile phone bluetooth door", "Face open door", "Open door card", "Open the door remotely");

  @Value("${form76-generator.output.file.name.format}")
  private String outputFileNameFormat;

  @Autowired
  LocationService locationService;

  @Autowired
  MylinkApiService mylinkApiService;

  @Autowired
  EmailService emailService;

  @Autowired
  ReportService reportService;

  @Autowired
  ReportGenerationRequestEventProducer reportGenerationRequestEventProducer;

  @Autowired
  UploadReportService uploadReportService;

  /**
   *   OK load active locations with active administrations (call method in LocationService)
   *   for every location:
   *   OK - generate DoorOpeningLogRequest and call MylinkService.loadDoorOpeningLog
   *   OK - with the received data -> generate report
   *   TODO: make the two steps async using kafka
   * @throws ParseException
   */
  public void triggerReportsGeneration() {

    try {
      List<LocationData> locations = locationService.getActiveLocationsInActiveAdministrations();

      logger.info("Found active locations: " + locations);

      for (LocationData locationData : locations) {
        // Generate DoorOpeningLogRequest for each location
        DoorOpeningLogRequest request = new DoorOpeningLogRequest(
            locationData.getId(),
            locationData.getName(),
            locationData.getExtCommunityId(),
            locationData.getExtCommunityUuid(),
            locationData.getReportAlgorithm(),
            LocalDateTime.now().minusMonths(1),
            LocalDateTime.now()
        );

        reportGenerationRequestEventProducer.publishReportGenerationRequest(request.getLocationExtCommunityUuid(), request);

      }
    } catch (Exception e) {
      logger.error("Failed to generate reports:", e);
    }
  }

  public void generateReportForLocation(DoorOpeningLogRequest request) throws ParseException {
    try {
      logger.info("Processing location: " + request.getLocationName() + " (" + request.getLocationExtCommunityUuid() + ")");
      DoorOpeningLogResponse response = mylinkApiService.loadDoorOpeningLog(request);
      //logger.info("Response: " + response);


      Map<String, Map<String, Employee>> monthEmployeeMap = readDataFromResponse(response);
      boolean firstLast = request.getReportAlgorithm() == ReportAlgorithm.FIRST_IN_LAST_OUT;
      calculateWorkedHours(request.getLocationName(), request.getLocationExtCommunityUuid(), monthEmployeeMap, firstLast);

      String generatedFileName = generateReportFile(request.getLocationExtCommunityUuid(), monthEmployeeMap, firstLast);

      logger.info("generatedFileName: " + generatedFileName);

      EmailRequest emailRequest = new EmailRequest();
      emailRequest.setRecipient("yavorjsimeonov@gmail.com");
      emailRequest.setMsgBody(generatedFileName);
      emailRequest.setSubject("test");
      emailRequest.setAttachment(generatedFileName);

      uploadReportService.uploadFile(generatedFileName);

      //create report record in record table
      reportService.saveReport(new ReportData(
          null, generatedFileName, LocalDateTime.now(),
          null,
          request.getStartDateTime(), request.getEndDateTime(),
          request.getLocationId(), request.getLocationName()
      ));
      emailService.sendMailWithAttachment(emailRequest);
    } catch (Exception e) {
      logger.error("Error generating report for location: " + request.getLocationName(), e);
    }
  }

  private Map<String, Map<String, Employee>> readDataFromResponse(DoorOpeningLogResponse doorOpeningLogResponse) throws ParseException {
    Map<String, Map<String, Employee>> monthEmployeeMap = new HashMap<>();
    List<DoorEvent> allEvents = doorOpeningLogResponse.getData().getList();

    for (DoorEvent event : allEvents) {
      //Validate event and log errors:
//      if (timestamp == null && id == null && doorName == null) {
//        break;
//      }
//
//      if (id == null || names == null) {
//        rowsWrongPerson.add(row.getRowNum());
//        continue;
//      }
//
//      if (!(eventPointName != null && (eventPointName.endsWith("-IN") || eventPointName.endsWith("-in") || eventPointName.endsWith("-OUT") || eventPointName.endsWith("-out")))) {
//        rowsWrongEventPoints.add(row.getRowNum());
//        continue;
//      }
//
//      if (openDoorType == null || !ACCEPTED_OPEN_DOOR_TYPES.contains(openDoorType)) {
//        rowsIgnoredDoorOpenTypes.add(row.getRowNum());
//        ignoredDoorOpenTypes.add(openDoorType);
//        continue;
//      }

      String yearMonthKey = DateHelper.getYearAndMonthFromDateString(event.getEventTime());
      Map<String, Employee> employeesMap = monthEmployeeMap.computeIfAbsent(yearMonthKey, k -> new HashMap<>());
      Employee employee = employeesMap.computeIfAbsent(Integer.toString(event.getEmpId()), k -> new Employee(event.getEmpId(), event.getEmpName()));

      employee.getDoorEvents().add(event);
    }

    return monthEmployeeMap;
  }

  public void calculateWorkedHours(String locationName, String locationExtCommunityUuid, Map<String, Map<String, Employee>> monthEmployeeMap, Boolean firstLast) throws ParseException {
    String locationInfo = locationName + " (uuid: " + locationExtCommunityUuid + ")";
    logger.info("Start processing worked hours for employees for location: " + locationInfo);

    List<Map<String, Employee>> employeeMapList = monthEmployeeMap.values().stream().toList();
    //logger.info("employeeMapList: " + employeeMapList);

    for (Map<String, Employee> map : employeeMapList) {
      List<Employee> employeeList = map.values().stream().toList();
      for (Employee employee : employeeList) {
        calculateWorkedHoursForEmployee(employee, firstLast);
      }
    }

    logger.info("End processing worked hours for employees for location: " + locationInfo);
  }

  private void calculateWorkedHoursForEmployee(Employee employee, Boolean firstLast) throws ParseException {
    List<DoorEvent> doorEvents = employee.getDoorEvents();
    doorEvents.sort(Comparator.comparing(de -> de.getEventDateTime()));

    TreeMap<String, List<DoorEvent>> doorEventsPerDate = new TreeMap<>();
    doorEvents.forEach(doorEvent -> {
      String date = DateHelper.toDateString(doorEvent.getEventDateTime());
      if (!doorEventsPerDate.containsKey(date)) {
        doorEventsPerDate.put(date, new ArrayList<>());
      }
      doorEventsPerDate.get(date).add(doorEvent);
    });

    for (Map.Entry<String, List<DoorEvent>> dateDoorEvents : doorEventsPerDate.entrySet()) {
      String dateStr = dateDoorEvents.getKey();
      calculateWorkedHoursForEmployAndDate(employee, dateStr, dateDoorEvents.getValue(), firstLast);
    }
  }

  private void calculateWorkedHoursForEmployAndDate(Employee employee, String date, List<DoorEvent> doorEvents, Boolean firstLast) throws ParseException {
    doorEvents.sort(Comparator.comparing(de -> de.getEventTime()));

    if(firstLast) {
      calculateWorkedHoursForFirstInLastOut(employee, date, doorEvents);
    } else {
      calculateWorkHoursForEveryInOut(employee, date, doorEvents);
    }

  }

  private void calculateWorkHoursForEveryInOut(Employee employee, String date, List<DoorEvent> doorEvents) throws ParseException {
    employee.getWorkedHoursPerDate().put(date, 0L);

    int i = 0;
    DoorEvent inEvent = null;
    while (i <  doorEvents.size()) {
      DoorEvent currentDoorEvent = doorEvents.get(i);
      Date currentEventDate = DateUtils.truncate(DateHelper.parseReportDate(currentDoorEvent.getEventTime()), Calendar.DATE);

      long timeToAdd = 0L;
      if (currentDoorEvent.isInEvent()) {
        if (inEvent == null) { // ne mislq che tazi proverka e nujna
          inEvent = currentDoorEvent;
        }

        DoorEvent outEvent = null;
        while (outEvent == null && i <  doorEvents.size() - 1) {
          DoorEvent nextEvent = doorEvents.get(++i);
          if (!nextEvent.isInEvent()) {
            outEvent = nextEvent;
          } else {
            logger.info("Unexpected inEvent[" + nextEvent + "] after another inEvent[" + inEvent + "]");
          }
        }

        timeToAdd = calculateWorkDuration(DateHelper.parseReportDate(inEvent.getEventTime()), outEvent != null ? DateHelper.parseReportDate(outEvent.getEventTime()) : null);
        inEvent = null;
      } else { //!currentDoorEvent.isInEvent
        DoorEvent outEvent = currentDoorEvent;
        if (inEvent == null) {
          timeToAdd = calculateWorkDuration(DateHelper.parseDate(date), DateHelper.parseReportDate(outEvent.getEventTime()));
        } else {
          timeToAdd = calculateWorkDuration(DateHelper.parseReportDate(inEvent.getEventTime()), DateHelper.parseReportDate(outEvent.getEventTime()));
          inEvent = null;
        }
      }

      Long workedHoursForDate = employee.getWorkedHoursPerDate().get(date);
      employee.getWorkedHoursPerDate().put(date, workedHoursForDate + timeToAdd);
      i++;
    }
  }

  private void calculateWorkedHoursForFirstInLastOut(Employee employee, String date, List<DoorEvent> doorEvents) throws ParseException {

    DoorEvent firstInEvent = null;
    DoorEvent lastOutEvent = null;

    for (DoorEvent event : doorEvents) {
      if (event.isInEvent() && firstInEvent == null) {
        firstInEvent = event;
      } else if (!event.isInEvent()) {
        lastOutEvent = event;
      }
    }

    if (firstInEvent != null && lastOutEvent != null) {
      long hoursWorked = calculateWorkDuration(DateHelper.parseReportDate(firstInEvent.getEventTime()), DateHelper.parseReportDate(lastOutEvent.getEventTime()));
      employee.getWorkedHoursPerDate().put(date, hoursWorked);
    } else {
      logger.info("Incomplete door events(firstInEvent[" + firstInEvent +"], lastOutEvent=[" + lastOutEvent + "]. Cannot calculate worked hours.");
      employee.getWorkedHoursPerDate().put(date, 0L);
    }
  }

  private long calculateWorkDuration(Date inEvent, Date outEvent) {
    if (inEvent == null || outEvent == null) {
      logger.info("Failed to calculate duration for the source in[" + inEvent + "] and out[" + outEvent + "] events");
      return 0L;
    }

    try {
      return (outEvent.getTime() - inEvent.getTime());
    } catch (Exception e) {
      logger.info("Failed to calculate duration for the source in[" + inEvent + "] and out[" + outEvent + "] events: ");
      e.printStackTrace();
    }

    return 0L;
  }
  private String generateReportFile(String locationExtCommunityUuid, Map<String, Map<String, Employee>> monthEmployeeMap, Boolean firstLast) throws IOException, ParseException {

    String outputFileName = getOutputFileName(locationExtCommunityUuid, firstLast);
    logger.info("Start exporting data in xls file: " + outputFileName);

    Form76XlsxReportBuilder form76XlsxReportBuilder = new Form76XlsxReportBuilder();
    form76XlsxReportBuilder.setEmployeesData(monthEmployeeMap);
    FileOutputStream generatedReportFile = form76XlsxReportBuilder.build().asFileOutputStream("/tmp/" + outputFileName);
    generatedReportFile.flush();
    generatedReportFile.close();

    logger.info("Report exported successfully.");

    return outputFileName;
  }

  private String getOutputFileName(String locationExtCommunityUuid, Boolean firstLast) {
    return "Report_Forma76_" + locationExtCommunityUuid + "_" + (firstLast ? "FL_" : "") + DateHelper.getTimeStampForReportFile(new Date()) + ".xlsx";
  }
}