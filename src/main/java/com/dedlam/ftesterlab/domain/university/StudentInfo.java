package com.dedlam.ftesterlab.domain.university;

import com.dedlam.ftesterlab.domain.people.database.Person;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "students_info")
public class StudentInfo {
  public StudentInfo() {
  }

  public StudentInfo(UUID id, Person student) {
    this.id = id;
    this.student = student;
  }

  @Id
  private UUID id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id")
  private Person student;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Person getStudent() {
    return student;
  }

  public void setStudent(Person student) {
    this.student = student;
  }
}
