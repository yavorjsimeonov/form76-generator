package com.form76.generator.db.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "administration")
public class Administration {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_administration_user"))
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user;

  @ManyToOne
  @JoinColumn(name = "location_id", foreignKey = @ForeignKey(name = "FK_administration_location"))
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Location location;

  @ManyToOne
  @JoinColumn(name = "report_id", foreignKey = @ForeignKey(name = "FK_administration_report"))
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Report report;

  public Administration() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Report getReport() {
    return report;
  }

  public void setReport(Report report) {
    this.report = report;
  }
}
