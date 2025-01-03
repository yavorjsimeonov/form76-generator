package com.form76.generator.db.repository;

import com.form76.generator.db.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, String> {
  @Query("SELECT l FROM Location l " +
      "WHERE l.active = true and l.administration.active = true")
  List<Location> findAllActiveLocationsInActiveAdministrations();

}
