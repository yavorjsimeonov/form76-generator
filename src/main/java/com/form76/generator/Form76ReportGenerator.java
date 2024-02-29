package com.form76.generator;

import com.form76.generator.model.DoorEvent;
import com.form76.generator.model.Employee;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Form76ReportGenerator {

  static Logger logger = Logger.getLogger(Form76GeneratorApplication.class.getName());

  private static boolean calculateOnlyFirstInAndLastOut = false;
  private static final SimpleDateFormat REPORT_TIMESTAMPS_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final SimpleDateFormat REPORT_FILE_NAME_TIMESTAMPS_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  public String generateReportFromSource(String fileName, Boolean firstLast) throws Exception {
    System.out.println("Start generating Form 76 report for source file: " + fileName);

    Map<String, Employee> employeeMap = readData(fileName);
    System.out.println("Parsed data for " + employeeMap.size() + " employees");

    calculateWorkedHours(employeeMap, firstLast);

    return generateReportFile(fileName, employeeMap, firstLast);

  }

  public Map<String, Employee> readData(String filePath) throws Exception {
    Map<String, Employee> employeeMap = new HashMap<>();

    Workbook workbook = null;
    FileInputStream file = null;
    try {
      file = new FileInputStream(filePath);
      workbook = filePath.endsWith(".xlsx") ? new XSSFWorkbook(file) : new HSSFWorkbook(file);

      Sheet sheet = workbook.getSheetAt(0); // Assuming data is on the first sheet

      for (Row row : sheet) {
        if (row.getRowNum() == 0) {
          //header row
          continue;
        }
        Cell timestampCell = row.getCell(0);
        Cell idCell = row.getCell(1);
        Cell nameCell = row.getCell(2);
        Cell doorCell = row.getCell(7);
        Cell eventPointCell = row.getCell(9);

        String timestamp = timestampCell != null ? timestampCell.getStringCellValue() : null; // Modify based on your timestamp format
        String id = idCell != null ? idCell.getStringCellValue() : null;
        String names = nameCell != null ? nameCell.getStringCellValue() : null;
        String doorName = doorCell != null ? doorCell.getStringCellValue() : null;
        String eventPointName = eventPointCell != null ? eventPointCell.getStringCellValue() : null;

        if (timestamp == null && id == null && doorName == null) {
          break;
        }

//        if (!(doorName != null && (doorName.startsWith("IN_") || doorName.startsWith("OUT_")))) {
//          System.out.printf("Unknown door type [%s]. Cannot process event.", doorName);
//          continue;
//        }
        if (!(eventPointName != null && (eventPointName.endsWith("-IN") || eventPointName.endsWith("-OUT")))) {
          System.out.printf("Unknown eventPointName type [%s]. Expecting eventPointName to end on '-IN' or '-OUT'. Cannot process event.", doorName);
          continue;
        }

        Date date = null;

        try {
          date = REPORT_TIMESTAMPS_FORMAT.parse(timestamp);
        } catch (ParseException e) {
          e.printStackTrace();
        }

        DoorEvent doorEvent = new DoorEvent(date, doorName, eventPointName);

        Employee employee  = null;
        if(employeeMap.containsKey(id)){
          employee = employeeMap.get(id);
        } else {
          employee = new Employee();
          employee.id = id;
          employee.names = names;
          employeeMap.put(employee.id, employee);
        }

        employee.doorEvents.add(doorEvent);

      }


    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      workbook.close();
      file.close();
    }

    return employeeMap;
  }

  public void calculateWorkedHours(Map<String, Employee> employeeMap, Boolean firstLast) throws ParseException {
    System.out.println("Start processing worked hours... ");

    List<Employee> employeeList = employeeMap.values().stream().toList();
    for (Employee employee :employeeList) {
      calculateWorkedHoursForEmployee(employee, firstLast);
    }

    System.out.println("End processing worked hours... ");
  }

  private void calculateWorkedHoursForEmployee(Employee employee, Boolean firstLast) throws ParseException {
    List<DoorEvent> doorEvents = employee.doorEvents;
    doorEvents.sort(Comparator.comparing(de -> de.timestamp));
//    System.out.println("Calculating worked hours (firstLast[" + firstLast + "]) for employee: " + employee.id);

    TreeMap<String, List<DoorEvent>> doorEventsPerDate = new TreeMap<>();
    doorEvents.forEach(doorEvent -> {
      String date = toDateString(doorEvent.timestamp);
      if (!doorEventsPerDate.containsKey(date)) {
        doorEventsPerDate.put(date, new ArrayList<DoorEvent>());
      }
      doorEventsPerDate.get(date).add(doorEvent);
    });
//    doorEvents.stream()
//        .sorted(Comparator.comparing(  de -> toDateString(de.timestamp)))
//        .collect(Collectors.groupingBy(  de -> toDateString(de.timestamp)));

    //System.out.println("DoorEvents per date: " + doorEventsPerDate);

    for (Map.Entry<String, List<DoorEvent>> dateDoorEvents : doorEventsPerDate.entrySet()) {
      String dateStr = dateDoorEvents.getKey();
      calculateWorkedHoursForEmployAndDate(employee, dateStr, dateDoorEvents.getValue(), firstLast);

      /*
      Duration duration = Duration.ofSeconds(employee.workedHoursPerDate.get(dateStr));
      System.out.println(String.format("Employee %s -> date %s -> Time spent on work: %dh %dm %ds\n-------------------\n",
          employee.id, dateStr, duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart()));*/
    }
  }

  private void calculateWorkedHoursForEmployAndDate(Employee employee, String date, List<DoorEvent> doorEvents, Boolean firstLast) throws ParseException {
    doorEvents.sort(Comparator.comparing(de -> de.timestamp));

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
      Date currentEventDate = DateUtils.truncate(currentDoorEvent.timestamp, Calendar.DATE);

      long timeToAdd = 0L;
      if (currentDoorEvent.isInEvent) {
        if (inEvent == null) { // ne mislq che tazi proverka e nujna
          inEvent = currentDoorEvent;
        }

        DoorEvent outEvent = null;
        while (outEvent == null) {
          DoorEvent nextEvent = doorEvents.get(++i);
          if (!nextEvent.isInEvent) {
            outEvent = nextEvent;
          } else {
            System.out.println("Unexpected inEvent[" + nextEvent + "] after another inEvent[" + inEvent + "]");
          }
        }

        timeToAdd = calculateWorkDuration(inEvent.timestamp, outEvent.timestamp);
        inEvent = null;
      } else { //!currentDoorEvent.isInEvent
        DoorEvent outEvent = currentDoorEvent;
        if (inEvent == null) {
          timeToAdd = calculateWorkDuration(DATE_FORMAT.parse(date), outEvent.timestamp);
        } else {
          timeToAdd = calculateWorkDuration(inEvent.timestamp, outEvent.timestamp);
          inEvent = null;
        }
      }

      Long workedHoursForDate = employee.workedHoursPerDate.get(date);
      employee.workedHoursPerDate.put(date, workedHoursForDate + timeToAdd);
      i++;
    }
  }

  private void calculateWorkedHoursForFirstInLastOut(Employee employee, String date, List<DoorEvent> doorEvents) {

    DoorEvent firstInEvent = null;
    DoorEvent lastOutEvent = null;

    for (DoorEvent event : doorEvents) {
      if (event.isInEvent && firstInEvent == null) {
        firstInEvent = event;
      } else if (!event.isInEvent) {
        lastOutEvent = event;
      }
    }

    if (firstInEvent != null && lastOutEvent != null) {
      long hoursWorked = calculateWorkDuration(firstInEvent.timestamp, lastOutEvent.timestamp);
      employee.workedHoursPerDate.put(date, hoursWorked);
    } else {
      System.out.println("Incomplete door events. Cannot calculate worked hours.");
      employee.workedHoursPerDate.put(date, 0L);
    }
  }

  private String toDateString(Date timestamp) {
    return DATE_FORMAT.format(DateUtils.truncate(timestamp, Calendar.DATE));
  }

  private long calculateWorkDuration(Date inEvent, Date outEvent) {
    if (inEvent == null || outEvent == null) {
      System.out.println("Failed to calculate duration for the source in[" + inEvent + "] and out[" + outEvent + "] events");
      return 0L;
    }

    try {
      return (outEvent.getTime() - inEvent.getTime());
    } catch (Exception e) {
      System.out.println("Failed to calculate duration for the source in[" + inEvent + "] and out[" + outEvent + "] events: ");
      e.printStackTrace();
    }

    return 0L;
  }
  private String generateReportFile(String srcFile, Map<String, Employee> employeeMap, Boolean firstLast) throws IOException {

    Workbook wb = createReportWorkbook();

    int extDotIndex = srcFile.lastIndexOf(".");
    String reportFileNameSuffix = "_Forma76_" + (firstLast ? "FL" : "") + REPORT_FILE_NAME_TIMESTAMPS_FORMAT.format(new Date());

    String outputFileName = extDotIndex != -1 ?
        srcFile.substring(0, extDotIndex) +  reportFileNameSuffix + srcFile.substring(extDotIndex) :
        srcFile + reportFileNameSuffix;
    System.out.println("Start exporting data in xls file: " + outputFileName);

    Form76XlsxReportBuilder form76XlsxReportBuilder = new Form76XlsxReportBuilder();
    form76XlsxReportBuilder.setEmployeesData(employeeMap);
    FileOutputStream generatedReportFile = form76XlsxReportBuilder.build().asFileOutputStream(outputFileName);
    generatedReportFile.flush();
    generatedReportFile.close();

    System.out.println("Report exported successfully.");

    return outputFileName;
  }

  private Workbook createReportWorkbook() {
    Workbook wb = new XSSFWorkbook();

    return wb;
  }

}