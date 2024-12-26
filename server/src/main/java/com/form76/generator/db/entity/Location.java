package com.form76.generator.db.entity;

import com.form76.generator.db.IdGenerator;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Entity
@Table(name = "location")
public class Location {

  @Id
  @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
  @GeneratedValue(generator = "idGenerator")
  public String id;

  @Column(name = "name", nullable = false)
  public String name;

  @ManyToOne
  @JoinColumn(name="administration_id", nullable=false)
  public Administration administration;

  @OneToMany(mappedBy = "location", fetch = FetchType.EAGER)
  public Set<Device> devices;

  //  //TODO: add representative columns
//  @Column(name = "representative_name", nullable = false)
//  public String representativeName;
//
//  @Column(name = "representative_email", nullable = false)
//  public String representativeEmail;

  @Column(name = "active", nullable = false)
  public boolean active;

}
