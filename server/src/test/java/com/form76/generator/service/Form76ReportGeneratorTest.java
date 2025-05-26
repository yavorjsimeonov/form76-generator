package com.form76.generator.service;

import com.form76.generator.db.entity.ReportAlgorithm;
import com.form76.generator.db.entity.ReportFileFormat;
import com.form76.generator.service.model.DoorEvent;
import com.form76.generator.service.model.DoorOpeningLog;
import com.form76.generator.service.model.Employee;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.form76.generator.service.Form76ReportService.TMP_DIR;


@SpringBootTest
public class Form76ReportGeneratorTest {

  Logger logger = LoggerFactory.getLogger(Form76ReportGeneratorTest.class);

  @Autowired
  TestDataGenerator testDataGenerator;

  @Autowired
  Form76ReportService form76ReportService;

  @Value("${form76-generator.output.file.name.prefix}")
  String reportFilePrefix;

  @Test
  @Ignore
  public void testCalculateWorkedHours() throws IOException, ParseException {

    DoorOpeningLog doorOpeningLog = testDataGenerator.generateDoorOpeningLog("2024-11-01 00:00:00", "2024-12-31 23:59:59");

    Map<String, Map<String, Employee>> monthEmployeeMap = new HashMap<>();
    List<DoorEvent> allEvents = doorOpeningLog.getList();

    for (DoorEvent event : allEvents) {
      String yearMonthKey = DateHelper.getYearAndMonthFromDateString(event.getEventTime());
      Map<String, Employee> employeesMap = monthEmployeeMap.computeIfAbsent(yearMonthKey, k -> new HashMap<>());
      Employee employee = employeesMap.computeIfAbsent(Integer.toString(event.getEmpId()), k -> new Employee(event.getEmpId(), event.getEmpName()));

      employee.getDoorEvents().add(event);
    }

    // assert that worked hours are not populated for the generated test data
    Collection<Map<String, Employee>> employees = monthEmployeeMap.values();
    for (Map<String, Employee> employeeDates : employees) {
      assert employeeDates.values().stream().allMatch(e -> e.getWorkedHoursPerDate().isEmpty());
    }

    String locationName = "Test Location";
    String locationUuid = "1234qwert";
    String administrationName = "Test Administration";
    boolean firstLast = true;
    String fileFormat = "XLSX";

    form76ReportService.calculateWorkedHours(locationName, locationUuid, monthEmployeeMap, firstLast);

    // assert that worked hours are populated for all employees
    employees = monthEmployeeMap.values();
    for (Map<String, Employee> employeeDates : employees) {
      assert employeeDates.values().stream().noneMatch(e -> e.getWorkedHoursPerDate().isEmpty());
    }

    String generatedFileName = form76ReportService.generateReportFile(administrationName, locationName, locationUuid, monthEmployeeMap, firstLast, fileFormat);
    assert generatedFileName.startsWith(reportFilePrefix + locationUuid + "_FL_");
    assert generatedFileName.endsWith(".xlsx");

    Form76XlsxReportBuilder form76XlsxReportBuilder = new Form76XlsxReportBuilder();
    form76XlsxReportBuilder.setEmployeesData(monthEmployeeMap);

    FileOutputStream fileOutputStream = null;
    try {
      fileOutputStream = form76XlsxReportBuilder.build(ReportFileFormat.XLSX.toString(), "Test Administration", "Test location").asFileOutputStream(generatedFileName);
      assert fileOutputStream != null;
    } finally {
      File outputFile = new File(TMP_DIR + generatedFileName);
      outputFile.delete();
    }

  }

}
