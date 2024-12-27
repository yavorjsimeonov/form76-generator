package com.form76.generator.controller;

import com.form76.generator.db.entity.Administration;
import com.form76.generator.db.repository.AdministrationRepository;
import com.form76.generator.service.AdministrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdministrationController {

  @Autowired
  private AdministrationService administrationService;

  @Autowired
  private AdministrationRepository administrationRepository;

  @GetMapping("/administrations")
  public List<Administration> getAdministrations() {
    return administrationService.listAdministrations();
  }

  @PostMapping("/administrations")
  public Administration createAdministration(@RequestBody Administration administration) {
    return administrationRepository.save(administration);
  }

  @GetMapping("/administrations/{id}")
  public Administration getAdministrationById(@PathVariable String id) {
    return administrationRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Administration not found"));
  }

  @PutMapping("/administrations/{id}")
  public Administration editAdministration(@PathVariable String id, @RequestBody Administration updatedAdministration) {
    Administration admin = administrationRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Administration not found"));
    admin.setName(updatedAdministration.getName());
    admin.setActive(updatedAdministration.isActive());
    return administrationRepository.save(admin);
  }

}
