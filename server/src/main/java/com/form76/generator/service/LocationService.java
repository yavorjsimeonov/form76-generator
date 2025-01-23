package com.form76.generator.service;

import com.form76.generator.db.entity.Administration;
import com.form76.generator.db.entity.Location;
import com.form76.generator.db.repository.AdministrationRepository;
import com.form76.generator.db.repository.LocationRepository;
import com.form76.generator.service.model.LocationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private AdministrationRepository administrationRepository;

  public List<Location> getActiveLocationsInActiveAdministrations() {
    return locationRepository.findAllActiveLocationsInActiveAdministrations();
  }

  public Location getLocationById(String id){
    return locationRepository.findById(id).orElse(null);
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


  public Location createLocation(LocationRequest locationRequest) {
    Administration administration = administrationRepository.findById(locationRequest.getAdministrationId())
        .orElseThrow(() -> new IllegalArgumentException("Administration not found"));

    Location location = new Location();
    location.name = locationRequest.getName();
    location.extCommunityId = locationRequest.getExtCommunityId();
    location.extCommunityUuid = locationRequest.getExtCommunityUuid();
    location.representativeName = locationRequest.getRepresentativeName();
    location.representativeEmail = locationRequest.getRepresentativeEmail();
    location.reportAlgorithm = locationRequest.getReportAlgorithm();
    location.active = locationRequest.isActive();
    location.administration = administration;

    return locationRepository.save(location);
  }


}
