package com.form76.generator;

import com.form76.generator.model.DoorEvent;
import com.form76.generator.model.Employee;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Form76ReportGenerator {

  static Logger logger = Logger.getLogger(Form76GeneratorApplication.class.getName());

  private static final SimpleDateFormat REPORT_TIMESTAMPS_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final SimpleDateFormat REPORT_FILE_NAME_TIMESTAMPS_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  public String generateReportFromSource(String fileName) throws Exception {
    System.out.println("Start generating Form 76 report for source file: " + fileName);

    Map<String, Employee> employeeMap = readData(fileName);
    System.out.println("Received data for " + employeeMap.size() + " employees");

    calculateWorkedHours(employeeMap);

    return generateReportFile(fileName, employeeMap);

  }

  public Map<String, Employee> readData(String filePath) throws Exception {
    Map<String, Employee> employeeMap = new HashMap<>();

    Workbook workbook = null;
    FileInputStream file = null;
    try {
      file = new FileInputStream(filePath);
      workbook = new XSSFWorkbook(file);
      Sheet sheet = workbook.getSheetAt(0); // Assuming data is on the first sheet

      for (Row row : sheet) {
        if (row.getRowNum() == 0) {
          //header row
          continue;
        }
        Cell timestampCell = row.getCell(0); // Assuming timestamp is in the first column
        Cell idCell = row.getCell(1); // Assuming ID is in the second column
        Cell nameCell = row.getCell(2); // Assuming ID is in the second column
        Cell doorCell = row.getCell(7); // Assuming action is in the fourth column

        String timestamp = timestampCell != null ? timestampCell.getStringCellValue() : null; // Modify based on your timestamp format
        String id = idCell != null ? idCell.getStringCellValue() : null;
        String names = nameCell != null ? nameCell.getStringCellValue() : null;
        String doorName = doorCell != null ? doorCell.getStringCellValue() : null;

        if (timestamp == null && id == null && doorName == null) {
          break;
        }

        if (!(doorName != null && (doorName.startsWith("IN_") || doorName.startsWith("OUT_")))) {
          System.out.printf("Unknown door type [%s]. Cannot process event.", doorName);
          continue;
        }

        Date date = null;

        try {
          date = REPORT_TIMESTAMPS_FORMAT.parse(timestamp);
          System.out.println(date);
        } catch (ParseException e) {
          e.printStackTrace();
        }

        DoorEvent doorEvent = new DoorEvent(date, doorName);

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

  public void calculateWorkedHours(Map<String, Employee> employeeMap) throws ParseException {
    List<Employee> employeeList = employeeMap.values().stream().toList();

    for (Employee employee :employeeList) {
      calculateWorkedHoursForEmployee(employee);
    }

    System.out.println("Calculated worked hours... ");

  }

  private void calculateWorkedHoursForEmployee(Employee employee) throws ParseException {
    List<DoorEvent> doorEvents = employee.doorEvents;
    doorEvents.sort(Comparator.comparing(de -> de.timestamp));
    System.out.println("Calculating worked hours for employee: " + employee);

    Map<String, List<DoorEvent>> doorEventsPerDate = doorEvents.stream().collect(
        Collectors.groupingBy(  de -> toDateString(de.timestamp) ));

    System.out.println("DoorEvents per date: " + doorEventsPerDate);

    for (Map.Entry<String, List<DoorEvent>> dateDoorEvents : doorEventsPerDate.entrySet()) {
      String dateStr = dateDoorEvents.getKey();
      calculateWorkedHoursForEmployAndDate(employee, dateStr, dateDoorEvents.getValue());

      Duration duration = Duration.ofSeconds(employee.workedHoursPerDate.get(dateStr));
      System.out.println(String.format("Employee %s -> date %s -> Time spent on work: %dh %dm %ds\n-------------------\n",
          employee.id, dateStr, duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart()));
    }
  }

  private void calculateWorkedHoursForEmployAndDate(Employee employee, String date, List<DoorEvent> doorEvents) throws ParseException {
    employee.workedHoursPerDate.put(date, 0L);

    int i = 0;
    DoorEvent inEvent = null;
    while (i <  doorEvents.size()) {
      DoorEvent currentDoorEvent = doorEvents.get(i);
      Date currentEventDate = DateUtils.truncate(currentDoorEvent.timestamp, Calendar.DATE);

      long timeToAdd = 0L;
      if (currentDoorEvent.isInEvent) {
        if (inEvent == null) {
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
  private String generateReportFile(String srcFile, Map<String, Employee> employeeMap) throws IOException {

    Workbook wb = createReportWorkbook();

    int extDotIndex = srcFile.lastIndexOf(".");
    String reportFileNameSuffix = "_Report_Forma76_" + REPORT_FILE_NAME_TIMESTAMPS_FORMAT.format(new Date());

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