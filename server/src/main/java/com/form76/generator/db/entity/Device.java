package com.form76.generator.db.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.form76.generator.db.IdGenerator;
import jakarta.persistence.*;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@ToString(exclude = "location")
@Entity
@Table(name = "device")
public class Device {

  @Id
  @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
  @GeneratedValue(generator = "idGenerator")
  public String id;

  @Column(name = "external_id", nullable = false)
  public String externalId;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  public DeviceType type;

  @Column(name = "name", nullable = false)
  public String name;

  @Column(name = "active", nullable = false)
  public boolean active;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name="location_id", nullable=false)
  public Location location;
}

