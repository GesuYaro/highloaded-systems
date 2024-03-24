package com.dedlam.ftesterlab.domain.people.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.UUID;

public class Person {
  public Person() {
  }

  public Person(UUID id, String name, String middleName, String lastName, LocalDate birthday, UUID userId) {
    this.id = id;
    this.name = name;
    this.middleName = middleName;
    this.lastName = lastName;
    this.birthday = birthday;
    this.userId = userId;
  }

  private UUID id;

  private String name;

  private String middleName;

  private String lastName;

  @JsonFormat(pattern = "dd.MM.yyyy")
  private LocalDate birthday;

  private UUID userId;

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

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }
}
