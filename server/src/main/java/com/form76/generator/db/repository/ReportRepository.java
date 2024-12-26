package com.form76.generator.db.repository;

import com.form76.generator.db.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, String> {
}
