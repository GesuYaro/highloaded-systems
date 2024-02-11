package com.dedlam.ftesterlab.domain.university;

import com.dedlam.ftesterlab.domain.people.database.Person;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "teachers_info")
public class TeacherInfo {
  public TeacherInfo() {
  }

  public TeacherInfo(UUID id, Person teacher) {
    this.id = id;
    this.teacher = teacher;
  }

  @Id
  private UUID id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id")
  private Person teacher;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Person getTeacher() {
    return teacher;
  }

  public void setTeacher(Person teacher) {
    this.teacher = teacher;
  }
}
