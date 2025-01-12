package com.form76.generator.service;

import com.form76.generator.db.entity.Location;
import com.form76.generator.db.repository.LocationRepository;
import com.form76.generator.service.model.DoorOpeningLogRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

  @Autowired
  private LocationRepository locationRepository;

  public List<Location> getActiveLocationsInActiveAdministrations() {
    return locationRepository.findAllActiveLocationsInActiveAdministrations();
  }

  public Location getLocationById(String id){
    return locationRepository.getReferenceById(id);
  }

  public List<Location> getLocationByAdministrationId(String id) {
    return locationRepository.findLocationsByAdministrationId(id);
  }

  public Optional<Location> editLocation(String id, Location updatedLocation) {
    return locationRepository.findById(id).map(existingLocation -> {
      existingLocation.name = updatedLocation.name;
      existingLocation.active = updatedLocation.active;
      existingLocation.extCommunityId = updatedLocation.extCommunityId;
      existingLocation.extCommunityUuid = updatedLocation.extCommunityUuid;
      existingLocation.reportAlgorithm = updatedLocation.reportAlgorithm;
      existingLocation.representativeName = updatedLocation.representativeName;
      existingLocation.representativeEmail = updatedLocation.representativeEmail;

      return locationRepository.save(existingLocation);
    });
  }



}
