package com.form76.generator.rest;

import com.form76.generator.rest.model.ReportData;
import com.form76.generator.rest.model.ReportDownloadResponse;
import com.form76.generator.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/reports")
public class ReportResource {

  private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  @Autowired
  ReportService reportService;

  @GetMapping("")
  public List<ReportData> listReports() {
    return null;
  }


  @PostMapping(value = "/{id}/download", produces = CONTENT_TYPE)
  public ResponseEntity<byte[]> downloadReportFile(@PathVariable("id") String reportId) throws IOException {
    ReportDownloadResponse downloadResponse = reportService.downloadReportFile(reportId);

    return ResponseEntity.ok()
        .contentLength(downloadResponse.content().length)
        .header(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + downloadResponse.fileName())
        .body(downloadResponse.content());
  }

}
