package com.dedlam.ftesterlab.domain.university;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeachersInfoRepository extends JpaRepository<TeacherInfo, UUID> {
}

