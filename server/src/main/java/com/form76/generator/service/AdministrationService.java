package com.form76.generator.service;

import com.form76.generator.db.entity.*;
import com.form76.generator.db.repository.AdministrationRepository;
import com.form76.generator.db.repository.UserRepository;
import com.form76.generator.rest.model.AdministrationData;
import com.form76.generator.rest.model.LocationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdministrationService {

  @Autowired
  private AdministrationRepository administrationRepository;

  @Autowired
  private UserRepository userRepository;

  public List<AdministrationData> listAdministrations() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    if (userDetails == null) {
      throw new RuntimeException("Access is Denied. Please again login or contact service provider");
    }

    GrantedAuthority adminRole = userDetails.getAuthorities().stream()
        .filter(authority -> authority.getAuthority().equals(Role.ADMIN.name()))
        .findAny()
        .orElse(null);

    if (adminRole != null) {
      return administrationRepository.findAll().stream()
          .map(this::convertToAdministrationData)
          .collect(Collectors.toList());
    }

    User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() ->
        new RuntimeException("Cannot find user with username: " + userDetails.getUsername()));

    Administration administration = administrationRepository.findById(user.getAdministration().getId())
        .orElseThrow(() -> new RuntimeException("Cannot find administration with id: " + user.getAdministration().getId()));

    return List.of(convertToAdministrationData(administration));
  }

  public AdministrationData createAdministration(AdministrationData administrationData) {

    Administration administration = convertToAdministration(administrationData);
    administration = administrationRepository.save(administration);
    return convertToAdministrationData(administration);
  }

  public AdministrationData updateAdministration(AdministrationData updatedAdministrationData) {
    Administration administration = administrationRepository.findById(updatedAdministrationData.getId()).orElseThrow(() -> new IllegalArgumentException("Administration not found for id: " + updatedAdministrationData.getId()));
    administration.setName(updatedAdministrationData.getName());
    administration.setActive(updatedAdministrationData.isActive());
    administration = administrationRepository.save(administration);
    return convertToAdministrationData(administration);
  }
  public AdministrationData findById(String id) {
    Administration administration = administrationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Administration not found for id: " + id));
    return convertToAdministrationData(administration);
  }

  private Administration convertToAdministration(AdministrationData administrationData) {
    Administration administration = new Administration();
    administration.setId(administrationData.getId());
    administration.setName(administrationData.getName());
    administration.setActive(administrationData.isActive());

    if (administrationData.getLocations() != null) {
      administration.setLocations(
          administrationData.getLocations().stream()
              .map(this::convertToLocation)
              .collect(Collectors.toSet())
      );
    }

    return administration;
  }

  private AdministrationData convertToAdministrationData(Administration administration) {
    return new AdministrationData(
        administration.getId(),
        administration.getName(),
        administration.isActive(),
        administration.getLocations() != null
            ? administration.getLocations().stream()
            .map(this::convertToLocationData)
            .collect(Collectors.toSet())
            : null
    );
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
    location.setSendEmail(locationData.isSendEmail());

    if (locationData.getAdministrationId() != null) {
      Administration administration = administrationRepository.findById(locationData.getAdministrationId()).orElseThrow(() -> new IllegalArgumentException("Administration not found for id: " + locationData.getAdministrationId()));;
      location.setAdministration(administration);
    }
    return location;
  }

  private LocationData convertToLocationData(Location location) {
    return new LocationData(
        location.getId(),
        location.getName(),
        location.getExtCommunityId(),
        location.getExtCommunityUuid(),
        location.getRepresentativeName(),
        location.getRepresentativeEmail(),
        location.getReportAlgorithm(),
        location.getFileFormat(),
        location.isActive(),
        location.isSendEmail(),
        null,
        /*location.getAdministration() != null
            ? convertToAdministrationData(location.getAdministration())
            : null,*/
        null// Devices conversion can be added here if needed
    );
  }
}
