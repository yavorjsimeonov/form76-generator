package com.form76.generator.service;

import com.form76.generator.db.entity.Report;
import com.form76.generator.db.repository.LocationRepository;
import com.form76.generator.db.repository.ReportRepository;
import com.form76.generator.rest.model.ReportData;
import com.form76.generator.rest.model.ReportDownloadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ReportService {

  @Autowired
  ReportRepository reportRepository;

  @Autowired
  UploadReportService uploadReportService;

  @Autowired
  LocationRepository locationRepository;


  public ReportData saveReport(ReportData reportData) {
    return convertToReportData(reportRepository.save(convertToReport(reportData)));
  }

  public List<ReportData> listReports() {
    return reportRepository.findAll().stream().map(this::convertToReportData).toList();
  }

  public List<ReportData> listReportsForLocation(String locationId) {
    return reportRepository.findReportsByByLocationId(locationId).stream().map(this::convertToReportData).toList();
  }

  public ReportData findReportById(String reportId) {
    Report report = reportRepository.findById(reportId).orElseThrow(() -> new IllegalArgumentException("Failed to get report for id: " + reportId));

    return convertToReportData(report);
  }


  public ReportDownloadResponse downloadReportFile(String reportId) throws IOException {
    Report report = reportRepository.findById(reportId).orElseThrow(() -> new IllegalArgumentException("Failed to get report for id: " + reportId));

    return uploadReportService.downloadFile(report.getFileName());

  }

  private ReportData convertToReportData(Report report) {
    return new ReportData(
        report.getId(),
        report.getFileName(),
        report.getCreationDate(),
        report.getCloudStorageReference(),
        report.getReportPeriodStartDateTime(),
        report.getReportPeriodEndDateTime(),
        report.getLocation().getId(),
        report.getLocation().getName(),
        report.getLocation().getAdministration().getName()
    );
  }
  private Report convertToReport(ReportData reportData) {
    Report report = new Report();
    report.setId(reportData.getId());
    report.setFileName(reportData.getFileName());
    report.setCreationDate(reportData.getCreationDate());
    report.setCloudStorageReference(reportData.getCloudStorageReference());
    report.setReportPeriodStartDateTime(reportData.getReportPeriodStartDateTime());
    report.setReportPeriodEndDateTime(reportData.getReportPeriodEndDateTime());
    report.setLocation(locationRepository.findById(reportData.getLocationId()).orElse(null));
    return report;
  }
}
