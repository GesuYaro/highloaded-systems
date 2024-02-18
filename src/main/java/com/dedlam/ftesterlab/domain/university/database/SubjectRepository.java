package com.dedlam.ftesterlab.domain.university.database;

import com.dedlam.ftesterlab.domain.university.models.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, UUID> {

    Page<Subject> findByTeacher_Teacher_Id(UUID id, Pageable pageable);

    List<Subject> findAllByNameIn(Set<String> names);
}
