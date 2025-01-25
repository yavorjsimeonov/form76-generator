package com.form76.generator.db.repository;

import com.form76.generator.db.entity.Device;
import com.form76.generator.db.entity.DeviceType;
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
public class DeviceRepositoryTest {

  @Autowired
  private DeviceRepository deviceRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Test
  public void testSaveAndFindDevice() {
    Location location = new Location();
    location.setName("Test Location");
    location = locationRepository.save(location);

    Device device = new Device();
    device.setType(DeviceType.IN);
    device.setName("Test Device");
    device.setLocation(location);

    Device savedDevice = deviceRepository.save(device);

    Device foundDevice = deviceRepository.findById(savedDevice.getId()).orElse(null);

    assert savedDevice.getId() != null;
    assert foundDevice != null;
    assert foundDevice.getName().equals("Test Device");
    assert foundDevice.getType().equals(DeviceType.IN);
  }
}
