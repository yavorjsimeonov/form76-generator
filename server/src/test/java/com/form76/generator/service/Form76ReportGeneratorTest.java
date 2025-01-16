package com.form76.generator.service;

import com.form76.generator.service.model.Employee;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static com.form76.generator.service.TestDataGenerator.SIMPLE_DATE_FORMAT_FOR_FILE_NAME;

public class Form76ReportGeneratorTest {

  Logger logger = LoggerFactory.getLogger(Form76ReportGeneratorTest.class);

  @Test
  public void testCalculateWorkedHours() throws IOException, ParseException {
    Map<String, Map<String, Employee>> employees = TestDataGenerator.generateEmployees(Arrays.asList(7, 8), 3);
    logger.info("Employees: " + employees);

    String fileName = String.format("/users/maya/Downloads/Report-forma76-%s.xlsx", SIMPLE_DATE_FORMAT_FOR_FILE_NAME.format(new Date()));
    Form76XlsxReportBuilder form76XlsxReportBuilder = new Form76XlsxReportBuilder();
    form76XlsxReportBuilder.setEmployeesData(employees);
    form76XlsxReportBuilder.build(); //.asFileOutputStream(fileName);

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
