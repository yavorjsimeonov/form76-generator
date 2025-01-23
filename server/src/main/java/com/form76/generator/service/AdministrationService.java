package com.form76.generator.service;

import com.form76.generator.db.entity.Administration;
import com.form76.generator.db.entity.Role;
import com.form76.generator.db.entity.User;
import com.form76.generator.db.repository.AdministrationRepository;
import com.form76.generator.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AdministrationService {

  @Autowired
  private AdministrationRepository administrationRepository;

  @Autowired
  private UserRepository userRepository;

  public List<Administration> listAdministrations() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    if (userDetails == null) {
      throw new RuntimeException("Access is Denied. Please again login or  contact service provider");
    }

    GrantedAuthority adminRole = userDetails.getAuthorities().stream()
        .filter(authority -> authority.getAuthority().equals(Role.ADMIN.name()))
        .findAny()
        .orElse(null);
    if (adminRole != null) {
      return administrationRepository.findAll();
    }

    User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
    if (user == null) {
      throw new RuntimeException("Cannot find user with username: " + userDetails.getUsername());
    }


    Administration administration = administrationRepository.findById(user.administration.id).orElse(null);
    if (administration == null) {
      throw new RuntimeException("Cannot find administration with id: " + user.administration.id);
    }
    return Collections.singletonList(administration);
  }
}
