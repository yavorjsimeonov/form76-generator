package com.form76.generator;

import com.form76.generator.model.Employee;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import static com.form76.generator.TestDataGenerator.SIMPLE_DATE_FORMAT_FOR_FILE_NAME;

public class Form76ReportGeneratorTest {

  @Test
  public void testCalculateWorkedHours() throws IOException, ParseException {
    Map<String, Employee> employees = TestDataGenerator.generateEmployees(10, 7);
    System.out.println(employees);

    String fileName = String.format("/users/maya/Downloads/Report-forma76-%s.xlsx", SIMPLE_DATE_FORMAT_FOR_FILE_NAME.format(new Date()));
    Form76XlsxReportBuilder form76XlsxReportBuilder = new Form76XlsxReportBuilder();
    form76XlsxReportBuilder.setEmployeesData(employees);
    form76XlsxReportBuilder.build().asFileOutputStream(fileName);

  }

  @Test
  public void testReportGenerationFromFile() throws Exception {
    String srcFileName = String.format("/users/maya/Downloads/test-door-events-%s.xlsx", SIMPLE_DATE_FORMAT_FOR_FILE_NAME.format(new Date()));
    TestDataGenerator.createDoorEventsSourceFile(2, 20, srcFileName);

    Form76ReportGenerator form76ReportGenerator = new Form76ReportGenerator();
    form76ReportGenerator.generateReportFromSource(srcFileName);
  }
}
