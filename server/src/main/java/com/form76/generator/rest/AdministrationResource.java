package com.form76.generator.rest;

import com.form76.generator.db.entity.Administration;
import com.form76.generator.db.entity.Location;
import com.form76.generator.db.repository.AdministrationRepository;
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
  public List<Administration> getAdministrations() {
    return administrationService.listAdministrations();
  }

  @PostMapping()
  public Administration createAdministration(@RequestBody Administration administration) {
    return administrationService.createAdministration(administration);
  }

  @GetMapping("/{id}")
  public Administration getAdministrationById(@PathVariable String id) {
    return administrationService.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Administration not found"));
  }

  @PutMapping("/{id}")
  public Administration editAdministration(@PathVariable String id, @RequestBody Administration updatedAdministration) {
    Administration admin = administrationService.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Administration not found"));
    admin.setName(updatedAdministration.getName());
    admin.setActive(updatedAdministration.isActive());
    return administrationService.createAdministration(admin);
  }

  @GetMapping("/{id}/locations")
  public ResponseEntity<Set<Location>> getLocationsByAdministrationId(@PathVariable String id) {
    Administration administration = getAdministrationById(id);
    Set<Location> locations = administration.getLocations();
    return ResponseEntity.ok(locations);
  }

}
