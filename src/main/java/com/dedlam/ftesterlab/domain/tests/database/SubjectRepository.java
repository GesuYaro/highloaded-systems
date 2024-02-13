package com.dedlam.ftesterlab.domain.tests.database;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, UUID> {
    Page<Subject> findByOwner_Id(UUID ownerId, Pageable pageable);
}
