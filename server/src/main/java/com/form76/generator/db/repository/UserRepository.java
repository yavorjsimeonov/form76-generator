package com.form76.generator.db.repository;

import com.form76.generator.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, String> {
  User findByUsernameAndPassword(String username, String password);
}
