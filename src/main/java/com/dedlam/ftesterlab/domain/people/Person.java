package com.dedlam.ftesterlab.domain.people;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "people")
class Person {
  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "first_name")
  private String name;

  public Person() {
  }

  public Person(UUID id, String name) {
    this.id = id;
    this.name = name;
  }
}
