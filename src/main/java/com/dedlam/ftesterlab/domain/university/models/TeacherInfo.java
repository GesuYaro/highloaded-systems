package com.dedlam.ftesterlab.domain.university.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "teachers_info")
public class TeacherInfo {
  public TeacherInfo() {
  }

  public TeacherInfo(UUID id, UUID teacherId, List<Subject> subjects) {
    this.id = id;
    this.teacherId = teacherId;
    this.subjects = subjects;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "teacher_id", nullable = false, unique = true)
  private UUID teacherId;

  @OneToMany(mappedBy = "teacher")
  private List<Subject> subjects;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getTeacherId() {
    return teacherId;
  }

  public void setTeacherId(UUID teacherId) {
    this.teacherId = teacherId;
  }

  public List<Subject> getSubjects() {
    return subjects;
  }

  public void setSubjects(List<Subject> subjects) {
    this.subjects = subjects;
  }
}
