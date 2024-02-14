package com.dedlam.ftesterlab.domain.university;

import com.dedlam.ftesterlab.domain.university.models.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, UUID> {

    Page<Subject> findByTeacher_Teacher_Id(UUID id, Pageable pageable);
}
