package com.form76.generator.db.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.form76.generator.db.IdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@ToString(exclude = "location")
@Entity
@Table(name = "device")
@Getter
@Setter
public class Device {

  @Id
  @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
  @GeneratedValue(generator = "idGenerator")
  private String id;

  @Column(name = "external_id", nullable = false)
  private String externalId;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private DeviceType type;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "active", nullable = false)
  private boolean active;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name="location_id", nullable=false)
  private Location location;
}

