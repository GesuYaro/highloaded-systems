package com.dedlam.ftesterlab.auth.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.LinkedList;

public class Teacher extends User {
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new LinkedList<>(super.getAuthorities());
    authorities.add(new SimpleGrantedAuthority("TEACHER_ROLE"));
    return authorities;
  }
}
