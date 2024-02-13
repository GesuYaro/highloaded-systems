package com.dedlam.ftesterlab.auth.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.io.Serial;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.dedlam.ftesterlab.auth.models.Role.ADMIN;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends DefaultUser {
  @Serial
  private static final long serialVersionUID = 1L;

  public Admin() {
    super();
  }

  public Admin(UUID id, String username, String password) {
    super(id, username, password);
  }

  @Transient
  @Override
  public List<Role> getAuthorities() {
    List<Role> roles = new LinkedList<>(super.getAuthorities());
    roles.add(ADMIN);
    return roles;
  }
}
