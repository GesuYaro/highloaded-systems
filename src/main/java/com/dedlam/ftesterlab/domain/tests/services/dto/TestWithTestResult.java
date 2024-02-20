package com.dedlam.ftesterlab.domain.tests.services.dto;

import com.dedlam.ftesterlab.domain.tests.models.Test;
import com.dedlam.ftesterlab.domain.tests.models.TestResult;

public record TestWithTestResult(
        Test test,
        TestResult testResult
) {
}
