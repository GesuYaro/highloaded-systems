package com.dedlam.ftesterlab.auth.database;

import com.dedlam.ftesterlab.auth.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentsRepository extends JpaRepository<Student, UUID> {
}
