package com.dedlam.ftesterlab.configuration;

import com.dedlam.ftesterlab.auth.JwtFilter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.dedlam.ftesterlab.auth.models.Role.*;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class SecurityConfig {
  private final JwtFilter jwtFilter;

  public SecurityConfig(JwtFilter jwtFilter) {
    this.jwtFilter = jwtFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      .csrf(csrf -> csrf
        .ignoringRequestMatchers(PathRequest.toH2Console())
        .ignoringRequestMatchers(antMatcher("/h2/**"))
        .ignoringRequestMatchers(antMatcher("/**"))
      )
      .headers(headers -> headers
        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
      )
      .authorizeHttpRequests(
        auth -> auth
          .requestMatchers(PathRequest.toH2Console()).permitAll()
          .requestMatchers(antMatcher("/auth/login")).permitAll()
          .requestMatchers(antMatcher("/auth/token")).permitAll()
          .requestMatchers(antMatcher("/admin/**")).hasAuthority(ADMIN.getAuthority())
          .requestMatchers(antMatcher("/people/**")).hasAuthority(DEFAULT_USER.getAuthority())
          .requestMatchers(antMatcher("/teachers/**")).hasAuthority(TEACHER.getAuthority())
          .requestMatchers(antMatcher("/students/**")).hasAuthority(STUDENT.getAuthority())
          .requestMatchers(antMatcher("/")).permitAll()
          .requestMatchers(antMatcher("/**")).permitAll()
          .anyRequest().authenticated()
      )
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
  }
}
