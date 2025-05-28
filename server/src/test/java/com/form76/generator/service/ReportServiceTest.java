package com.form76.generator.service;

import com.form76.generator.db.entity.Administration;
import com.form76.generator.db.entity.Location;
import com.form76.generator.db.entity.Report;
import com.form76.generator.db.entity.Role;
import com.form76.generator.db.repository.LocationRepository;
import com.form76.generator.db.repository.ReportRepository;
import com.form76.generator.rest.model.ReportData;
import com.form76.generator.rest.model.ReportDownloadResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

  private ReportRepository reportRepository;
  private UploadReportService uploadReportService;
  private LocationRepository locationRepository;
  private ReportService reportService;
  private Authentication authentication;

  @BeforeEach
  void setup() {
    reportRepository = mock(ReportRepository.class);
    uploadReportService = mock(UploadReportService.class);
    locationRepository = mock(LocationRepository.class);
    authentication = mock(Authentication.class);

    reportService = new ReportService();
    reportService.reportRepository = reportRepository;
    reportService.uploadReportService = uploadReportService;
    reportService.locationRepository = locationRepository;
  }

  @Test
  void testSaveReport() {
    ReportData reportData = createReportData();
    Report reportEntity = createReportEntity();

    when(locationRepository.findById(reportData.getLocationId())).thenReturn(Optional.of(reportEntity.getLocation()));
    when(reportRepository.save(any(Report.class))).thenReturn(reportEntity);

    ReportData result = reportService.saveReport(reportData);

    assertEquals(reportData.getId(), result.getId());
    verify(reportRepository).save(any(Report.class));
  }

  @Test
  void testListReportsAsAdmin() {
    Report report = createReportEntity();
    when(reportRepository.findAll()).thenReturn(List.of(report));

    when(authentication.getPrincipal()).thenReturn(new User("userName", "pwd", List.of(new SimpleGrantedAuthority(Role.ADMIN.name()))));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    List<ReportData> result = reportService.listReports();

    assertEquals(1, result.size());
    assertEquals("r1", result.get(0).getId());
  }

  @Test
  void testListReportsForLocation() {
    Report report = createReportEntity();
    when(reportRepository.findReportsByLocationId("loc1")).thenReturn(List.of(report));

    List<ReportData> result = reportService.listReportsForLocation("loc1");

    assertEquals(1, result.size());
    assertEquals("r1", result.get(0).getId());
  }

  @Test
  void testFindReportById_Found() {
    Report report = createReportEntity();
    when(reportRepository.findById("r1")).thenReturn(Optional.of(report));

    ReportData result = reportService.findReportById("r1");

    assertNotNull(result);
    assertEquals("r1", result.getId());
  }

  @Test
  void testFindReportById_NotFound() {
    when(reportRepository.findById("bad")).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () -> reportService.findReportById("bad"));
  }

  @Test
  void testDownloadReportFile_Success() throws IOException {
    //given
    Report report = createReportEntity();
    when(reportRepository.findById("r1")).thenReturn(Optional.of(report));

    ReportDownloadResponse response = new ReportDownloadResponse("test.xlsx", "test".getBytes());
    when(uploadReportService.downloadFile(eq(report.getFileName()))).thenReturn(response);

    //when
    ReportDownloadResponse result = reportService.downloadReportFile("r1");

    //then
    assertNotNull(result);
    verify(uploadReportService).downloadFile(eq(report.getFileName()));
  }

  @Test
  void testDownloadReportFile_NotFound() {
    when(reportRepository.findById("bad")).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () -> reportService.downloadReportFile("bad"));
  }

  private Report createReportEntity() {
    Administration admin = new Administration();
    admin.setName("Admin");

    Location loc = new Location();
    loc.setId("loc1");
    loc.setName("Location");
    loc.setAdministration(admin);

    Report report = new Report();
    report.setId("r1");
    report.setFileName("file.pdf");
    report.setCloudStorageReference("cloudref");
    report.setCreationDate(LocalDateTime.now());
    report.setReportPeriodStartDateTime(LocalDateTime.now());
    report.setReportPeriodEndDateTime(LocalDateTime.now());
    report.setLocation(loc);

    return report;
  }

  private ReportData createReportData() {
    return new ReportData(
        "r1",
        "file.pdf",
        LocalDateTime.now(),
        "cloudref",
        LocalDateTime.now(),
        LocalDateTime.now(),
        "loc1",
        "Location",
        "Admin"
    );
  }
}
