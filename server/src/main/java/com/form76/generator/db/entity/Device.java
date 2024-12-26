package com.form76.generator.db.entity;


import com.form76.generator.db.IdGenerator;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "device")
public class Device {

  @Id
  @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
  @GeneratedValue(generator = "idGenerator")
  public String id;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  public DeviceType type;

  @Column(name = "name", nullable = false)
  public String name;

  @Column(name = "active", nullable = false)
  public boolean active;

  @ManyToOne
  @JoinColumn(name="location_id", nullable=false)
  public Location location;
}

