package com.dedlam.ftesterlab.domain.people.services;

import java.time.LocalDate;

public class PersonDto {
  private final String name;
  private final String middleName;
  private final String lastName;
  private final LocalDate birthday;

  public PersonDto(String name, String middleName, String lastName, LocalDate birthday) {
    this.name = name;
    this.middleName = middleName;
    this.lastName = lastName;
    this.birthday = birthday;
  }

  public String getName() {
    return name;
  }

  public String getMiddleName() {
    return middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public LocalDate getBirthday() {
    return birthday;
  }
}
