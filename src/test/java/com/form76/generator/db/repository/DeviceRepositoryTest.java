package com.form76.generator.db.repository;

import com.form76.generator.db.entity.Device;
import com.form76.generator.db.entity.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
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
    device.setType(Device.DeviceType.IN);
    device.setName("Test Device");
    device.setLocation(location);

    Device savedDevice = deviceRepository.save(device);

    Device foundDevice = deviceRepository.findById(savedDevice.getId()).orElse(null);

    assert savedDevice.getId() != null;
    assert foundDevice != null;
    assert foundDevice.getName().equals("Test Device");
    assert foundDevice.getType() == Device.DeviceType.IN;
    assert foundDevice.getLocation().getId().equals(location.getId());
  }
}
