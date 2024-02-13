package com.dedlam.ftesterlab.domain.university;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "subjects")
@Getter
@Setter
public class Subject {
  public Subject(UUID id, String name, TeacherInfo teacher) {
    this.id = id;
    this.name = name;
    this.teacher = teacher;
  }


  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String name;

  @ManyToOne
  @JoinColumn(name = "teacher_id", referencedColumnName = "id")
  private TeacherInfo teacher;

  @ManyToMany(mappedBy = "subjects")
  private List<Group> groups;

  @Override
  public final boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null)
      return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass)
      return false;
    Subject subject = (Subject) o;
    return getId() != null && Objects.equals(getId(), subject.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }
}
