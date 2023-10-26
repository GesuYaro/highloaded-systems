package com.dedlam.ftesterlab.domain.people;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "students_info")
public class StudentInfo {
  @Id
  UUID id;

  @Column(name = "personId")
  UUID personId;

  @Column(name = "group_id")
  UUID groupId;

  public StudentInfo() {

  }

  public StudentInfo(UUID id, UUID personId, UUID groupId) {
    this.id = id;
    this.personId = personId;
    this.groupId = groupId;
  }
}
