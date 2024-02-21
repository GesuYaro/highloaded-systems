package com.dedlam.ftesterlab.domain.tests.models;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestResultRepository extends JpaRepository<TestResult, UUID> {
}