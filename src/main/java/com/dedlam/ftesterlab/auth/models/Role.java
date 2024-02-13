package com.dedlam.ftesterlab.auth.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  ADMIN("ADMIN"),
  DEFAULT_USER("DEFAULT_USER"),
  TEACHER("TEACHER"),
  STUDENT("STUDENT");

  private final String name;

  Role(String name) {
    this.name = name;
  }


  @Override
  public String getAuthority() {
    return name;
  }
}
