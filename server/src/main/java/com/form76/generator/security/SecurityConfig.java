package com.form76.generator.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;

@Configuration
public class SecurityConfig {
  Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    logger.info("Registering SecurityFilterChain...");
    return http
        .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
            .requestMatchers(OPTIONS, "/**").permitAll()
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/error", "/csrf").permitAll()
            //.requestMatchers("/api/**").authenticated()
            .requestMatchers("/api/administrations").hasAnyAuthority("ADMIN", "USER")
            .requestMatchers(POST, "/api/administrations").hasAnyAuthority("ADMIN")
            .requestMatchers("/api/administrations/**").hasAnyAuthority("ADMIN", "USER")
            .requestMatchers("/api/locations/**").hasAnyAuthority("ADMIN", "USER")
            .requestMatchers("/api/users").hasAnyAuthority("ADMIN")
            .requestMatchers("/api/users/**").hasAnyAuthority("ADMIN")
            .requestMatchers("/api/**").hasAnyAuthority("ADMIN")
            .anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults())
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .csrf(AbstractHttpConfigurer::disable)
        .build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

}
