package com.form76.generator.db.repository;

import com.form76.generator.db.entity.Administration;
import com.form76.generator.db.entity.Location;
import com.form76.generator.db.entity.Report;
import com.form76.generator.db.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class AdministrationRepositoryTest {

  @Autowired
  private AdministrationRepository administrationRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private ReportRepository reportRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testSaveAndFindAdministration() {
    Location location = new Location();
    location.setName("Test Location");
    location = locationRepository.save(location);

    Report report = new Report();
    report.setFileName("report.xlsx");
    report.setCreationDate(LocalDateTime.now());
    report.setCloudStorageReference("cloud-reference");
    report = reportRepository.save(report);

    User user = new User();
    user.setName("misho");
    user.setPassword("test");
    user.setEmail("mishom@mail.com");
    user = userRepository.save(user);

    Administration administration = new Administration();
    administration.setLocation(location);
    administration.setReport(report);
    administration.setUser(user);

    Administration savedAdministration = administrationRepository.save(administration);

    Administration foundAdministration = administrationRepository.findById(savedAdministration.getId()).orElse(null);

    assert savedAdministration.getId() != null;
    assert foundAdministration != null;
    assert foundAdministration.getLocation().getId().equals(location.getId());
    assert foundAdministration.getReport().getId().equals(report.getId());
    assert foundAdministration.getUser().getId().equals(user.getId());
  }
}
