package com.form76.generator.db.repository;

import com.form76.generator.db.entity.Administration;
import com.form76.generator.db.entity.Role;
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

import java.util.List;
import java.util.Optional;

@Testcontainers
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserDataRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AdministrationRepository administrationRepository;

  @Test
  void testSaveAndFindById() {
    Administration administration = new Administration();
    administration.setName("Central Administration");
    administration.setActive(true);
    administrationRepository.save(administration);

    User user = new User();
    user.setFirstName("John");
    user.setLastName("Doe");
    user.setEmail("john.doe@example.com");
    user.setUsername("johndoe");
    user.setPassword("securepassword");
    user.setRole(Role.ADMIN);
    user.setActive(true);
    user.setAdministration(administration);
    userRepository.save(user);

    List<User> found = userRepository.findAll();
    assert found.size() > 0;
    assert (found.get(0).getUsername()).equals("johndoe");
  }

}
