package com.dedlam.ftesterlab.domain.university;

import com.dedlam.ftesterlab.domain.people.database.Person;
import jakarta.persistence.*;

import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "students_info")
public class StudentInfo {
  public StudentInfo() {
  }

  public StudentInfo(UUID id, Person student, Group group) {
    this.id = id;
    this.student = student;
    this.group = group;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "student_id", referencedColumnName = "id")
  private Person student;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "group_id", referencedColumnName = "id")
  private Group group;

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

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }
}
