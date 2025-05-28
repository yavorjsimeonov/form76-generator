package com.form76.generator.db.repository;

import com.form76.generator.db.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, String> {

  @Query("SELECT r FROM Report r WHERE r.location.id = :locationId order by r.creationDate desc")
  List<Report> findReportsByLocationId(@Param("locationId")String locationId);

  @Query("SELECT r FROM Report r WHERE r.location.administration.id = :administrationId order by r.creationDate desc")
  List<Report> findReportsByAdministration(@Param("administrationId")String administrationId);

}
