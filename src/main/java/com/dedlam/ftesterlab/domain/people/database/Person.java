package com.dedlam.ftesterlab.domain.people.database;

import com.dedlam.ftesterlab.auth.models.DefaultUser;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "people")
public class Person {
  public Person() {
  }

  public Person(UUID id, String name, String middleName, String lastName, LocalDate birthday, DefaultUser user) {
    this.id = id;
    this.name = name;
    this.middleName = middleName;
    this.lastName = lastName;
    this.birthday = birthday;
    this.user = user;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column
  private String middleName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false)
  private LocalDate birthday;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private DefaultUser user;

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

  public DefaultUser getUser() {
    return user;
  }
}
