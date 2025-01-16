package com.form76.generator.security;

import com.form76.generator.db.entity.User;
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
    User user = userService.getUserByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
    List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.role.name()));
    return mapUserToCustomUserDetails(user, authorities);
  }

  private AppUserDetails mapUserToCustomUserDetails(User user, List<SimpleGrantedAuthority> authorities) {
    AppUserDetails appUserDetails = new AppUserDetails();
    appUserDetails.setId(user.id);
    appUserDetails.setUsername(user.username);
    appUserDetails.setPassword(user.password);
    appUserDetails.setFirstName(user.firstName);
    appUserDetails.setLastName(user.lastName);
    appUserDetails.setEmail(user.email);
    appUserDetails.setActive(user.active);
    appUserDetails.setAuthorities(authorities);
    return appUserDetails;
  }
}