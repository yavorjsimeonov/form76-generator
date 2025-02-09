package com.form76.generator.service;

import com.form76.generator.db.entity.ReportAlgorithm;
import com.form76.generator.db.entity.ReportFileFormat;
import com.form76.generator.service.model.DoorEvent;
import com.form76.generator.service.model.DoorOpeningLog;
import com.form76.generator.service.model.Employee;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;


public class Form76ReportGeneratorTest {

  Logger logger = LoggerFactory.getLogger(Form76ReportGeneratorTest.class);

  @Test
  public void testCalculateWorkedHours() throws IOException, ParseException {
    TestDataGenerator testDataGenerator = new TestDataGenerator();
    Form76ReportService form76ReportService = new Form76ReportService();
    DoorOpeningLog doorOpeningLog = testDataGenerator.generateDoorOpeningLog("2024-11-01 00:00:00", "2024-12-31 23:59:59");
    logger.info("Employees: " + doorOpeningLog.getList());


    Map<String, Map<String, Employee>> monthEmployeeMap = new HashMap<>();
    List<DoorEvent> allEvents = doorOpeningLog.getList();

    for (DoorEvent event : allEvents) {
      String yearMonthKey = DateHelper.getYearAndMonthFromDateString(event.getEventTime());
      Map<String, Employee> employeesMap = monthEmployeeMap.computeIfAbsent(yearMonthKey, k -> new HashMap<>());
      Employee employee = employeesMap.computeIfAbsent(Integer.toString(event.getEmpId()), k -> new Employee(event.getEmpId(), event.getEmpName()));

      employee.getDoorEvents().add(event);
    }
    boolean firstLast = true;
    String fileFormat = "XLSX";

    form76ReportService.calculateWorkedHours("aaa", "cccc", monthEmployeeMap, firstLast);

    String generatedFileName = form76ReportService.generateReportFile("cccc", monthEmployeeMap, firstLast, fileFormat);



    String fileName = String.format("/users/maya/Downloads/Report-forma76-%s.xlsx", DateHelper.formatReportDate(new Date()));
    Form76XlsxReportBuilder form76XlsxReportBuilder = new Form76XlsxReportBuilder();
    //form76XlsxReportBuilder.setEmployeesData(employees);
    form76XlsxReportBuilder.build(ReportFileFormat.XLSX.toString()).asFileOutputStream(fileName);

  }

//  @Test
//  public void testReportGenerationFromFile() throws Exception {
//    String srcFileName = String.format("/users/maya/Downloads/1708687718209_733556.xls", SIMPLE_DATE_FORMAT_FOR_FILE_NAME.format(new Date()));
//    //TestDataGenerator.createDoorEventsSourceFile(2, 20, srcFileName);
//
//    srcFileName = "/users/maya/Downloads/1709287584082_753799-1.xlsx";
//    Form76ReportService form76ReportService = new Form76ReportService();
//    //form76ReportService.generateReportFromSource(srcFileName, false);
//  }
}
