package com.dedlam.ftesterlab.auth.database;

import com.dedlam.ftesterlab.auth.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeachersRepository extends JpaRepository<Teacher, UUID> {
}
