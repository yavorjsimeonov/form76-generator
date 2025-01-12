package com.form76.generator.service;

import com.form76.generator.db.entity.Administration;
import com.form76.generator.db.repository.AdministrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdministrationService {

  @Autowired
  private AdministrationRepository administrationRepository;

  public List<Administration> listAdministrations() {
    return administrationRepository.findAll();
  }
}
