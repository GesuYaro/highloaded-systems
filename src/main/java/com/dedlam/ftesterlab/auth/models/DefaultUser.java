package com.dedlam.ftesterlab.auth.models;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

import static com.dedlam.ftesterlab.auth.models.Role.DEFAULT_USER;

@Entity
@Table(name = "users")
public class DefaultUser implements UserDetails {
  @Serial
  private static final long serialVersionUID = 1L;

  @Getter
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  public DefaultUser() {

  }

  public DefaultUser(UUID id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = password;
  }

  @Transient
  @Override
  public List<Role> getAuthorities() {
    return List.of(DEFAULT_USER);
  }

  @Transient
  @Override
  public String getPassword() {
    return password;
  }

  @Transient
  @Override
  public String getUsername() {
    return username;
  }

  @Transient
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Transient
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Transient
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Transient
  @Override
  public boolean isEnabled() {
    return true;
  }
}
