package com.form76.generator.db.entity;


import com.form76.generator.db.IdGenerator;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
@Getter
@Setter
public class User {

  @Id
  @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
  @GeneratedValue(generator = "idGenerator")
  private String id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private Role role;

  @OneToOne
  private Administration administration;

  @Column(name = "active", nullable = false)
  private boolean active;

}
