package com.form76.generator.service;

import com.form76.generator.service.model.DoorEvent;
import com.form76.generator.service.model.DoorOpeningLog;
import com.form76.generator.service.model.Employee;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TestDataGenerator {
  Logger logger = LoggerFactory.getLogger(TestDataGenerator.class);

  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String YEAR_MONTH_DATE_FORMAT = "yyyy-MM-dd";
  public static final String YEAR_MONTH_FORMAT = "yyyy-MM";


  public static final SimpleDateFormat SIMPLE_DATE_FORMAT_FOR_FILE_NAME = new SimpleDateFormat("yyyyMMddHHmmss");
  public static final SimpleDateFormat SIMPLE_DATE_FORMAT_FOR_DATA = new SimpleDateFormat(DATE_TIME_FORMAT);

  private final static List<String> menNames = Arrays.asList("Ivan", "Dragan", "Petkan", "Petar", "Pavel", "Anton", "Angelov");
  private final static List<String> menFamilies = Arrays.asList("Ivanov", "Draganov", "Petkanov", "Petrov", "Pavel", "Antonov", "Angelpv");
  private final static List<String> womenNames = Arrays.asList("Anna", "Boyana", "Vania", "Gergana", "Maria", "Nia", "Stela");
  private final static List<String> womenFamilies = Arrays.asList("Ivanova", "Ptrova", "Kirova", "Angelova", "Savova", "Radeva", "Mineva");


  static Random random = new Random();

  public DoorOpeningLog generateDoorOpeningLog(String startDateStr, String endDateStr) throws ParseException {
    logger.info("\nReceived report generation request for dates: startDate[" + startDateStr + "], endDate[" + endDateStr + "]");

    // Mock data generation logic
    Date startDate = SIMPLE_DATE_FORMAT_FOR_DATA.parse(startDateStr);
    Date endDate = SIMPLE_DATE_FORMAT_FOR_DATA.parse(endDateStr);

    int numberOfEmployees = 2; // random.nextInt(5, 10);

    Map<DoorEvent, Employee> doorEventsToEmployeeMap = getEmployeesDoorEventsFor(startDate, endDate, numberOfEmployees);
    List<DoorEvent> eventList = getOrderedDoorEventsList(doorEventsToEmployeeMap);

    logger.info("Generated eventList:" + eventList);
    // Create Data object
    return new DoorOpeningLog(eventList.size(), 1, eventList);
  }


  public static void createDoorEventsSourceFile(String startDateTime, String endDateTime, int numberOfEmployees, String srcFileName) throws ParseException, IOException {
    System.out.printf("Generating test file %s for time frame [%s - %s] and employees[%d]\n", srcFileName, startDateTime, endDateTime, numberOfEmployees);
    final List<String> tableHeaders = Arrays.asList(
        "Open door time", "Person ID", "Name", "Person No", "Cert No", "Dept Name", "bodyTemperature", "Device Name", "Event Code", "Event Points", "Open Door Type", "Capture"
    );

    Date startDate = SIMPLE_DATE_FORMAT_FOR_DATA.parse(startDateTime);
    Date endDate = SIMPLE_DATE_FORMAT_FOR_DATA.parse(endDateTime);

    Map<DoorEvent, Employee> doorEventsToEmployeeMap = getEmployeesDoorEventsFor(startDate, endDate, numberOfEmployees);
    List<DoorEvent> allEvents = getOrderedDoorEventsList(doorEventsToEmployeeMap);

    XSSFWorkbook workbook = new XSSFWorkbook(XSSFWorkbookType.XLSX);
    Font defaultFont = workbook.createFont();
    defaultFont.setFontHeightInPoints((short) 12);
    defaultFont.setFontName("Arial");

    XSSFSheet sheet = workbook.createSheet("Normal door opening record");

    int rowNum = 0;
    Row row = sheet.createRow(rowNum++);
    for (int i = 0; i < tableHeaders.size(); i++) {
      Cell cell = row.createCell(i);
      cell.setCellValue(tableHeaders.get(i));
    }

    for (DoorEvent doorEvent : allEvents) {
      row = sheet.createRow(rowNum++);
      for (int i = 0; i < tableHeaders.size(); i++) {
        Cell cell = row.createCell(i);
      }

      Employee employee = doorEventsToEmployeeMap.get(doorEvent);

      String timeStamp = doorEvent.getEventTime();
      row.createCell(0).setCellValue(timeStamp);
      row.createCell(1).setCellValue(employee.getId());
      row.createCell(2).setCellValue(employee.getNames());
      for (int i = 3; i <= 6; i++) {
        row.createCell(i).setCellValue("");
      }
      row.createCell(7).setCellValue(doorEvent.getDoorName());
      row.createCell(8).setCellValue("2011141576");
      row.createCell(9).setCellValue("my link samokov 11A/BUILDING1/building 1 DOOR1" + (doorEvent.isInEvent() ? "-IN" : "-OUT"));
      row.createCell(10).setCellValue("Open door card");
      row.createCell(11).setCellValue("");
    }

    for (int i = 0; i < 12; i++) {
      sheet.autoSizeColumn(i);
    }
    System.out.printf("Ready to save test file %s for time frame [%s - %s] and employees[%d]\n", srcFileName, startDateTime, endDateTime, numberOfEmployees);

    try (OutputStream reportFileOutputStream = new FileOutputStream(srcFileName)) {
      workbook.write(reportFileOutputStream);
    }
    System.out.printf("Generated successfully test file %s for time frame [%s - %s] and employees[%d]\n", srcFileName, startDateTime, endDateTime, numberOfEmployees);
  }


  // ------- Private methods --------------------
  private static List<DoorEvent> getOrderedDoorEventsList(Map<DoorEvent, Employee> allEventsMap) throws ParseException {
    List<DoorEvent> allEvents = new ArrayList<>(allEventsMap.keySet().stream().toList());
    allEvents.sort(Comparator.comparing(de -> {
      try {
        return SIMPLE_DATE_FORMAT_FOR_DATA.parse(de.getEventTime());
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
    }));

    return  allEvents;
  }

  private static Map<DoorEvent, Employee> getEmployeesDoorEventsFor(Date startDate, Date endDate, int numberOfEmployees) throws ParseException {
    Map<String, Employee> employees = generateEmployees(startDate, endDate, numberOfEmployees);
    Map<DoorEvent, Employee> allEventsMap = new HashMap<>();
    for (Employee employee : employees.values()) {
      for (DoorEvent doorEvent : employee.getDoorEvents()) {
        allEventsMap.put(doorEvent, employee);
      }
    }
    return allEventsMap;
  }


  public static Map<String, Employee> generateEmployees(Date startDate, Date endDate, int numberOfEmployees) throws ParseException {
    List<Employee> employeesList = new ArrayList<>(numberOfEmployees);
    for (int i = 0; i < numberOfEmployees; i++) {
      employeesList.add(generateEmployee());
    }

    Map<String, Employee> employees = new HashMap<String, Employee>();

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(startDate);

    while (!calendar.getTime().after(endDate)) {
      Date currentDate = calendar.getTime();
      for (int i = 0; i < numberOfEmployees; i++) {
        Employee employee = employeesList.get(i);
        employee.getDoorEvents().addAll(generateEventsForDate(employee, currentDate));
        employees.put(employee.getUuid(), employee);
      }

      calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
    }

    return employees;
  }

  private static Employee generateEmployee() {
    int employeeId = random.nextInt(1000000, 9999999);
    long employeeUuid = random.nextLong(1000000000000L, 9999999999999L);
    boolean man = random.nextBoolean();
    int indxName = random.nextInt(1, 7);
    int indxFamilyName = random.nextInt(1, 7);

    Employee employee = new Employee();
    employee.setId(employeeId);
    employee.setUuid(Long.toString(employeeUuid));
    employee.setNames(man ? menNames.get(indxName) + " " + menFamilies.get(indxFamilyName) :
        womenNames.get(indxName) + " " + womenFamilies.get(indxFamilyName));

    return employee;
  }

  private static List<DoorEvent> generateEventsForDate(Employee employee, Date date) {
    List<DoorEvent> doorEvents = new ArrayList<>();

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    int numberOfEvents = 4;; // You can randomize if needed
    int lastHour = 0;
    for (int i = 0; i < numberOfEvents; i++) {
      int hour = random.nextInt(lastHour, Math.min(lastHour + 6, 24));
      lastHour = hour;
      int minute = random.nextInt(0, 60);

      calendar.set(Calendar.HOUR_OF_DAY, hour);
      calendar.set(Calendar.MINUTE, minute);

      DoorEvent doorEvent = new DoorEvent(
          i % 2 == 0 ? "IN_door" : "OUT_door",
          "DevSn-" + (i + 1),
          i % 2 == 0 ? "eventPoint DOOR-IN" : "eventPoint DOOR-OUT",
          employee.getNames(),
          employee.getId(),
          employee.getUuid(),
          i % 2 == 0 ? 21 : 28, //TODO: to fix this according to the values in Appendix 1
          "Open door card",
          null,
          null,
          null,
          SIMPLE_DATE_FORMAT_FOR_DATA.format(calendar.getTime()),
          "Push successful",
          1);

      doorEvents.add(doorEvent);
    }

    return doorEvents;
  }

}