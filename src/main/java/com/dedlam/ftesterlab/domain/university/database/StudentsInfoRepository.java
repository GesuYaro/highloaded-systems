package com.dedlam.ftesterlab.domain.university.database;

import com.dedlam.ftesterlab.domain.university.models.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface StudentsInfoRepository extends JpaRepository<StudentInfo, UUID> {
  List<StudentInfo> findAllByIdIn(Set<UUID> ids);
}
