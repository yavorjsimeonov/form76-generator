package com.form76.generator.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.form76.generator.db.IdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@ToString(exclude = "administration")
@Entity
@Table(name = "location")
@Getter
@Setter
public class Location {

  @Id
  @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
  @GeneratedValue(generator = "idGenerator")
  private String id;

  @Column(name = "ext_community_id", nullable = false)
  private Integer extCommunityId;

  @Column(name = "ext_community_uuid", nullable = false)
  private String extCommunityUuid;

  @Column(name = "name", nullable = false)
  private String name;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name="administration_id", nullable=false)
  private Administration administration;

  @OneToMany(mappedBy = "location", fetch = FetchType.EAGER)
  private Set<Device> devices;

  @Column(name = "representative_name", nullable = false)
  private String representativeName;

  @Column(name = "representative_email", nullable = false)
  private String representativeEmail;

  @Column(name = "report_algorithm", nullable = false)
  @Enumerated(EnumType.STRING)
  private ReportAlgorithm reportAlgorithm;

  @Column(name = "send-email", nullable = false)
  private boolean sendEmail;

  @Column(name = "active", nullable = false)
  private boolean active;

}
