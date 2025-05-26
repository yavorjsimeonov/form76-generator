package com.form76.generator.db.repository;

import com.form76.generator.db.entity.Administration;
import com.form76.generator.db.entity.Location;
import com.form76.generator.db.entity.ReportAlgorithm;
import com.form76.generator.db.entity.ReportFileFormat;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

@Testcontainers
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class LocationRepositoryTest {

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private AdministrationRepository administrationRepository;

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

    List<Location> found = locationRepository.findAll();
    assert found.size() > 0;
    assert (found.get(0).getName()).equals("Main Building");
  }

}
