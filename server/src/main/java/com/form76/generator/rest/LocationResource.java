package com.form76.generator.rest;

import com.form76.generator.db.entity.Location;
import com.form76.generator.rest.model.LocationData;
import com.form76.generator.rest.model.ReportData;
import com.form76.generator.service.Form76ReportService;
import com.form76.generator.service.LocationService;
import com.form76.generator.service.ReportService;
import com.form76.generator.service.model.DoorOpeningLogRequest;
import com.form76.generator.service.model.ReportRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/locations")
public class LocationResource {

  Logger logger = LoggerFactory.getLogger(LocationResource.class);

  @Autowired
  LocationService locationService;

  @Autowired
  ReportService reportService;


  @Autowired
  Form76ReportService form76ReportService;

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


  @PostMapping("/{id}/generate")
  public ResponseEntity<String> generateReportForLocation(@PathVariable("id") String locationId, @RequestBody ReportRequest reportRequest) throws ParseException {
    logger.info("Received manual report generation request for location [" + locationId + "]: " + reportRequest);

    LocalDateTime startDateTime = reportRequest.getStartDateTime();
    LocalDateTime endDateTime = reportRequest.getEndDateTime();

    LocationData locationData = locationService.getLocationById(locationId);
    logger.info("Loaded location [" + locationId + "]: " + locationData);

    DoorOpeningLogRequest doorOpeningLogRequest = new DoorOpeningLogRequest(
        locationId, locationData.getName(),
        locationData.getExtCommunityId(), locationData.getExtCommunityUuid(),
        locationData.getReportAlgorithm(),
        startDateTime, endDateTime, locationData.isSendEmail());

    form76ReportService.generateReportForLocation(doorOpeningLogRequest);

    return ResponseEntity.ok("Report generation triggered successfully.");
  }



  @GetMapping("/{id}/reports")
  public List<ReportData> listReportsForLocation(@PathVariable("id") String locationId) {
    return reportService.listReportsForLocation(locationId);
  }
}
