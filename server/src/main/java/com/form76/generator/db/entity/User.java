package com.form76.generator.db.entity;


import com.form76.generator.db.IdGenerator;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User {

  @Id
  @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
  @GeneratedValue(generator = "idGenerator")
  public String id;

  @Column(name = "first_name", nullable = false)
  public String firstName;

  @Column(name = "last_name", nullable = false)
  public String lastName;

  @Column(name = "email", nullable = false, unique = true)
  public String email;

  @Column(name = "username", nullable = false)
  public String username;

  @Column(name = "password", nullable = false)
  public String password;

  @Column(name = "active", nullable = false)
  public boolean active;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  public Role role;

  @OneToOne
  public Administration administration;
}
