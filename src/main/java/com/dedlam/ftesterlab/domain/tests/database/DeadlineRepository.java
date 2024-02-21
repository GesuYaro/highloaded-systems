package com.dedlam.ftesterlab.domain.tests.database;

import com.dedlam.ftesterlab.domain.tests.models.Deadline;
import com.dedlam.ftesterlab.domain.university.models.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeadlineRepository extends JpaRepository<Deadline, UUID> {
    Page<Deadline> findByTest_Teacher_Teacher_Id(UUID id, Pageable pageable);

    Page<Deadline> findByGroup_Id(UUID id, Pageable pageable);

    Page<Deadline> findByGroup(Group group, Pageable pageable);
}
