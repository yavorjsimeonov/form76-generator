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
    user.firstName = "Misho";
    user.lastName = "Mishov";
    user.password = "test";
    user.email = "mishom@mail.com";
    user.active = true;

    User savedUser = userRepository.save(user);

    User foundUser = userRepository.findById(savedUser.id).orElse(null);

    assert foundUser != null;
    assert foundUser.firstName.equals("Misho");
    assert foundUser.lastName.equals("Mishov");
    assert foundUser.email.equals("mishom@mail.com");


  }

}
