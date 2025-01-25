package com.form76.generator.rest;

import com.form76.generator.rest.model.LocationData;
import com.form76.generator.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/locations")
public class LocationResource {

  Logger logger = LoggerFactory.getLogger(LocationResource.class);

  @Autowired
  private LocationService locationService;

  @GetMapping("/{id}")
  public ResponseEntity<LocationData> getLocationById(@PathVariable String id) {
    LocationData locationData = locationService.getLocationById(id);
    if (locationData != null) {
      return ResponseEntity.ok(locationData);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<LocationData> editLocation(@PathVariable("id") String id, @RequestBody LocationData updatedLocationData) {
    Optional<LocationData> locationData = locationService.editLocation(id, updatedLocationData);
    return locationData.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping("")
  public ResponseEntity<LocationData> createLocation(@RequestBody LocationData locationData) {
    LocationData createdLocation = locationService.createLocation(locationData);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdLocation);
  }
}
