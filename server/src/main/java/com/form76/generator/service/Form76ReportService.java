package com.form76.generator.service;

import com.form76.generator.rest.Form76GeneratorResource;
import com.form76.generator.service.model.DoorEvent;
import com.form76.generator.service.model.Employee;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Form76ReportService {

  Logger logger = LoggerFactory.getLogger(Form76ReportService.class);

  static boolean xlsxFile = true;

  public static final String YEAR_MONTH_DATE_FORMAT = "yyyy-MM";

  private static boolean calculateOnlyFirstInAndLastOut = false;
  private static final SimpleDateFormat REPORT_TIMESTAMPS_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final SimpleDateFormat REPORT_FILE_NAME_TIMESTAMPS_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  private static final List<String> ACCEPTED_OPEN_DOOR_TYPES = Arrays.asList("Mobile phone bluetooth door", "Face open door", "Open door card", "Open the door remotely");

  @Value("${form76-generator.output.file.name.format}")
  private String outputFileNameFormat;

  public String generateReportFromSource(String fileName, Boolean firstLast) throws Exception {
    logger.info("Start generating Form 76 report for source file: " + fileName);

    Map<String, Map<String, Employee>> monthEmployeeMap = readData(fileName);

    calculateWorkedHours(monthEmployeeMap, firstLast);

    return generateReportFile(fileName, monthEmployeeMap, firstLast);

  }

  public Map<String, Map<String, Employee>> readData(String filePath) throws Exception {
    Map<String, Map<String, Employee>> monthEmployeeMap = new HashMap<>();

    Workbook workbook = null;
    FileInputStream file = null;
    try {
      file = new FileInputStream(filePath);
      // workbook = filePath.endsWith(".xlsx") ? new XSSFWorkbook(file) : new HSSFWorkbook(file);

      if(filePath.endsWith(".xlsx")){
        workbook = new XSSFWorkbook(file);
      } else {
        workbook = new HSSFWorkbook(file);
        xlsxFile = false;
      }

      Sheet sheet = workbook.getSheetAt(0); // Assuming data is on the first sheet

      List<Integer> rowsWrongPerson = new ArrayList<>();
      List<Integer> rowsWrongEventPoints = new ArrayList<>();
      List<Integer> rowsIgnoredDoorOpenTypes = new ArrayList<>();
      Set<String> ignoredDoorOpenTypes = new HashSet<>();

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
        Cell openDoorTypeCell = row.getCell(10);

        String timestamp = timestampCell != null ? timestampCell.getStringCellValue() : null; // Modify based on your timestamp format
        String id = idCell != null ? idCell.getStringCellValue() : null;
        String names = nameCell != null ? nameCell.getStringCellValue() : null;
        String doorName = doorCell != null ? doorCell.getStringCellValue() : null;
        String eventPointName = eventPointCell != null ? eventPointCell.getStringCellValue() : null;
        String openDoorType = openDoorTypeCell != null ? openDoorTypeCell.getStringCellValue() : null;

        if (timestamp == null && id == null && doorName == null) {
          break;
        }

        if (id == null || names == null) {
          rowsWrongPerson.add(row.getRowNum());
          continue;
        }

        if (!(eventPointName != null && (eventPointName.endsWith("-IN") || eventPointName.endsWith("-in") || eventPointName.endsWith("-OUT") || eventPointName.endsWith("-out")))) {
          rowsWrongEventPoints.add(row.getRowNum());
          continue;
        }

        if (openDoorType == null || !ACCEPTED_OPEN_DOOR_TYPES.contains(openDoorType)) {
          rowsIgnoredDoorOpenTypes.add(row.getRowNum());
          ignoredDoorOpenTypes.add(openDoorType);
          continue;
        }

        Date date = null;

        try {
          date = REPORT_TIMESTAMPS_FORMAT.parse(timestamp);
        } catch (ParseException e) {
          e.printStackTrace();
        }

        SimpleDateFormat monthExtractionFormat = new SimpleDateFormat(YEAR_MONTH_DATE_FORMAT);

        String monthKey = monthExtractionFormat.format(date);
        Map<String, Employee> employeeMap = monthEmployeeMap.computeIfAbsent(monthKey, k -> new HashMap<>());
        Employee employee = employeeMap.computeIfAbsent(id, k -> new Employee());
        employee.id = id;
        employee.names = names;


        DoorEvent doorEvent = new DoorEvent(date, doorName, eventPointName);

//        Employee employee  = null;
//        if(monthEmployeeMap.containsKey(id)){
//          employee = monthEmployeeMap.get(id);
//        } else {
//          employee = new Employee();
//          employee.id = id;
//          employee.names = names;
//          monthEmployeeMap.put(employee.id, employee);
//        }

        employee.doorEvents.add(doorEvent);

      }

      logger.info(String.format("Parsed data for %s months\n", String.join(",", monthEmployeeMap.keySet())));
      logger.info(String.format("Parsed data for %d  employees\n", monthEmployeeMap.values().stream().mapToInt(it -> it.keySet().size()).sum()));
      logger.info("Summary of the excluded rows:");

      logger.info(String.format("--- rows with unknown person (empty id or name): %s\n", rowsWrongPerson.stream().map(Object::toString).collect(Collectors.joining(", "))));
      logger.info(String.format("--- rows with unknown eventPointName type (not ending on '-IN' or '-OUT'): : %s\n", rowsWrongEventPoints.stream().map(it ->{ return it.toString();}).collect(Collectors.joining(", "))));
      logger.info(String.format("--- rows with ignored openDoorTypes [%s]: %s\n",
          ignoredDoorOpenTypes.stream().map(it ->{ return it.toString();}).collect(Collectors.joining(", ")),
          rowsIgnoredDoorOpenTypes.stream().map(it ->{ return it.toString();}).collect(Collectors.joining(", "))
      ));


    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      workbook.close();
      file.close();
    }

    return monthEmployeeMap;
  }

  public void calculateWorkedHours(Map<String, Map<String, Employee>> monthEmployeeMap, Boolean firstLast) throws ParseException {
    logger.info("Start processing worked hours... ");

    List<Map<String, Employee>> employeeMapList = monthEmployeeMap.values().stream().toList();
    for (Map<String, Employee> map : employeeMapList) {
      List<Employee> employeeList = map.values().stream().toList();
      for (Employee employee : employeeList) {
        calculateWorkedHoursForEmployee(employee, firstLast);
      }
    }

    logger.info("End processing worked hours... ");
  }

  private void calculateWorkedHoursForEmployee(Employee employee, Boolean firstLast) throws ParseException {
    List<DoorEvent> doorEvents = employee.doorEvents;
    doorEvents.sort(Comparator.comparing(de -> de.timestamp));
//    logger.info("Calculating worked hours (firstLast[" + firstLast + "]) for employee: " + employee.id);

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

    //logger.info("DoorEvents per date: " + doorEventsPerDate);

    for (Map.Entry<String, List<DoorEvent>> dateDoorEvents : doorEventsPerDate.entrySet()) {
      String dateStr = dateDoorEvents.getKey();
      calculateWorkedHoursForEmployAndDate(employee, dateStr, dateDoorEvents.getValue(), firstLast);

      /*
      Duration duration = Duration.ofSeconds(employee.workedHoursPerDate.get(dateStr));
      logger.info(String.format("Employee %s -> date %s -> Time spent on work: %dh %dm %ds\n-------------------\n",
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
        while (outEvent == null && i <  doorEvents.size() - 1) {
          DoorEvent nextEvent = doorEvents.get(++i);
          if (!nextEvent.isInEvent) {
            outEvent = nextEvent;
          } else {
            logger.info("Unexpected inEvent[" + nextEvent + "] after another inEvent[" + inEvent + "]");
          }
        }

        timeToAdd = calculateWorkDuration(inEvent.timestamp, outEvent != null ? outEvent.timestamp : null);
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
      logger.info("Incomplete door events. Cannot calculate worked hours.");
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
  private String generateReportFile(String srcFile, Map<String, Map<String, Employee>> monthEmployeeMap, Boolean firstLast) throws IOException {

    String outputFileName = getOutputFileName(srcFile, firstLast);
    logger.info("Start exporting data in xls file: " + outputFileName);

    Form76XlsxReportBuilder form76XlsxReportBuilder = new Form76XlsxReportBuilder();
    form76XlsxReportBuilder.setEmployeesData(monthEmployeeMap);
    FileOutputStream generatedReportFile = form76XlsxReportBuilder.build().asFileOutputStream(outputFileName);
    generatedReportFile.flush();
    generatedReportFile.close();

    logger.info("Report exported successfully.");

    return outputFileName;
  }

  private String getOutputFileName(String srcFile, Boolean firstLast) {
    String outputFileName = null;

    int extDotIndex = srcFile.lastIndexOf(".");

    String srcFileNameNoExt = extDotIndex != -1 ? srcFile.substring(0, extDotIndex) : srcFile;

    if (outputFileNameFormat != null) {
      outputFileName = new SimpleDateFormat(outputFileNameFormat.replace("srcFileName_", srcFileNameNoExt + "_").replace("FL_", (firstLast ? "FL_" : ""))).format(new Date());
    } else {
      outputFileName = "Report_Forma76_" + (firstLast ? "FL_" : "") + REPORT_FILE_NAME_TIMESTAMPS_FORMAT.format(new Date());
    }
    return outputFileName;
  }
}