package com.form76.generator.db.entity;

import com.form76.generator.db.IdGenerator;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "report")
public class Report {

  @Id
  @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
  @GeneratedValue(generator = "idGenerator")
  public String id;

  @Column(name = "file_name", nullable = false)
  public String fileName;

  @Column(name = "creation_date", nullable = false)
  public LocalDateTime creationDate;

  @Column(name = "cloud_storage_reference", nullable = false)
  public String cloudStorageReference;

  @OneToOne
  Location location;
}