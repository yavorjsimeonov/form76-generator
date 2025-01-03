package com.form76.generator.service;

import com.form76.generator.db.entity.Location;
import com.form76.generator.db.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

  @Autowired
  private LocationRepository locationRepository;

  public List<Location> getActiveLocationsInActiveAdministrations() {
    return locationRepository.findAllActiveLocationsInActiveAdministrations();
  }

}
