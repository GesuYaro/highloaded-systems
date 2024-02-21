package com.dedlam.ftesterlab.domain.university.database;

import com.dedlam.ftesterlab.domain.university.models.Group;
import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GroupsRepository extends JpaRepository<Group, UUID> {
  boolean existsByName(String name);
  Optional<Group> findByName(String name);

  Window<Group> findByGradeNotNullOrderByNameAsc(OffsetScrollPosition offsetScrollPosition);
}
