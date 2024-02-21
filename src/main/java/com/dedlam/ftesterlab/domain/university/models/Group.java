package com.dedlam.ftesterlab.domain.university.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "groups")
public class Group {
  public Group() {
  }

  public Group(UUID id, String name, int grade, List<StudentInfo> students, List<Subject> subjects) {
    this.id = id;
    this.name = name;
    this.grade = grade;
    this.students = students;
    this.subjects = subjects;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private int grade;

  @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
  private List<StudentInfo> students;

  @ManyToMany
  @JoinTable(
    name = "groups_to_subjects",
    joinColumns = @JoinColumn(name = "group_id"),
    inverseJoinColumns = @JoinColumn(name = "subject_id"))
  private List<Subject> subjects;

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

  public int getGrade() {
    return grade;
  }

  public void setGrade(int grade) {
    this.grade = grade;
  }

  public List<StudentInfo> getStudents() {
    return students;
  }

  public void setStudents(List<StudentInfo> students) {
    this.students = students;
  }

  public List<Subject> getSubjects() {
    return subjects;
  }

  public void setSubjects(List<Subject> subjects) {
    this.subjects = subjects;
  }
}
