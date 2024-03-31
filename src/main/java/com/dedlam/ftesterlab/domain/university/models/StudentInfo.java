package com.dedlam.ftesterlab.domain.university.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "students_info")
public class StudentInfo {
  public StudentInfo() {
  }

  public StudentInfo(UUID id, UUID studentId, Group group) {
    this.id = id;
    this.studentId = studentId;
    this.group = group;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "student_id", unique = true, nullable = false)
  private UUID studentId;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "group_id", referencedColumnName = "id")
  private Group group;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getStudentId() {
    return studentId;
  }

  public @Nullable Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }
}
