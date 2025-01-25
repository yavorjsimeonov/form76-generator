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
      existingLocation.setName(updatedLocation.getName());
      existingLocation.setActive(updatedLocation.isActive());
      existingLocation.setExtCommunityId(updatedLocation.getExtCommunityId());
      existingLocation.setExtCommunityUuid(updatedLocation.getExtCommunityUuid());
      existingLocation.setReportAlgorithm(updatedLocation.getReportAlgorithm());
      existingLocation.setRepresentativeName(updatedLocation.getRepresentativeName());
      existingLocation.setRepresentativeEmail(updatedLocation.getRepresentativeEmail());

      return locationRepository.save(existingLocation);
    });
  }


  public Location createLocation(LocationRequest locationRequest) {
    Administration administration = administrationRepository.findById(locationRequest.getAdministrationId())
        .orElseThrow(() -> new IllegalArgumentException("Administration not found"));

    Location location = new Location();
    location.setName(locationRequest.getName());
    location.setExtCommunityId(locationRequest.getExtCommunityId());
    location.setExtCommunityUuid(locationRequest.getExtCommunityUuid());
    location.setRepresentativeName(locationRequest.getRepresentativeName());
    location.setRepresentativeEmail(locationRequest.getRepresentativeEmail());
    location.setReportAlgorithm(locationRequest.getReportAlgorithm());
    location.setActive(locationRequest.isActive());
    location.setAdministration(administration);

    return locationRepository.save(location);
  }


}
