package com.dedlam.ftesterlab.auth.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.dedlam.ftesterlab.auth.models.Role.STUDENT;

@Entity
@DiscriminatorValue("STUDENT")
public class Student extends DefaultUser {
  public Student() {
    super();
  }

  public Student(UUID id, String username, String password) {
    super(id, username, password);
  }

  @Transient
  @Override
  public List<Role> getAuthorities() {
    List<Role> roles = new LinkedList<>(super.getAuthorities());
    roles.add(STUDENT);
    return roles;
  }
}
