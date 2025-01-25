package com.form76.generator.rest;

import com.form76.generator.rest.model.AdministrationData;
import com.form76.generator.rest.model.LocationData;
import com.form76.generator.service.AdministrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/administrations")
public class AdministrationResource {

  @Autowired
  private AdministrationService administrationService;

  @GetMapping()
  public List<AdministrationData> getAdministrations() {
    return administrationService.listAdministrations();
  }

  @PostMapping()
  public AdministrationData createAdministration(@RequestBody AdministrationData administrationData) {
    return administrationService.createAdministration(administrationData);
  }

  @GetMapping("/{id}")
  public AdministrationData getAdministrationById(@PathVariable String id) {
    return administrationService.findById(id);
  }

  @PutMapping("/{id}")
  public AdministrationData editAdministration(@PathVariable String id, @RequestBody AdministrationData updatedAdministrationData) {
    return administrationService.updateAdministration(updatedAdministrationData);
  }

  @GetMapping("/{id}/locations")
  public ResponseEntity<Set<LocationData>> getLocationsByAdministrationId(@PathVariable String id) {
    AdministrationData administrationData = getAdministrationById(id);
    Set<LocationData> locationData = administrationData.getLocations();
    return ResponseEntity.ok(locationData);
  }

}
