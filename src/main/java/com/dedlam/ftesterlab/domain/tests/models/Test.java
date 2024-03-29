package com.dedlam.ftesterlab.domain.tests.models;

import com.dedlam.ftesterlab.domain.university.models.Subject;
import com.dedlam.ftesterlab.domain.university.models.TeacherInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tests")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Test {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id")
  private UUID id;

  @Column(name = "name")
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "subject_id")
  @ToString.Exclude
  private Subject subject;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "test", cascade = CascadeType.PERSIST)
  @ToString.Exclude
  private List<Question> questions;

  @Column(name = "duration")
  private Duration duration;

  @Column(name = "is_open")
  private Boolean isOpen;

  @ManyToOne
  @JoinColumn(name = "teacher_id")
  private TeacherInfo teacher;

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
    Test test = (Test) o;
    return getId() != null && Objects.equals(getId(), test.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }
}
