package com.form76.generator.db.repository;

import com.form76.generator.db.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, String> {
}
