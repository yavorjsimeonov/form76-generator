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

import java.util.List;

@Testcontainers
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class DeviceRepositoryTest {

  @Autowired
  private AdministrationRepository administrationRepository;

  @Autowired
  private DeviceRepository deviceRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Test
  public void testSaveAndFindDevice() {
    Administration administration = new Administration();
    administration.setName("Central Administration");
    administration.setActive(true);
    administrationRepository.save(administration);


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
    location.setAdministration(administration);
    locationRepository.save(location);

    Device device = new Device();
    device.setName("Front Door Scanner");
    device.setExternalId("123");
    device.setType(DeviceType.IN);
    device.setActive(true);
    device.setLocation(location);
    deviceRepository.save(device);

    List<Device> found = deviceRepository.findAll();
    assert found.size() > 0;
    assert found.get(0).getName().equals("Front Door Scanner");
  }
}
