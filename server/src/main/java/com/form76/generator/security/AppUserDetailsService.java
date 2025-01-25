package com.form76.generator.security;

import com.form76.generator.db.entity.User;
import com.form76.generator.rest.model.UserData;
import com.form76.generator.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AppUserDetailsService implements UserDetailsService {
  Logger logger = LoggerFactory.getLogger(AppUserDetailsService.class);

  @Autowired
  UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) {
    logger.info("Loading user by username:" + username);
    UserData user = userService.getUserByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
    List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
    return mapUserToAppUserDetails(user, authorities);
  }

  private AppUserDetails mapUserToAppUserDetails(UserData user, List<SimpleGrantedAuthority> authorities) {
    AppUserDetails appUserDetails = new AppUserDetails();
    appUserDetails.setId(user.getId());
    appUserDetails.setUsername(user.getUsername());
    appUserDetails.setPassword(user.getPassword());
    appUserDetails.setFirstName(user.getFirstName());
    appUserDetails.setLastName(user.getLastName());
    appUserDetails.setEmail(user.getEmail());
    appUserDetails.setActive(user.isActive());
    appUserDetails.setAuthorities(authorities);
    return appUserDetails;
  }
}