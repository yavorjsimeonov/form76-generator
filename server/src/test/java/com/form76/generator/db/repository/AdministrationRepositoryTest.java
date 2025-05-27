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
import java.util.List;
import java.util.Optional;

@Testcontainers
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AdministrationRepositoryTest {

  @Autowired
  private AdministrationRepository administrationRepository;

  @Test
  void testSaveAndFindById() {
    Administration admin = new Administration();
    admin.setName("Central Administration");
    admin.setActive(true);
    administrationRepository.save(admin);

    List<Administration> found = administrationRepository.findAll();
    assert found.size() > 0;
    assert found.get(0).getName().equals("Central Administration");
    assert found.get(0).isActive();
  }
}
