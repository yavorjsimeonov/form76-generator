package com.form76.generator.service;

import com.form76.generator.db.entity.Administration;
import com.form76.generator.db.entity.Location;
import com.form76.generator.db.entity.ReportAlgorithm;
import com.form76.generator.db.entity.ReportFileFormat;
import com.form76.generator.db.repository.AdministrationRepository;
import com.form76.generator.db.repository.LocationRepository;
import com.form76.generator.rest.model.LocationData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocationServiceTest {

  private LocationRepository locationRepository;
  private AdministrationRepository administrationRepository;
  private LocationService locationService;

  @BeforeEach
  void setUp() {
    locationRepository = mock(LocationRepository.class);
    administrationRepository = mock(AdministrationRepository.class);
    locationService = new LocationService();
    locationService.locationRepository = locationRepository;
    locationService.administrationRepository = administrationRepository;
  }

  @Test
  void testGetActiveLocationsInActiveAdministrations() {
    Location location = createSampleLocation();
    when(locationRepository.findAllActiveLocationsInActiveAdministrations())
        .thenReturn(List.of(location));

    List<LocationData> result = locationService.getActiveLocationsInActiveAdministrations();

    assertEquals(1, result.size());
    assertEquals(location.getId(), result.get(0).getId());
  }

  @Test
  void testGetLocationById_Found() {
    Location location = createSampleLocation();
    when(locationRepository.findById("loc1")).thenReturn(Optional.of(location));

    LocationData result = locationService.getLocationById("loc1");

    assertNotNull(result);
    assertEquals("loc1", result.getId());
  }

  @Test
  void testGetLocationById_NotFound() {
    when(locationRepository.findById("invalid")).thenReturn(Optional.empty());

    LocationData result = locationService.getLocationById("invalid");

    assertNull(result);
  }

  @Test
  void testGetLocationByAdministrationId() {
    Location location = createSampleLocation();
    when(locationRepository.findLocationsByAdministrationId("admin1")).thenReturn(List.of(location));

    List<LocationData> result = locationService.getLocationByAdministrationId("admin1");

    assertEquals(1, result.size());
    assertEquals("loc1", result.get(0).getId());
  }

  @Test
  void testEditLocation_Found() {
    Location existing = createSampleLocation();
    Location saved = createSampleLocation();
    saved.setName("Updated");

    LocationData updated = createSampleLocationData();
    updated.setName("Updated");

    when(locationRepository.findById("loc1")).thenReturn(Optional.of(existing));
    when(locationRepository.save(any(Location.class))).thenReturn(saved);

    Optional<LocationData> result = locationService.editLocation("loc1", updated);

    assertTrue(result.isPresent());
    assertEquals("Updated", result.get().getName());
  }

  @Test
  void testEditLocation_NotFound() {
    when(locationRepository.findById("missing")).thenReturn(Optional.empty());

    Optional<LocationData> result = locationService.editLocation("missing", createSampleLocationData());

    assertFalse(result.isPresent());
  }

  @Test
  void testCreateLocation_WithAdmin() {
    LocationData data = createSampleLocationData();
    data.setAdministrationId("admin1");

    Administration admin = new Administration();
    admin.setId("admin1");

    Location location = createSampleLocation();
    when(administrationRepository.findById("admin1")).thenReturn(Optional.of(admin));
    when(locationRepository.save(any(Location.class))).thenReturn(location);

    LocationData result = locationService.createLocation(data);

    assertNotNull(result);
    assertEquals("loc1", result.getId());
  }

  @Test
  void testCreateLocation_AdminNotFound_ShouldThrow() {
    LocationData data = createSampleLocationData();
    data.setAdministrationId("adminX");

    when(administrationRepository.findById("adminX")).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () -> locationService.createLocation(data));
  }

  private Location createSampleLocation() {
    Location loc = new Location();
    loc.setId("loc1");
    loc.setName("Test Location");
    loc.setExtCommunityId(783172831);
    loc.setExtCommunityUuid("uuid1");
    loc.setRepresentativeName("Rep");
    loc.setRepresentativeEmail("rep@test.com");
    loc.setReportAlgorithm(ReportAlgorithm.EVERY_IN_OUT);
    loc.setFileFormat(ReportFileFormat.XLSX);
    loc.setActive(true);
    loc.setSendEmail(true);

    Administration admin = new Administration();
    admin.setId("admin1");
    loc.setAdministration(admin);

    return loc;
  }

  private LocationData createSampleLocationData() {
    return new LocationData(
        "loc1", "Test Location", 783172831, "uuid1", "Rep",
        "rep@test.com", ReportAlgorithm.EVERY_IN_OUT, ReportFileFormat.XLSX, true, true, "admin1", null
    );
  }
}
