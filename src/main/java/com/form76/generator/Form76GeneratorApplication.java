package com.form76.generator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.text.ParseException;
import java.util.Date;

import static com.form76.generator.TestDataGenerator.SIMPLE_DATE_FORMAT_FOR_FILE_NAME;

@SpringBootApplication
@RestController
public class Form76GeneratorApplication {

  private final static Logger logger = LoggerFactory.getLogger(Form76GeneratorApplication.class.getName());

  private static String tmpdir = System.getProperty("java.io.tmpdir");
  private static String fileSeparator = FileSystems.getDefault().getSeparator();

  public static void main(String[] args) {
    SpringApplication.run(Form76GeneratorApplication.class, args);
  }

  @GetMapping("/ping")
  @ResponseBody
  public String ping(@RequestParam(value = "myName", defaultValue = "World") String name) {
    return String.format("Hello %s!", name);
  }

  @GetMapping(path = "/testsrcfile/generate", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<Resource> generateTestSrcFile(@PathParam(value = "month") int month, @PathParam(value = "employeesCount") int employeesCount) throws IOException, ParseException {
    System.out.println(String.format("Going to generate test file for month[%d] and employeesCount[%d]", month, employeesCount));

    String error = "";
    try {


      tmpdir = tmpdir.endsWith(fileSeparator) ? tmpdir : tmpdir + fileSeparator;
      String srcFileName = String.format(tmpdir + "test-door-events-%s.xlsx", SIMPLE_DATE_FORMAT_FOR_FILE_NAME.format(new Date()));
      TestDataGenerator.createDoorEventsSourceFile(month, employeesCount, srcFileName);

      Resource resource = new FileSystemResource(srcFileName);

      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+srcFileName+"\"");

      return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

  }

  @PostMapping(path = "/report/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<Resource> generateReport(@RequestParam("srcFile") MultipartFile file) throws Exception {
    System.out.println(String.format("Going to generate Form 76 report file for srcFile[%s]", file.getName()));

    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("File not uploaded successfully");
    }

    System.out.println(String.format("Received file %s", file.getOriginalFilename()));

    String error = "";
    try {
      tmpdir = tmpdir.endsWith(fileSeparator) ? tmpdir : tmpdir + fileSeparator;
      String receivedFile = tmpdir + file.getOriginalFilename();
      file.transferTo(new File(receivedFile));

      System.out.println(String.format("Start processing file %s!", receivedFile));

      Form76ReportGenerator form76ReportGenerator = new Form76ReportGenerator();
      String generatedFileName = form76ReportGenerator.generateReportFromSource(receivedFile);

      Resource resource = new FileSystemResource(generatedFileName);

      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+generatedFileName+"\"");
      System.out.println("Ready to return the generated file");

      return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
}
