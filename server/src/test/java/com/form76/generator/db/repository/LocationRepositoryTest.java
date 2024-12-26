package com.form76.generator.db.repository;

import com.form76.generator.db.entity.Administration;
import com.form76.generator.db.entity.Location;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

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
  public void testSaveAndFindLocation() {
    Administration administration = new Administration();
    administration.name = "Bank";
    administration.active = true;

    Administration savedAdministration = administrationRepository.save(administration);


    Location location = new Location();
    location.name = "Test Location";
    location.administration = savedAdministration;

    Location savedLocation = locationRepository.save(location);

    Location foundLocation = locationRepository.findById(savedLocation.id).orElse(null);

    assert savedLocation.id != null;
    assert foundLocation != null;
    assert foundLocation.name.equals("Test Location");
  }

}
