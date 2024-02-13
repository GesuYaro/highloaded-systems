package com.dedlam.ftesterlab.domain.university.models;

import com.dedlam.ftesterlab.domain.people.database.Person;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "teachers_info")
public class TeacherInfo {
  public TeacherInfo() {
  }

  public TeacherInfo(UUID id, Person teacher, List<Subject> subjects) {
    this.id = id;
    this.teacher = teacher;
    this.subjects = subjects;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "teacher_id", referencedColumnName = "id")
  private Person teacher;

  @OneToMany(mappedBy = "teacher")
  @Transient
  private List<Subject> subjects;

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

  public List<Subject> getSubjects() {
    return subjects;
  }

  public void setSubjects(List<Subject> subjects) {
    this.subjects = subjects;
  }
}
