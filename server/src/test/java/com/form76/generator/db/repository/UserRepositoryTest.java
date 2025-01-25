package com.form76.generator.db.repository;

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

@Testcontainers
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testAddUser() {
    User user = new User();
    user.setFirstName("Misho");
    user.setLastName("Mishov");
    user.setPassword("test");
    user.setEmail("mishom@mail.com");
    user.setActive(true);

    User savedUser = userRepository.save(user);

    User foundUser = userRepository.findById(savedUser.getId()).orElse(null);

    assert foundUser != null;
    assert foundUser.getFirstName().equals("Misho");
    assert foundUser.getLastName().equals("Mishov");
    assert foundUser.getEmail().equals("mishom@mail.com");


  }

}
