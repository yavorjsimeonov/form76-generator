package com.form76.generator.service;

import com.form76.generator.db.entity.Administration;
import com.form76.generator.db.entity.Location;
import com.form76.generator.db.repository.AdministrationRepository;
import com.form76.generator.db.repository.LocationRepository;
import com.form76.generator.rest.model.AdministrationData;
import com.form76.generator.rest.model.LocationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private AdministrationRepository administrationRepository;

  public List<LocationData> getActiveLocationsInActiveAdministrations() {
    return locationRepository.findAllActiveLocationsInActiveAdministrations().stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  public LocationData getLocationById(String id) {
    return locationRepository.findById(id)
        .map(this::convertToDTO)
        .orElse(null);
  }

  public List<LocationData> getLocationByAdministrationId(String id) {
    return locationRepository.findLocationsByAdministrationId(id).stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  public Optional<LocationData> editLocation(String id, LocationData updatedLocationData) {
    return locationRepository.findById(id).map(existingLocation -> {
      existingLocation.setName(updatedLocationData.getName());
      existingLocation.setActive(updatedLocationData.isActive());
      existingLocation.setExtCommunityId(updatedLocationData.getExtCommunityId());
      existingLocation.setExtCommunityUuid(updatedLocationData.getExtCommunityUuid());
      existingLocation.setReportAlgorithm(updatedLocationData.getReportAlgorithm());
      existingLocation.setRepresentativeName(updatedLocationData.getRepresentativeName());
      existingLocation.setRepresentativeEmail(updatedLocationData.getRepresentativeEmail());

      Location savedLocation = locationRepository.save(existingLocation);
      return convertToDTO(savedLocation);
    });
  }

  public LocationData createLocation(LocationData locationData) {
    Location savedLocation = locationRepository.save(convertToLocation(locationData));
    return convertToDTO(savedLocation);
  }

  private Location convertToLocation(LocationData locationData) {
    Location location = new Location();
    location.setId(locationData.getId());
    location.setExtCommunityId(locationData.getExtCommunityId());
    location.setExtCommunityUuid(locationData.getExtCommunityUuid());
    location.setName(locationData.getName());
    location.setRepresentativeName(locationData.getRepresentativeName());
    location.setRepresentativeEmail(locationData.getRepresentativeEmail());
    location.setReportAlgorithm(locationData.getReportAlgorithm());
    location.setActive(locationData.isActive());

    if (locationData.getAdministrationId() != null) {
      Administration administration = administrationRepository.findById(locationData.getAdministrationId())
          .orElseThrow(() -> new IllegalArgumentException("Administration not found for id: " + locationData.getAdministrationId()));
      location.setAdministration(administration);
    }

    return location;
  }

  private LocationData convertToDTO(Location location) {
    return new LocationData(
        location.getId(),
        location.getName(),
        location.getExtCommunityId(),
        location.getExtCommunityUuid(),
        location.getRepresentativeName(),
        location.getRepresentativeEmail(),
        location.getReportAlgorithm(),
        location.isActive(),
        location.getAdministration() != null ? location.getAdministration().getId() : null,
        null // Handle device conversion if necessary
    );
  }

  private Administration convertToEntity(AdministrationData administrationData) {
    Administration administration = new Administration();
    administration.setId(administrationData.getId());
    administration.setName(administrationData.getName());
    administration.setActive(administrationData.isActive());

    return administration;
  }

  private AdministrationData convertToDTO(Administration administration) {
    return new AdministrationData(
        administration.getId(),
        administration.getName(),
        administration.isActive(),
        null // Handle location conversion if necessary
    );
  }
}
