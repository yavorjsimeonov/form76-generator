package com.form76.generator.db.repository;

import com.form76.generator.db.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testAddUser() {
    User user = new User();
    user.setName("misho");
    user.setPassword("test");
    user.setEmail("mishom@mail.com");

    User savedUser = userRepository.save(user);

    User foundUser = userRepository.findById(savedUser.getId()).orElse(null);

    assert foundUser != null;
    assert foundUser.getName().equals("misho");
    assert foundUser.getEmail().equals("mishom@mail.com");


  }

}
