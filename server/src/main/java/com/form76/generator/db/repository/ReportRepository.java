package com.form76.generator.db.repository;

import com.form76.generator.db.entity.Location;
import com.form76.generator.db.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, String> {

  @Query("SELECT r FROM Report r WHERE r.location.id = :locationId")
  List<Report> findReportsByByLocationId(@Param("locationId")String locationId);

}
