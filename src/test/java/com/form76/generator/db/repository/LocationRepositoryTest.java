package com.form76.generator.db.repository;

import com.form76.generator.db.entity.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LocationRepositoryTest {

  @Autowired
  private LocationRepository locationRepository;

  @Test
  public void testSaveAndFindLocation() {
    Location location = new Location();
    location.setName("Test Location");

    Location savedLocation = locationRepository.save(location);

    Location foundLocation = locationRepository.findById(savedLocation.getId()).orElse(null);

    assert savedLocation.getId() != null;
    assert foundLocation != null;
    assert foundLocation.getName().equals("Test Location");
  }

}
