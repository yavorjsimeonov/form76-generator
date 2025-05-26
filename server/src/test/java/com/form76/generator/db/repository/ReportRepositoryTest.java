package com.form76.generator.db.repository;

import com.form76.generator.db.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Testcontainers
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ReportRepositoryTest {

  @Autowired
  private AdministrationRepository administrationRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private ReportRepository reportRepository;

  @Test
  void testSaveAndFindById() {
    Administration admin = new Administration();
    admin.setName("Central Administration");
    admin.setActive(true);
    administrationRepository.save(admin);

    Location location = new Location();
    location.setName("Main Building");
    location.setExtCommunityId(12345);
    location.setExtCommunityUuid("uuid-1234");
    location.setRepresentativeName("Alice Smith");
    location.setRepresentativeEmail("alice@example.com");
    location.setReportAlgorithm(ReportAlgorithm.FIRST_IN_LAST_OUT);
    location.setFileFormat(ReportFileFormat.XLSX);
    location.setActive(true);
    location.setSendEmail(true);
    location.setAdministration(admin);
    locationRepository.save(location);

    Report report = new Report();
    report.setFileName("report_May.xlsx");
    report.setCreationDate(LocalDateTime.now());
    report.setCloudStorageReference("cloud://storage/report_May.xlsx");
    report.setReportPeriodStartDateTime(LocalDateTime.of(2025, 5, 1, 0, 0, 0));
    report.setReportPeriodEndDateTime(LocalDateTime.of(2025, 5, 31, 23, 59, 59));
    report.setLocation(location);
    reportRepository.save(report);

    List<Report> found = reportRepository.findAll();
    assert found.size() > 0;
    assert (found.get(0).getFileName()).equals("report_May.xlsx");
  }
}
