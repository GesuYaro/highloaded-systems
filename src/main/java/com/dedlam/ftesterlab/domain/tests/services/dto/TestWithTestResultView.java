package com.dedlam.ftesterlab.domain.tests.services.dto;

public record TestWithTestResultView(
        TestStudentView test,
        TestResultView result
) {
}
