package com.form76.generator.db.entity;

import com.form76.generator.db.IdGenerator;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;

@Entity
@Table(name = "administration")
public class Administration {

  @Id
  @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
  @GeneratedValue(generator = "idGenerator")
  public String id;

  @Column(name = "name", nullable = false)
  public String name;

  @Column(name = "active", nullable = false)
  public boolean active;

  @OneToMany(mappedBy = "administration", fetch = FetchType.EAGER)
  public Set<Location> locations;
}
