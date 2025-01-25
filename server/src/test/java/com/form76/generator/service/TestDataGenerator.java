package com.form76.generator.service;

import com.form76.generator.service.model.DoorEvent;
import com.form76.generator.service.model.Employee;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class TestDataGenerator {

  public static final SimpleDateFormat SIMPLE_DATE_FORMAT_FOR_FILE_NAME = new SimpleDateFormat("yyyyMMddHHmmss");
  public static final SimpleDateFormat SIMPLE_DATE_FORMAT_FOR_DATA = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private final static List<String> menNames = Arrays.asList("Ivan", "Dragan", "Petkan", "Petar", "Pavel", "Anton", "Angelov");
  private final static List<String> menFamilies = Arrays.asList("Ivanov", "Draganov", "Petkanov", "Petrov", "Pavel", "Antonov", "Angelpv");
  private final static List<String> womenNames = Arrays.asList("Anna", "Boyana", "Vania", "Gergana", "Maria", "Nia", "Stela");
  private final static List<String> womenFamilies = Arrays.asList("Ivanova", "Ptrova", "Kirova", "Angelova", "Savova", "Radeva", "Mineva");


  static Random random;


  // ------------- Private -----------
  public static Map<String, Map<String, Employee>> generateEmployees(List<Integer> months, int numberOfEmployees) throws ParseException {
    random = new Random(System.currentTimeMillis());
    Map<String, Map<String, Employee>> monthEmployee = new HashMap<String, Map<String, Employee>>();

    for (Integer month : months) {
      Map<String, Employee> employees = new HashMap<String, Employee>();

      for (int i = 0; i < numberOfEmployees; i++) {
        Employee employee = generateEmployee(month);

        employees.put(employee.getUuid(), employee);
      }

      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.MONTH, month - 1);
      monthEmployee.put(new SimpleDateFormat(DateHelper.YEAR_MONTH_FORMAT).format(calendar.getTime()), employees);
    }

//    Form76ReportGenerator generator = new Form76ReportGenerator();
//    generator.calculateWorkedHours(monthEmployee, false);

    return monthEmployee;
  }

  public static Employee generateEmployee(int month) {

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

    int maxDays = 31;
    if (month == 2) {
      maxDays = 28;
    } else if ( month < 8 && (month % 2 == 0) || month > 8 && (month % 2 != 0)) {
      maxDays = 30;
    }
    for (int dayOfMonth = 1; dayOfMonth <= maxDays; dayOfMonth++) {

      employee.getDoorEvents().addAll(Objects.requireNonNull(generateEventsForDate(month, dayOfMonth)));
    }

    return employee;
  }

  public static List<DoorEvent> generateEventsForDate(int month, int dayOfMonth) {
    List<DoorEvent> doorEvents = new ArrayList<>();

    LocalDateTime localDateTime = LocalDateTime.now();
    localDateTime = localDateTime.withMonth(month);
    localDateTime = localDateTime.withDayOfMonth(dayOfMonth);

    int numberOfEvents = 8; // random.nextInt(2, 10);
    int hour = 0;
    for (int i = 0; i < numberOfEvents; i++) {
      hour += Math.min(random.nextInt(1, 4), 23);
      localDateTime = localDateTime.withHour(hour);
      localDateTime = localDateTime.withMinute(random.nextInt(0, 59));

      DoorEvent doorEvent = new DoorEvent(
          DateHelper.formatReportDate(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())),
          i % 2 == 0 ? "IN_door" : "OUT_door",
          i % 2 == 0 ? "eventPoint DOOR-IN" : "eventPoint DOOR-OUT"
      ); // random.nextBoolean() ? "IN_door" : "OUT_door";

      doorEvents.add(doorEvent);
    }

    return doorEvents;
  }


  public static void createDoorEventsSourceFile(String months, int numberOfEmployees, String srcFileName) throws ParseException, IOException {
    System.out.printf("Generating test file %s for month[%s] and empls[%d]\n", srcFileName, months, numberOfEmployees);
    final List<String> tableHeaders = Arrays.asList(
        "Open door time", "Person ID", "Name", "Person No", "Cert No", "Dept Name", "bodyTemperature", "Device Name", "Event Code", "Event Points", "Open Door Type", "Captur"
    );

    List<Integer> monthsNumbers = Arrays.stream(months.split(",")).map(String::trim).map(Integer::valueOf).toList();
    Map<String, Map<String, Employee>> employees = generateEmployees(monthsNumbers, numberOfEmployees);

    XSSFWorkbook workbook = new XSSFWorkbook(XSSFWorkbookType.XLSX);
    Font defaultFont = workbook.createFont();
    defaultFont.setFontHeightInPoints((short)12);
    defaultFont.setFontName("Arial");

    XSSFSheet sheet = workbook.createSheet("Normal door opening record");

    int rowNum = 0;
    Row row = sheet.createRow(rowNum++);
    for (int i = 0; i < tableHeaders.size(); i++) {
      Cell cell = row.createCell(i);
      cell.setCellValue(tableHeaders.get(i));
    }

    Map<DoorEvent, Employee> allEventsMap = new HashMap<>();
    for (Employee employee : employees.values().stream().map(Map::values).flatMap(Collection::stream).toList()) {
      for (DoorEvent doorEvent : employee.getDoorEvents()) {
        allEventsMap.put(doorEvent, employee);
      }
    }
    List<DoorEvent> allEvents = new ArrayList<>(allEventsMap.keySet().stream().toList());
    allEvents.sort(Comparator.comparing(de -> {
      try {
        return DateHelper.parseReportDate(de.getEventTime());
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
    }));

    for (DoorEvent doorEvent : allEvents) {
      row = sheet.createRow(rowNum++);
      for (int i = 0; i < tableHeaders.size(); i++) {
        Cell cell = row.createCell(i);
      }

      Employee employee = allEventsMap.get(doorEvent);

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
    System.out.printf("Ready to save test file %s for months[%s] and empls[%d]\n", srcFileName, months, numberOfEmployees);

    OutputStream reportFileOutputStream = new FileOutputStream(srcFileName);
    workbook.write(reportFileOutputStream);
    reportFileOutputStream.flush();
    reportFileOutputStream.close();
    System.out.printf("Generated successfully test file %s for months[%s] and empls[%d]\n", srcFileName, months, numberOfEmployees);

  }
}
