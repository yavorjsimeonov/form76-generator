package com.form76.generator.resource;

import com.form76.generator.Form76ReportGenerator;
import com.form76.generator.TestDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.text.ParseException;
import java.util.Date;

import static com.form76.generator.TestDataGenerator.SIMPLE_DATE_FORMAT_FOR_FILE_NAME;

@Controller
@RequestMapping("/form76")
public class Form76GeneratorResource {

  private static String TMP_DIR = System.getProperty("java.io.tmpdir");
  private static String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

  @Autowired
  Form76ReportGenerator form76ReportGenerator;

  @GetMapping("/ping")
  @ResponseBody
  public String ping() {
    return "Ping successful...";
  }

  @GetMapping(path = "/generate")
  public String generatorFor(Model model)  {
    return "generator";
  }

  /**
   *
   * @param months commaseparated list of month numbers, e.g. 1, 2, 3 for Jan, Feb, Mar
   * @param employeesCount how many employees
   * @return
   * @throws IOException
   * @throws ParseException
   */
  @GetMapping(path = "/generate/test", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<Resource> generateTestSrcFile(@RequestParam(value = "months") String months, @RequestParam(value = "employeesCount") int employeesCount) throws IOException, ParseException {

    String error = "";
    try {
      System.out.println(String.format("Going to generate test file for months[%s] and employeesCount[%d]", months, employeesCount));


      TMP_DIR = TMP_DIR.endsWith(FILE_SEPARATOR) ? TMP_DIR : TMP_DIR + FILE_SEPARATOR;
      String srcFileName = String.format(TMP_DIR + "test-door-events-%s.xlsx", SIMPLE_DATE_FORMAT_FOR_FILE_NAME.format(new Date()));
      TestDataGenerator.createDoorEventsSourceFile(months, employeesCount, srcFileName);

      Resource resource = new FileSystemResource(srcFileName);

      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+srcFileName+"\"");

      return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalArgumentException("File generate test file: " + e.getMessage());
    }

  }

  @PostMapping(path = "/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<Resource> generateReport(@RequestParam("srcFile") MultipartFile file, @RequestParam(name = "firstLast", required = false) Boolean firstLast, Model model) throws Exception {

    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("File not uploaded successfully");
    }

    System.out.println(String.format("Going to generate Form 76 report file for srcFile[%s] and firstLast[%s]", file.getOriginalFilename(), firstLast));

    String error = "";
    try {
      TMP_DIR = TMP_DIR.endsWith(FILE_SEPARATOR) ? TMP_DIR : TMP_DIR + FILE_SEPARATOR;
      String receivedFile = TMP_DIR + file.getOriginalFilename();
      file.transferTo(new File(receivedFile));


      String generatedFileName = form76ReportGenerator.generateReportFromSource(receivedFile, firstLast != null ? firstLast : false);
      String attachmentFileName = generatedFileName.lastIndexOf(FILE_SEPARATOR) == -1 && generatedFileName.lastIndexOf(FILE_SEPARATOR) < generatedFileName.length() -1 ?
          generatedFileName :
          generatedFileName.substring(generatedFileName.lastIndexOf(FILE_SEPARATOR) + 1);

      System.out.println(String.format("Ready to return the generated file %s as attachment %s", generatedFileName, attachmentFileName));

      Resource resource = new FileSystemResource(generatedFileName);
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ attachmentFileName +"\"");
      return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Exception occurred: " + e.getMessage());
    }
  }


}
