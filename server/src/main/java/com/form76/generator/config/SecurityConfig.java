package com.form76.generator.config;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;

//@Configuration
public class SecurityConfig {

//  @Bean
//  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    http.authorizeHttpRequests((authz) -> authz
//        .requestMatchers("/api/register", "/api/login", "/api/logout").permitAll()
//    ).formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.loginPage("/login").permitAll());
//    return http.build();
//  }
//
//
//  @Bean
//  @Order(1)
//  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
//    http
//        .securityMatcher("/api/**")
//        .authorizeHttpRequests(authorize -> authorize
//            .anyRequest().hasRole("ADMIN")
//        )
//        .httpBasic(Customizer.withDefaults());
//    return http.build();
//  }
//
//  @Bean
//  public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
//    http
//        .authorizeHttpRequests(authorize -> authorize
//            .anyRequest().authenticated()
//        )
//        .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.loginPage("/login").permitAll());
//    return http.build();
//  }


//  @Bean
//  @Order(1)
//  public SecurityFilterChain approvalsSecurityFilterChain(HttpSecurity http) throws Exception {
//    String[] approvalsPaths = { "/accounts/approvals/**", "/loans/approvals/**", "/credit-cards/approvals/**" };
//    http
//        .securityMatcher(approvalsPaths)
//        .authorizeHttpRequests(authorize -> authorize
//            .anyRequest().hasRole("ADMIN")
//        )
//        .httpBasic(Customizer.withDefaults());
//    return http.build();
//  }
//
//  @Bean
//  @Order(2)
//  public SecurityFilterChain generatorSecurityFilterChain(HttpSecurity http) throws Exception {
//    String[] generatorPaths = { "/generator/**" };
//    http
//        .securityMatcher(generatorPaths)
//        .authorizeHttpRequests(authorize -> authorize
//            .anyRequest().authenticated()
//        )
//        .anyRequest().authenticated().formLogin(formLogin -> formLogin
//    ;
//    return http.build();
//  }

//  @Bean
//  @Order(1)
//  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
//    logger.info("~~~~~~~~ Received request in apiFilterChain");
//
//    String[] allowedPaths = { "/api/login", "/api/logout" };
//
//    http
//        .securityMatcher("/api/**")
//        .authorizeHttpRequests(authorize -> authorize
//            .requestMatchers(allowedPaths).permitAll()
//            .anyRequest().authenticated()
//        )
//        .httpBasic(Customizer.withDefaults());
//    return http.build();
//  }
//  @Bean
//  public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//    logger.info("~~~~~~~~ Received request in defaultSecurityFilterChain");
//
//    String[] allowedPaths = { "/auth/authenticate", "/auth/logout", "/auth/register" };
//    http
//        .authorizeHttpRequests(authorize -> authorize
//            .requestMatchers(allowedPaths).permitAll()
//            .anyRequest().authenticated()
//        )
//        .formLogin(formLogin -> formLogin
//            .loginPage("/login")
//            .loginProcessingUrl("/auth/authenticate")
//        )
//        .logout(logout -> logout
//            .logoutUrl("/logout")
//            .logoutSuccessUrl("/?logout")
//        );
//
//    return http.build();
//  }


//  @Bean
//  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    return http
//        .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
////            .requestMatchers(HttpMethod.GET, "/api/books", "/api/books/**").hasAnyAuthority("ADMIN", "USER")
////            .requestMatchers(HttpMethod.GET, "/api/users/me").hasAnyAuthority("ADMIN", "USER")
////            .requestMatchers("/api/books", "/api/books/**").hasAuthority(ADMIN)
////            .requestMatchers("/api/users", "/api/users/**").hasAuthority(ADMIN)
//            .requestMatchers("/auth/**").permitAll()
//            .requestMatchers("/", "/error", "/csrf").permitAll()
//            .anyRequest().authenticated())
//        .formLogin(formLogin -> formLogin
//            .loginPage("/login")
//            .loginProcessingUrl("/auth/authenticate")
//        )
//        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//        .build();
//  }
//

}