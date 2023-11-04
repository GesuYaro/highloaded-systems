package com.dedlam.ftesterlab.domain.people.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "people")
public class Person {
  public Person() {
  }

  public Person(UUID id, String name, String middleName, String lastName, LocalDate birthday) {
    this.id = id;
    this.name = name;
    this.middleName = middleName;
    this.lastName = lastName;
    this.birthday = birthday;
  }

  @Id
  @Column(nullable = false)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column
  private String middleName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false)
  private LocalDate birthday;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthDay) {
    this.birthday = birthDay;
  }
}
