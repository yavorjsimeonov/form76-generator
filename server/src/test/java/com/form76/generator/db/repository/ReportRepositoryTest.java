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
    report.fileName = "report.xlsx";
    report.creationDate = LocalDateTime.now();
    report.cloudStorageReference = "cloud-reference";

    Report savedReport = reportRepository.save(report);

    Report foundReport = reportRepository.findById(savedReport.id).orElse(null);

    assert savedReport.id != null;
    assert foundReport != null;
    assert foundReport.fileName.equals("report.xlsx");
    assert foundReport.cloudStorageReference.equals("cloud-reference");
  }
}
