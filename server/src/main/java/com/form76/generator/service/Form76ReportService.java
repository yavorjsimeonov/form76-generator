package com.form76.generator.service;

import com.form76.generator.db.entity.Location;
import com.form76.generator.db.entity.ReportAlgorithm;
import com.form76.generator.kafka.ReportGenerationRequestEventProducer;
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

  private static final SimpleDateFormat REPORT_TIMESTAMPS_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final SimpleDateFormat REPORT_FILE_NAME_TIMESTAMPS_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
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
  ReportGenerationRequestEventProducer reportGenerationRequestEventProducer;

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
      List<Location> locations = locationService.getActiveLocationsInActiveAdministrations();

      logger.info("Found active locations: " + locations);

      for (Location location : locations) {
        // Generate DoorOpeningLogRequest for each location
        DoorOpeningLogRequest request = new DoorOpeningLogRequest(
            location,
            LocalDateTime.now().minusMonths(1),
            LocalDateTime.now()
        );

        reportGenerationRequestEventProducer.publishReportGenerationRequest(request.location.extCommunityUuId, request);

      }
    } catch (Exception e) {
      logger.error("Failed to generate reports:", e);
    }
  }

  public void generateReportForLocation(DoorOpeningLogRequest request) throws ParseException {
    try {
      logger.info("Processing location: " + request.location);
      DoorOpeningLogResponse response = mylinkApiService.loadDoorOpeningLog(request);
      //logger.info("Response: " + response);


      Map<String, Map<String, Employee>> monthEmployeeMap = readDataFromResponse(response);
      boolean firstLast = request.location.reportAlgorithm == ReportAlgorithm.FIRST_IN_LAST_OUT;
      calculateWorkedHours(request.location, monthEmployeeMap, firstLast);

      String generatedFilePath = generateReportFile(request.location, monthEmployeeMap, firstLast);

      logger.info("generatedFilePath: " + generatedFilePath);

      EmailRequest emailRequest = new EmailRequest();
      emailRequest.recipient = "yavorjsimeonov@gmail.com";
      emailRequest.msgBody = generatedFilePath;
      emailRequest.subject = "test";
      emailRequest.attachment = null;

      emailService.sendSimpleMail(emailRequest);
    } catch (Exception e) {
      logger.error("Error generating report for location: " + request.location.name, e);
    }
  }

  private Map<String, Map<String, Employee>> readDataFromResponse(DoorOpeningLogResponse doorOpeningLogResponse) throws ParseException {
    Map<String, Map<String, Employee>> monthEmployeeMap = new HashMap<>();
    List<DoorEvent> allEvents = doorOpeningLogResponse.data.list;

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

      String yearMonthKey = DateHelper.getYearAndMonthFromDateString(event.eventTime);
      Map<String, Employee> employeesMap = monthEmployeeMap.computeIfAbsent(yearMonthKey, k -> new HashMap<>());
      Employee employee = employeesMap.computeIfAbsent(Integer.toString(event.empId), k -> new Employee(event.empId, event.empName));

      employee.doorEvents.add(event);
    }

    return monthEmployeeMap;
  }

  public void calculateWorkedHours(Location location, Map<String, Map<String, Employee>> monthEmployeeMap, Boolean firstLast) throws ParseException {
    String locationInfo = location.name + " (uuid: " + location.extCommunityUuId + ")";
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
    List<DoorEvent> doorEvents = employee.doorEvents;
    doorEvents.sort(Comparator.comparing(de -> de.getEventDateTime()));

    TreeMap<String, List<DoorEvent>> doorEventsPerDate = new TreeMap<>();
    doorEvents.forEach(doorEvent -> {
      String date = toDateString(doorEvent.getEventDateTime());
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
    doorEvents.sort(Comparator.comparing(de -> de.eventTime));

    if(firstLast) {
      calculateWorkedHoursForFirstInLastOut(employee, date, doorEvents);
    } else {
      calculateWorkHoursForEveryInOut(employee, date, doorEvents);
    }

  }

  private void calculateWorkHoursForEveryInOut(Employee employee, String date, List<DoorEvent> doorEvents) throws ParseException {
    employee.workedHoursPerDate.put(date, 0L);

    int i = 0;
    DoorEvent inEvent = null;
    while (i <  doorEvents.size()) {
      DoorEvent currentDoorEvent = doorEvents.get(i);
      Date currentEventDate = DateUtils.truncate(REPORT_TIMESTAMPS_FORMAT.parse(currentDoorEvent.eventTime), Calendar.DATE);

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

        timeToAdd = calculateWorkDuration(REPORT_TIMESTAMPS_FORMAT.parse(inEvent.eventTime), outEvent != null ? REPORT_TIMESTAMPS_FORMAT.parse(outEvent.eventTime) : null);
        inEvent = null;
      } else { //!currentDoorEvent.isInEvent
        DoorEvent outEvent = currentDoorEvent;
        if (inEvent == null) {
          timeToAdd = calculateWorkDuration(DATE_FORMAT.parse(date), REPORT_TIMESTAMPS_FORMAT.parse(outEvent.eventTime));
        } else {
          timeToAdd = calculateWorkDuration(REPORT_TIMESTAMPS_FORMAT.parse(inEvent.eventTime), REPORT_TIMESTAMPS_FORMAT.parse(outEvent.eventTime));
          inEvent = null;
        }
      }

      Long workedHoursForDate = employee.workedHoursPerDate.get(date);
      employee.workedHoursPerDate.put(date, workedHoursForDate + timeToAdd);
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
      long hoursWorked = calculateWorkDuration(REPORT_TIMESTAMPS_FORMAT.parse(firstInEvent.eventTime), REPORT_TIMESTAMPS_FORMAT.parse(lastOutEvent.eventTime));
      employee.workedHoursPerDate.put(date, hoursWorked);
    } else {
      logger.info("Incomplete door events(firstInEvent[" + firstInEvent +"], lastOutEvent=[" + lastOutEvent + "]. Cannot calculate worked hours.");
      employee.workedHoursPerDate.put(date, 0L);
    }
  }

  private String toDateString(Date timestamp) {
    return DATE_FORMAT.format(DateUtils.truncate(timestamp, Calendar.DATE));
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
  private String generateReportFile(Location location, Map<String, Map<String, Employee>> monthEmployeeMap, Boolean firstLast) throws IOException, ParseException {

    String outputFileName = getOutputFileName(location, firstLast);
    logger.info("Start exporting data in xls file: " + outputFileName);

    Form76XlsxReportBuilder form76XlsxReportBuilder = new Form76XlsxReportBuilder();
    form76XlsxReportBuilder.setEmployeesData(monthEmployeeMap);
    FileOutputStream generatedReportFile = form76XlsxReportBuilder.build().asFileOutputStream(outputFileName);
    generatedReportFile.flush();
    generatedReportFile.close();

    logger.info("Report exported successfully.");

    return outputFileName;
  }

  private String getOutputFileName(Location location, Boolean firstLast) {
    return "Report_Forma76_" + location.extCommunityId + "_" + (firstLast ? "FL_" : "") + REPORT_FILE_NAME_TIMESTAMPS_FORMAT.format(new Date());
  }
}