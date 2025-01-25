package com.form76.generator.db.entity;

import com.form76.generator.db.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@ToString
@Entity
@Table(name = "administration")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Administration {

  @Id
  @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
  @GeneratedValue(generator = "idGenerator")
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "active", nullable = false)
  private boolean active;

  @OneToMany(mappedBy = "administration", fetch = FetchType.EAGER)
  private Set<Location> locations;

}
