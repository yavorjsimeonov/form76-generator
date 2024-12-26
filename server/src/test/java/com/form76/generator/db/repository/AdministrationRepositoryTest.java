package com.form76.generator.db.repository;

import com.form76.generator.db.entity.Administration;
import com.form76.generator.db.entity.Location;
import com.form76.generator.db.entity.Report;
import com.form76.generator.db.entity.User;
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

    Administration administration = new Administration();
    administration.name = "Bank";
    administration.active = true;

    Administration savedAdministration = administrationRepository.save(administration);

    Administration foundAdministration = administrationRepository.findById(savedAdministration.id).orElse(null);

    assert savedAdministration.id != null;
    assert foundAdministration != null;
    assert foundAdministration.name.equals("Bank");
    assert foundAdministration.active;
  }
}
