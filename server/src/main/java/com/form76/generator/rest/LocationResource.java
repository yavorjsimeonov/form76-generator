package com.form76.generator.rest;

import com.form76.generator.db.entity.Location;
import com.form76.generator.service.Form76ReportService;
import com.form76.generator.service.LocationService;
import com.form76.generator.service.model.DoorOpeningLogRequest;
import com.form76.generator.service.model.ReportRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LocationResource {

  Logger logger = LoggerFactory.getLogger(LocationResource.class);

  @Autowired
  LocationService locationService;

  @Autowired
  Form76ReportService form76ReportService;

  @GetMapping("/administrations/{id}/locations")
  public ResponseEntity<List<Location>> getLocationsByAdministrationId(@PathVariable String id) {
    List<Location> locations = locationService.getLocationByAdministrationId(id);
    return ResponseEntity.ok(locations);
  }

  @GetMapping("/locations/{id}")
  public ResponseEntity<Location> getLocationsById(@PathVariable String id) {
    Location location = locationService.getLocationById(id);
    return ResponseEntity.ok(location);
  }

  @PutMapping("/locations/{id}")
  public ResponseEntity<Location> editLocation(@PathVariable("id") String id, @RequestBody Location updatedLocation) {
    Optional<Location> location = locationService.editLocation(id, updatedLocation);

    return location.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping("/locations/{id}/generate")
  public ResponseEntity<String> generateReportForLocation(@PathVariable("id") String locationId, @RequestBody ReportRequest reportRequest) throws ParseException {
//    LocalDateTime startDateTime = reportRequest.getStartDateTime();
//    LocalDateTime endDateTime = reportRequest.getEndDateTime();

    LocalDateTime startDateTime = LocalDateTime.now().minusMonths(1);
    LocalDateTime endDateTime = LocalDateTime.now();

    Location location = locationService.getLocationById(locationId);

    DoorOpeningLogRequest doorOpeningLogRequest = new DoorOpeningLogRequest(location, startDateTime, endDateTime);

    form76ReportService.generateReportForLocation(doorOpeningLogRequest);

    return ResponseEntity.ok("Report generation triggered successfully.");
  }


}
