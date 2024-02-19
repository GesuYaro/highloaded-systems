package com.dedlam.ftesterlab.domain.tests.database;

import com.dedlam.ftesterlab.domain.tests.models.Test;
import com.dedlam.ftesterlab.domain.university.models.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TestRepository extends JpaRepository<Test, UUID> {
    Page<Test> findBySubject_Id(UUID id, Pageable pageable);
}
