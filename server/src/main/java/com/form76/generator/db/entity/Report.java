package com.form76.generator.db.entity;

import com.form76.generator.db.IdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "report")
@Getter
@Setter
public class Report {

  @Id
  @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
  @GeneratedValue(generator = "idGenerator")
  private String id;

  @Column(name = "file_name", nullable = false)
  private String fileName;

  @Column(name = "creation_date", nullable = false)
  private LocalDateTime creationDate;

  @Column(name = "cloud_storage_reference")
  private String cloudStorageReference;

  @Column(name = "report_period_start_date_time", nullable = false)
  private LocalDateTime reportPeriodStartDateTime;

  @Column(name = "report_period_end_date_time", nullable = false)
  private LocalDateTime reportPeriodEndDateTime;

  @ManyToOne
  @JoinColumn(name="location_id", nullable=false)
  private Location location;
}