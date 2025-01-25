package com.form76.generator.db.repository;

import com.form76.generator.db.entity.Report;
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

@Testcontainers
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ReportRepositoryTest {

  @Autowired
  private ReportRepository reportRepository;

  @Test
  public void testAddReport() {
    Report report = new Report();
    report.setFileName("report.xlsx");
    report.setCreationDate(LocalDateTime.now());
    report.setCloudStorageReference("cloud-reference");

    Report savedReport = reportRepository.save(report);

    Report foundReport = reportRepository.findById(savedReport.getId()).orElse(null);

    assert savedReport.getId() != null;
    assert foundReport != null;
    assert foundReport.getFileName().equals("report.xlsx");
    assert foundReport.getCloudStorageReference().equals("cloud-reference");
  }
}
